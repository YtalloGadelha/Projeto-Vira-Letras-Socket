import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class GameServer {

	Socket socketJogo;
	ServerSocket serverJogo;
	DataOutputStream saidaJogo;
	String alertaJogo = "Jogo pronto para ser iniciado!";
	int porta;

	List<DataOutputStream> escritoresJogo = new ArrayList<>();

	public GameServer() {
		/**
		 * Pega a porta do servidor
		 */
		String numero = null;
		while (numero == null || numero.equals("")) {
			numero = JOptionPane.showInputDialog("Informe a porta do servidor do Jogo!");
		}
		porta = Integer.parseInt(numero);
		System.err.println(porta);

		try {
			serverJogo = new ServerSocket(porta);

			while(escritoresJogo.size() < 2){

				socketJogo = serverJogo.accept();
				new Thread(new EscutaClienteJogo(socketJogo)).start();
				saidaJogo = new DataOutputStream(socketJogo.getOutputStream());
				escritoresJogo.add(saidaJogo);

				/**
				 * Quando o 1º jogador conecta, mando um comando para que ele receba o nome
				 * Jogador1  
				 */
				if(escritoresJogo.size() == 1){
					encaminhaParaTodosJogo("jog1");
					System.err.println("jog1");
				}

				/**
				 * Quando o 2º jogador conecta, mando um comando para que ele receba o nome
				 * Jogador2 
				 */
				if(escritoresJogo.size() == 2){
					encaminhaParaTodosJogo("jog2");
					System.err.println("jog2");
				}
			}

			/**
			 * Quando o 2º jogador conecta mando um aviso informando que o jogo pode iniciar.
			 */
			if(escritoresJogo.size() == 2){
				System.out.println(alertaJogo);
				encaminhaParaTodosJogo("ale");
			}

		} catch (Exception e) {
			System.err.println(e+"GameServer");
		}
	}

	private void encaminhaParaTodosJogo(String texto){

		for (DataOutputStream writer : escritoresJogo) {
			try{
				writer.writeUTF(texto);
				writer.flush();

			}catch(Exception x ){
				System.err.println(x);
			}
		}
	}

	private class EscutaClienteJogo implements Runnable {

		DataInputStream entrada;

		public EscutaClienteJogo(Socket socket){

			try{
				entrada = new DataInputStream(socket.getInputStream());
			}catch (Exception e) {
				System.err.println(e +" EscutaClienteJogo");
			}
		}

		@Override
		public void run() {

			try{
				String textoChat;
				while((textoChat = entrada.readUTF()) != null){
					encaminhaParaTodosJogo(textoChat);
				}
			}catch(Exception c){
				System.err.println(c);
			}
		}
	}

	public static void main(String[] args) {

		new GameServer();
	}

}
