import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ChatServer {

	Socket socketChat;
	ServerSocket serverChat;
	DataOutputStream saidaChat;
	String alertaChat = "2º jogador conectado ao ChatServer!";
	int porta;
	
	List<DataOutputStream> escritoresChat = new ArrayList<>();

	public ChatServer() {
		
		String numero = null;
		while (numero == null || numero.equals("")) {
		numero = JOptionPane.showInputDialog("Informe a porta do servidor do Chat!");
		}
		
		porta = Integer.parseInt(numero);
		System.err.println(porta);

		try{
			serverChat = new ServerSocket(porta);

			while(escritoresChat.size() < 2){
				socketChat = serverChat.accept();
				new Thread(new EscutaClienteChat(socketChat)).start();
				saidaChat = new DataOutputStream(socketChat.getOutputStream());
				escritoresChat.add(saidaChat); 
			}
			if(escritoresChat.size() == 2){
				encaminhaParaTodosChat(alertaChat);
			}

		} catch (Exception a) {
			System.err.println(a);
		}
	}

	private void encaminhaParaTodosChat(String texto){

		for (DataOutputStream writer : escritoresChat) {
			try{
				writer.writeUTF(texto);
				writer.flush();

			}catch(Exception x ){
				System.err.println(x);
			}
		}
	}

	private class EscutaClienteChat implements Runnable {

		DataInputStream entrada;

		public EscutaClienteChat(Socket socket){

			try{
				entrada = new DataInputStream(socket.getInputStream());
			}catch (Exception e) {
				System.err.println(e);
			}
		}

		@Override
		public void run() {

			try{
				String textoChat;
				while((textoChat = entrada.readUTF()) != null){
					encaminhaParaTodosChat(textoChat);
				}
			}catch(Exception c){
				System.err.println(c);
			}

		}
	}

	public static void main(String[] args) {

		new ChatServer();
	}

}




