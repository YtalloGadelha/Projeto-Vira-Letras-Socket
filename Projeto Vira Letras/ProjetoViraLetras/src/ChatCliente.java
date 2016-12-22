/**
 * @author Ytallo de Lima Gadelha
 * Projeto Vira Letras
 * Versão 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ChatCliente extends JFrame {

	private JButton botaoEnviar;
	private JButton botaoIniciar;
	private JButton botaoReiniciar;
	private JButton botaoTerminar;
	private JButton botaoDesistir;
	private JButton botaoFinalizarJogada;
	private JButton botaoJogarDados;
	private JTextField jogadorAtual;
	private JTextField valorDados;
	private JTextField textoParaEnviar;
	private JTextField jogador1;
	private JTextField jogador2;
	private JTextField placarJogador1;
	private JTextField placarJogador2;
	private String nomeJogador = null;
	private JTextArea textoRecebido;
	private JScrollPane scrollTextoRecebido;
	private ArrayList<MyButton> pecasTabuleiro = new ArrayList<>();
	private Container tabuleiro;
	private Socket socketChat;
	private DataInputStream inputChat;
	private DataOutputStream outputChat;
	private Socket socketJogo;
	private DataInputStream inputJogo;
	private DataOutputStream outputJogo;
	private int numDados = 0;
	private int numDesv = 0;
	private String minhaVez;
	private int placar1 = 0;
	private int placar2 = 0;
	private int portaServidorChat;
	private int portaServidorJogo;
	private String servidorPortaChat = null;
	private String servidorPortaJogo = null;
	private String ipServidorChat = null;
	private String ipServidorJogo = null;

	public ChatCliente(){

		
		super();
		
		/**
		 * Obtenção do IP do servidor do Chat
		 */
		while (ipServidorChat == null || ipServidorChat.equals("")) {
			ipServidorChat = JOptionPane.showInputDialog("Informe o IP do servidor do Chat!");
		}
		System.err.println(ipServidorChat);
		
		/**
		 * Obtenção da Porta do servidor do Chat
		 */
		while (servidorPortaChat == null || servidorPortaChat.equals("")) {
			servidorPortaChat = JOptionPane.showInputDialog("Informe a PORTA do servidor do Chat!");
		}
		portaServidorChat = Integer.parseInt(servidorPortaChat);
		System.err.println(portaServidorChat);
		
		/**
		 * Obtenção do IP do servidor do Jogo
		 */
		while (ipServidorJogo == null || ipServidorJogo.equals("")) {
			ipServidorJogo = JOptionPane.showInputDialog("Informe o IP do servidor do Jogo!");
		}
		System.err.println(ipServidorJogo);
		
		/**
		 * Obtenção da Porta do servidor do Jogo
		 */
		while (servidorPortaJogo == null || servidorPortaJogo.equals("")) {
			servidorPortaJogo = JOptionPane.showInputDialog("Informe a PORTA do servidor do Jogo!");
		}
		portaServidorJogo = Integer.parseInt(servidorPortaJogo);
		System.err.println(portaServidorJogo);
		
		/**
		 * Configuração de uma fonte
		 */
		Font fonte = new Font("Serif", Font.PLAIN, 30);

		/**
		 * Configuração do botão enviar
		 */
		botaoEnviar = new JButton("Enviar");
		botaoEnviar.setBackground(new Color(255, 255, 255));
		botaoEnviar.setForeground(new Color(31, 58, 147));
		botaoEnviar.setFont(new Font("Serif", Font.PLAIN, 26));
		botaoEnviar.addActionListener(new EnviarListenerChat());

		/**
		 * Configuração do botão iniciar partida
		 */
		botaoIniciar = new JButton("Iniciar");
		botaoIniciar.setBackground(new Color(255, 255, 255));
		botaoIniciar.setForeground(new Color(31, 58, 147));
		botaoIniciar.setFont(new Font("Serif", Font.PLAIN, 20));
		botaoIniciar.addActionListener(new EnviarListenerJogo());

		/**
		 * Configuração do botão reiniciar partida
		 */
		botaoReiniciar = new JButton("Reiniciar");
		botaoReiniciar.setBackground(new Color(255, 255, 255));
		botaoReiniciar.setForeground(new Color(31, 58, 147));
		botaoReiniciar.setFont(new Font("Serif", Font.PLAIN, 20));
		botaoReiniciar.addActionListener(new EnviarListenerJogo());

		/**
		 * Configuração do botão terminar partida
		 */
		botaoTerminar = new JButton("Terminar");
		botaoTerminar.setBackground(new Color(255, 255, 255));
		botaoTerminar.setForeground(new Color(31, 58, 147));
		botaoTerminar.setFont(new Font("Serif", Font.PLAIN, 20));
		botaoTerminar.addActionListener(new EnviarListenerJogo());

		/**
		 * Configuração do botão desistir da partida
		 */
		botaoDesistir = new JButton("Desistir");
		botaoDesistir.setBackground(new Color(255,255,255));
		botaoDesistir.setForeground(new Color(31, 58, 147));
		botaoDesistir.setFont(new Font("Serif", Font.PLAIN, 20));
		botaoDesistir.addActionListener(new EnviarListenerJogo());

		/**
		 * Configuração do botão finalizar jogada
		 */
		botaoFinalizarJogada = new JButton("Finalizar Jogada");
		botaoFinalizarJogada.setBackground(new Color(255, 255, 255));
		botaoFinalizarJogada.setForeground(new Color(31, 58, 147));
		botaoFinalizarJogada.setFont(new Font("Serif", Font.PLAIN, 20));
		botaoFinalizarJogada.addActionListener(new EnviarListenerJogo());

		/**
		 * Configuração do botão jogar dados
		 */
		botaoJogarDados = new JButton("Jogar Dados");
		botaoJogarDados.setBackground(new Color(255, 255, 255));
		botaoJogarDados.setForeground(new Color(31, 58, 147));
		botaoJogarDados.setFont(new Font("Serif", Font.PLAIN, 20));
		botaoJogarDados.addActionListener(new EnviarListenerJogo());

		/**
		 * Configuração do textField do jogador atual
		 */
		jogadorAtual = new JTextField();
		jogadorAtual.setHorizontalAlignment(SwingConstants.CENTER);
		jogadorAtual.setEditable(false);
		jogadorAtual.setText("Jogador Atual");
		jogadorAtual.setBackground(new Color(255, 255, 255));
		jogadorAtual.setForeground(Color.DARK_GRAY);
		jogadorAtual.setFont(new Font("Serif", Font.PLAIN, 20));

		/**
		 * Configuração do textField que contém o valor dos dados
		 */
		valorDados = new JTextField();
		valorDados.setForeground(Color.DARK_GRAY);
		valorDados.setHorizontalAlignment(SwingConstants.CENTER);
		valorDados.setEditable(false);
		valorDados.setText("Valor dos Dados");
		valorDados.setBackground(new Color(255, 255, 255));
		valorDados.setFont(new Font("Serif", Font.PLAIN, 20));

		/**
		 * Configuração do textField do jogador 1
		 */
		jogador1 = new JTextField();
		jogador1.setForeground(Color.DARK_GRAY);
		jogador1.setHorizontalAlignment(SwingConstants.CENTER);
		jogador1.setEditable(false);
		jogador1.setText("Jogador 1: ");
		jogador1.setBackground(new Color(255, 255, 255));
		jogador1.setFont(new Font("Serif", Font.PLAIN, 20));

		/**
		 * Configuração do textField do jogador 2
		 */
		jogador2 = new JTextField();
		jogador2.setForeground(Color.DARK_GRAY);
		jogador2.setHorizontalAlignment(SwingConstants.CENTER);
		jogador2.setEditable(false);
		jogador2.setText("Jogador 2:");
		jogador2.setBackground(new Color(255, 255, 255));
		jogador2.setFont(new Font("Serif", Font.PLAIN, 20));

		/**
		 * Configuração do textField do placar do jogador 1
		 */
		placarJogador1 = new JTextField();
		placarJogador1.setForeground(Color.DARK_GRAY);
		placarJogador1.setHorizontalAlignment(SwingConstants.CENTER);
		placarJogador1.setEditable(false);
		placarJogador1.setText("0");
		placarJogador1.setBackground(new Color(255, 255, 255));
		placarJogador1.setFont(new Font("Serif", Font.PLAIN, 20));

		/**
		 * Configuração do textField do placar do jogador 2
		 */
		placarJogador2 = new JTextField();
		placarJogador2.setForeground(Color.DARK_GRAY);
		placarJogador2.setHorizontalAlignment(SwingConstants.CENTER);
		placarJogador2.setEditable(false);
		placarJogador2.setText("0");
		placarJogador2.setBackground(new Color(255, 255, 255));
		placarJogador2.setFont(new Font("Serif", Font.PLAIN, 20));

		/**
		 * Configuração do textField composto pelas informações a serem enviadas pelo chat
		 */
		textoParaEnviar = new JTextField();
		textoParaEnviar.setBackground(new Color(255, 250, 240));
		textoParaEnviar.setFont(fonte);

		/**
		 * Configurando a textArea composta pelas informações do chat vindas do servidor
		 */
		textoRecebido = new JTextArea();
		textoRecebido.setBackground(new Color(255, 250, 240));
		textoRecebido.setLineWrap(true);
		textoRecebido.setFont(new Font("Serif", Font.PLAIN, 20));
		textoRecebido.setEditable(false);

		/**
		 * Configuração do scroll composto pela textArea textoRecebido
		 */
		scrollTextoRecebido = new JScrollPane(textoRecebido);
		scrollTextoRecebido.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollTextoRecebido.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTextoRecebido.setPreferredSize(new Dimension(scrollTextoRecebido.getWidth(), 120));

		/**
		 * Container com os botões principais do jogo
		 */
		Container botoes = new JPanel();
		botoes.setBackground(new Color(100, 149, 237));
		((JComponent) botoes).setBorder(BorderFactory.createLineBorder(Color.gray));
		botoes.setLayout(new GridLayout(3,4,4,4));
		botoes.add(botaoIniciar,new Point(0,0));

		botoes.add(botaoReiniciar,new Point(0,1));
		botoes.add(botaoDesistir, new Point(0,2));
		botoes.add(botaoTerminar, new Point(0,3));

		botoes.add(jogadorAtual, new Point(1,0));
		botoes.add(botaoJogarDados, new Point(1,1));
		botoes.add(valorDados, new Point(1,2));
		botoes.add(botaoFinalizarJogada, new Point(1,3));
		botoes.add(jogador1, new Point(2,0));
		botoes.add(placarJogador1, new Point(2,1));
		botoes.add(jogador2, new Point(2,2));
		botoes.add(placarJogador2, new Point(2,3));
		getContentPane().add(BorderLayout.NORTH, botoes);

		/**
		 * Container com as peças do tabuleiro
		 */
		tabuleiro = new JPanel();
		((JComponent) tabuleiro).setBorder(new LineBorder(new Color(128, 128, 128), 1, true));
		tabuleiro.setLayout(new GridLayout(8, 8, 2, 2));
		tabuleiro.setBackground(Color.white);

		/**
		 * Carregamento as imagens de vogais e consoantes nos botões e adiciona no arrayList pecasTabuleiro
		 */
		carregarImagensVogais();
		carregarImagensConsoante();

		/**
		 * Inclusão de peças no tabuleiro(peças vazias)
		 */
		iniciarTabuleiro();

		/**
		 * Adição do tabuleiro no container principal
		 */
		getContentPane().add(BorderLayout.CENTER, tabuleiro);

		/**
		 * Container que possui apenas o textField e botaoEnviar
		 */
		Container envioTextFieldBotao = new JPanel();
		envioTextFieldBotao.setLayout(new BorderLayout());
		envioTextFieldBotao.add(BorderLayout.CENTER,textoParaEnviar);
		envioTextFieldBotao.add(BorderLayout.EAST,botaoEnviar);

		/**
		 * Container que possui todos os componetes de envio(scroll,textField e botaoEnviar)
		 */
		Container envio = new JPanel();
		envio.setLayout(new BorderLayout());
		envio.add(BorderLayout.CENTER, scrollTextoRecebido);
		envio.add(BorderLayout.SOUTH, envioTextFieldBotao);

		/**
		 * Adição do container Envio no container principal
		 */
		getContentPane().add(BorderLayout.SOUTH, envio);

		/**
		 * Configuração da conexão com os servidores
		 */
		configurarConexaoChat();
		configurarConexaoJogo();

		/**
		 * Configurações da Janela
		 */
		setSize(700, 750);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter());
	}

	class WindowAdapter implements WindowListener{

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowOpened(WindowEvent e) {}

		/**
		 * Método chamado quando o usuário fecha a janela
		 * Esse método é utilizado para que o fechamento da janela seja tratado 
		 * como uma desistência do usuário que provocou o fechamento.
		 * Obs: Só será tratado com desistência se o jogo já tiver sido iniciado.
		 * Caso o jogo ainda não tenha sido iniciado, ocorrerá simplesmente o fechamento da janela.
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {
			/**
			 * Verifico se o jogo já foi iniciado. Caso já tenha sido iniciado, qualquer tentativa de
			 * fechamento da janela, será tratada como desistência.
			 */
			if(botaoIniciar.isEnabled() == false){
				if (!(JOptionPane.showConfirmDialog(null, "Deseja mesmo encerrar o jogo?") == JOptionPane.OK_OPTION)) {
					return;
				}else{

					/**
					 * Se jogador 1 desistiu, então jogador 2 é o vencedor
					 */
					if(nomeJogador.equals("Jogador1")){
						try {
							outputJogo.writeUTF("ven2");
						} catch (IOException e) {
							System.err.println(e+"Botao desistir jogador 1");
						}
					}

					/**
					 * Se jogador 2 desistiu, então jogador 1 é o vencedor
					 */
					else if(nomeJogador.equals("Jogador2")){
						try {
							outputJogo.writeUTF("ven1");
						} catch (IOException e) {
							System.err.println(e+"Botao desistir jogador 2");
						}
					}
				}
			}
			System.exit(0);
		}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowClosed(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowIconified(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowDeiconified(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowActivated(WindowEvent e) {}

		/**
		 * Método não utilizado
		 */
		@Override
		public void windowDeactivated(WindowEvent e) {}
	}

	/**
	 * Método que carrega as imagens das vogais para o ArrayList pecasTabuleiro
	 */
	private void carregarImagensVogais(){

		ImageIcon iv;
		Image imagemVogal;
		MyButton button;

		for(int i=0; i<7; i++){
			iv = new ImageIcon(getClass().getResource("/Letra A.png"));
			imagemVogal = iv.getImage();
			button = new MyButton(imagemVogal);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<7; i++){
			iv = new ImageIcon(getClass().getResource("/Letra E.png"));
			imagemVogal = iv.getImage();
			button = new MyButton(imagemVogal);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<6; i++){
			iv = new ImageIcon(getClass().getResource("/Letra I.png"));
			imagemVogal = iv.getImage();
			button = new MyButton(imagemVogal);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<7; i++){
			iv = new ImageIcon(getClass().getResource("/Letra O.png"));
			imagemVogal = iv.getImage();
			button = new MyButton(imagemVogal);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<4; i++){
			iv = new ImageIcon(getClass().getResource("/Letra U.png"));
			imagemVogal = iv.getImage();
			button = new MyButton(imagemVogal);
			pecasTabuleiro.add(button);
		}
	}

	/**
	 * Método que carrega as imagens das consoantes para o ArrayList pecasTabuleiro
	 */
	private void carregarImagensConsoante(){

		ImageIcon ic;
		Image imagemConsoante;
		MyButton button;

		for(int i=0; i<2; i++){
			ic = new ImageIcon(getClass().getResource("/Letra B.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){
			ic = new ImageIcon(getClass().getResource("/Letra C.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){
			ic = new ImageIcon(getClass().getResource("/Letra D.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		ic = new ImageIcon(getClass().getResource("/Letra F.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);

		ic = new ImageIcon(getClass().getResource("/Letra G.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);

		ic = new ImageIcon(getClass().getResource("/Letra H.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);

		ic = new ImageIcon(getClass().getResource("/Letra J.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);


		for(int i=0; i<3; i++){
			ic = new ImageIcon(getClass().getResource("/Letra L.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){
			ic = new ImageIcon(getClass().getResource("/Letra M.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){

			ic = new ImageIcon(getClass().getResource("/Letra N.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){

			ic = new ImageIcon(getClass().getResource("/Letra P.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		ic = new ImageIcon(getClass().getResource("/Letra Qu.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);

		for(int i=0; i<3; i++){
			ic = new ImageIcon(getClass().getResource("/Letra R.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}


		for(int i=0; i<4; i++){
			ic = new ImageIcon(getClass().getResource("/Letra S.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){
			ic = new ImageIcon(getClass().getResource("/Letra T.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		for(int i=0; i<2; i++){
			ic = new ImageIcon(getClass().getResource("/Letra V.png"));
			imagemConsoante = ic.getImage();
			button = new MyButton(imagemConsoante);
			pecasTabuleiro.add(button);
		}

		ic = new ImageIcon(getClass().getResource("/Letra X.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);

		ic = new ImageIcon(getClass().getResource("/Letra Z.png"));
		imagemConsoante = ic.getImage();
		button = new MyButton(imagemConsoante);
		pecasTabuleiro.add(button);

	}

	/**
	 * Método que configura a conexão do cliente com o servidor do chat
	 */
	private void configurarConexaoChat(){

		try{
			socketChat = new Socket(ipServidorChat, portaServidorChat);
			inputChat = new DataInputStream(socketChat.getInputStream());
			outputChat = new DataOutputStream(socketChat.getOutputStream());
			new Thread(new EscutaServidorChat()).start();

		}catch(Exception e){
			System.err.println(e);
		}
	}

	/**
	 * Método que configura a conexão do cliente com o servidor do jogo
	 */
	private void configurarConexaoJogo(){
		try {
			socketJogo = new Socket(ipServidorJogo, portaServidorJogo);
			inputJogo = new DataInputStream(socketJogo.getInputStream());
			outputJogo = new DataOutputStream(socketJogo.getOutputStream());
			new Thread(new EscutaServidorJogo()).start();

		} catch (Exception e) {
			System.err.println(e+ " configurarConexaoJogo");
		}

	}

	/**
	 * Método que envia algumas informações do jogo para o servidor.
	 */
	private class EnviarListenerJogo implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent g) {

			JButton btnGame = (JButton) g.getSource();

			/**
			 * Caso em que o botão Jogar Dados é clicado
			 * Nesse caso, o comando referente aos dados é enviado ao servidor juntamente com o valor 
			 * relativo a soma dos 2 dados.
			 */
			if(btnGame.getText() == "Jogar Dados"){
				/**
				 * Verifico se o jogo já foi iniciado. Se sim, o jogador atual pode jogar os dados.
				 */
				if(botaoIniciar.isEnabled() == false){
					int dado1,dado2;
					Random gerador = new Random();
					dado1 = gerador.nextInt(6)+1;
					dado2 = gerador.nextInt(6)+1;
					numDados = dado1 + dado2;
					numDesv = numDados;
					botaoJogarDados.setEnabled(false);

					try {
						outputJogo.writeUTF("dad"+numDados);
					} catch (IOException e) {
						System.err.println(e+"Botão jogar dados");
					}
				}
			}

			/**
			 * Caso em que o botão Finalizar Jogada é clicado
			 * Nesse caso, o comando informando a finalização da jogada é enviado ao servidor.
			 * Juntamente com o comando, vai a informação de quem finalizou a jogada e o seu placar.
			 */
			else if(btnGame.getText() == "Finalizar Jogada"){
				/**
				 * Verifico se o jogo já foi iniciado. Se sim, o jogador atual pode finalizar a jogada.
				 */
				if(botaoIniciar.isEnabled() == false){
					botaoJogarDados.setEnabled(true);
					if(nomeJogador.equals("Jogador1")){
						try {
							outputJogo.writeUTF("fin1"+placar1);
						} catch (Exception e) {
							System.err.println(e+"Finaliza jogada: Jogador1");
						}
					}
					else if(nomeJogador.equals("Jogador2")){
						try {
							outputJogo.writeUTF("fin2"+placar2);
						} catch (Exception e) {
							System.err.println(e+"Finaliza jogada: Jogador2");
						}
					}
				}
			}

			/**
			 * Caso em que o botão Iniciar é clicado.
			 * Nesse caso, o comando informando o início do jogo é enviado ao servidor.
			 * Juntamente com o comando, vai a informação de quem iniciou o jogo.
			 * Ao iniciar o jogo, também é enviado o comando para misturar as peças do tabuleiro
			 */
			else if(btnGame.getText() == "Iniciar"){
				if(nomeJogador.equals("Jogador1")){
					try {
						outputJogo.writeUTF("ini1");
					} catch (IOException e) {
						System.err.println(e+"Botão iniciar: Jogador1");
					}
				}
				else if(nomeJogador.equals("Jogador2")){
					try {
						outputJogo.writeUTF("ini2");
					} catch (IOException e) {
						System.err.println(e+"Botão iniciar: Jogador2");
					}
				}

				/**
				 * Envio do comando para misturar as peças do tabuleiro juntamente com um dos índices
				 * que será necessário.
				 */
				try {
					Random gerador = new Random();
					int ind;

					for(int i = 0; i < 70; i++){
						ind = gerador.nextInt(63)+1;
						outputJogo.writeUTF("mis"+ind);
					}

				} catch (Exception e) {
					System.err.println(e+"Botão iniciar: misturando as peças do tabuleiro");
				}
			}

			/**
			 * Caso em que o botão Desistir é clicado.
			 * Nesse caso, o comando informando a desistência da partida é enviado ao servidor.
			 * Juntamente com o comando, vai a informação de quem será beneficiado com a desistência,
			 * ou seja, aquele que ganhará a partida.
			 */
			else if(btnGame.getText() == "Desistir"){

				/**
				 * Verifico se o jogo já foi iniciado. Se sim, o Jogadoratual pode desistir da partida.
				 */
				if(botaoIniciar.isEnabled() == false){
					/**
					 * Se Jogador1 desistiu, então Jogador2 é o vencedor.
					 */
					if(nomeJogador.equals("Jogador1")){
						try {
							outputJogo.writeUTF("ven2");
						} catch (IOException e) {
							System.err.println(e+"Botão desistir Jogador1");
						}
					}

					/**
					 * Se Jogador2 desistiu, então Jogador1 é o vencedor
					 */
					else if(nomeJogador.equals("Jogador2")){
						try {
							outputJogo.writeUTF("ven1");
						} catch (IOException e) {
							System.err.println(e+"Botão desistir Jogador2");
						}
					}
				}
			}

			/**
			 * Caso em que o botão Terminar é clicado.
			 * Nesse caso, o comando informando o término da partida é enviado ao servidor.
			 * Juntamente com o comando, vai a informação de quem vencerá a partida.
			 */
			else if(btnGame.getText() == "Terminar"){
				/**
				 * Verifico se o jogo já foi iniciado. Se sim, o Jogadoratual pode terminar a jogada
				 */
				if(botaoIniciar.isEnabled() == false){
					int placar1 = Integer.parseInt(placarJogador1.getText());
					int placar2 = Integer.parseInt(placarJogador2.getText());

					if(placar1 > placar2){
						try {
							outputJogo.writeUTF("ven1");
						} catch (IOException e) {
							System.err.println(e+"Botão terminar: 1º venceu");
						}
					}

					else if(placar2 > placar1){

						try {
							outputJogo.writeUTF("ven2");
						} catch (IOException e) {
							System.err.println(e+"Botão terminar: 2º venceu");
						}
					}

					else if(placar1 == placar2){

						try {
							outputJogo.writeUTF("ven3");
						} catch (IOException e) {
							System.err.println(e+"Botão terminar: Empate");
						}
					}
				}
			}

			/**
			 * Caso em que o botão Reiniciar é clicado.
			 * Nesse caso, o comando informando o reinício da partida é enviado ao servidor.
			 */
			else if(btnGame.getText() == "Reiniciar"){
				/**
				 * Verifico se o jogo já foi iniciado. Se sim, o Jogadoratual pode reiniciar a jogada
				 */
				if(botaoIniciar.isEnabled() == false){
					try {
						outputJogo.writeUTF("rei");
					} catch (Exception e) {
						System.err.println(e+"Botão reiniar");
					}
				}
			}
		}
	}

	/**
	 * Método que envia as informações do chat para o servidor
	 */
	private class EnviarListenerChat implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			try{
				outputChat.writeUTF(nomeJogador + ": " + textoParaEnviar.getText());
				outputChat.flush();
				textoParaEnviar.setText("");
				textoParaEnviar.requestFocus();

			}catch(Exception b){
				System.err.println(b);
			}
		}
	}

	/**
	 * Método que recebe as informações do servidor referentes ao chat
	 */
	private class EscutaServidorChat implements Runnable{
		@Override
		public void run() {

			try{
				String textoChat;
				while((textoChat = inputChat.readUTF()) != null){
					textoRecebido.append(textoChat + "\n");
					textoRecebido.setCaretPosition(textoRecebido.getText().length()-1);
				}
			}catch(Exception x){
				System.err.println(x);
			}
		}
	}

	/**
	 * Método que recebe as informações do servidor referentes ao jogo
	 */
	private class EscutaServidorJogo implements Runnable{
		@Override
		public void run(){

			String textoJogo;
			String acao;
			int ind;

			try {
				while((textoJogo = inputJogo.readUTF()) != null){

					/**
					 * Criação de uma substring com as 3 primeiras letras que chegarão do servidor.
					 * Essa substring representa o comando das ações que serão tomadas.
					 */
					acao = textoJogo.substring(0, 3);
					
					/**
					 * Caso em que é recebido o comando para identificar os jogadores do jogo.
					 * O que possui índice 1 recebe o nome de Jogador1 e o que possui índice 2
					 * recebe o nome de Jogador2
					 */
					if(acao.equals("jog")){
						
						ind = Integer.parseInt(textoJogo.substring(3));
						
						if(ind == 1){
							nomeJogador = "Jogador1";
						}

						if(ind == 2){
							if(nomeJogador == null)
								nomeJogador = "Jogador2";
						}
					}
					
					/**
					 * Caso em que é recebido o comando para a mistura de peças do arrayList pecasTabuleiro.
					 * Juntamente ao comando se encontra um dos índices usados na troca de posições das peças.
					 * Essa troca é feita sempre entre o primeiro índice do arrayList e o índice que é recebido.
					 */
					else if(acao.equals("mis")){

						ind = Integer.parseInt(textoJogo.substring(3));
						Collections.swap(pecasTabuleiro, 0, ind);
					}

					/**
					 * Caso em que é recebido o comando para a finalização de uma jogada.
					 * Juntamente ao comando se encontra o índice do jogador que finalizou a jogada e
					 * também a sua pontuação para que a mesma possa ser atualizada no textField referente
					 * ao seu placar.
					 */
					else if(acao.equals("fin")){

						String indice = textoJogo.substring(3);
						int novoInd = Integer.parseInt(indice.substring(0,1));
						String pontuacao = indice.substring(1);

						/**
						 * A string minhaVez é umas das variáveis que ajudam no controle de turno da partida
						 * e também serve para informar o jogador atual.
						 */
						if(novoInd == 1){
							minhaVez = "Jogador2";
							jogadorAtual.setText(minhaVez);
							placarJogador1.setText(pontuacao);
						}

						if(novoInd == 2){
							minhaVez = "Jogador1";
							jogadorAtual.setText(minhaVez);
							placarJogador2.setText(pontuacao);
						}

						/**
						 * Configuração para o controle de turno.
						 */
						if(!nomeJogador.equals(minhaVez)){
							desabilitarBotoes();
						}

						if(nomeJogador.equals(minhaVez)){
							habilitarBotoes();
						}

						/**
						 * Verificação das peças que não formaram palavra ao finalizar uma jogada.
						 * Caso haja alguma peça que o Jogadoresqueceu de desvirar, essa é desvirada
						 * ao finalizar a jogada.
						 */
						for(int i = 0; i < 64 ; i++){

							MyButton btn = (MyButton) tabuleiro.getComponent(i);

							if(btn.hasImage == true && btn.beenDisabled == false){

								try {
									outputJogo.writeUTF("des"+i);
								} catch (IOException e1) {
									System.err.println(e1+"Envia comando desvira");
								}
							}
						}

						/**
						 * Verificação se ainda existem peças a serem desviradas.
						 * Caso não haja mais peças, os placares dos jogadores são verificados e
						 * o jogador vencedor é informado.
						 */
						verificarTermino();

						/**
						 * Variáveis que também ajudam no controle de turno da partida.
						 * numDados é zerado para evitar das jogadas atuais com as anteriores.
						 * numDesv é zerado para evitar que o jogador oponente desvire as peças do jogador atual.
						 */
						numDados = 0;
						numDesv = 0;
					}

					/**
					 * Caso em que é recebido o comando para virar uma peça.
					 * Juntamente ao comando é recebido o índice de qual peça deverá ser virada.
					 */
					if(acao.equals("vir") ){
						ind = Integer.parseInt(textoJogo.substring(3));
						MyButton btn = (MyButton) tabuleiro.getComponent(ind);

						btn.setVisible(false);
						tabuleiro.remove(ind);
						btn = pecasTabuleiro.get(ind);
						btn.hasImage = true;
						btn.beenDisabled = false;
						btn.setVisible(true);
						btn.putClientProperty("indice", new Point(ind, 0));
						btn.setBorder(BorderFactory.createLineBorder(Color.gray));
						tabuleiro.add(btn,ind);	
						tabuleiro.validate();
					}

					/**
					 * Caso em que é recebido o comando para desvirar uma peça.
					 * Juntamente ao comando é recebido o índice de qual peça deverá ser desvirada.
					 */
					else if(acao.equals("des")){
						ind = Integer.parseInt(textoJogo.substring(3));
						MyButton btn = (MyButton) tabuleiro.getComponent(ind);

						btn.setVisible(false);
						tabuleiro.remove(ind);
						btn = new MyButton();
						btn.hasImage = false;
						btn.beenDisabled = false;
						btn.setVisible(true);
						btn.putClientProperty("indice", new Point(ind, 0));
						btn.setBorder(BorderFactory.createLineBorder(Color.gray));
						tabuleiro.add(btn, ind);
						tabuleiro.validate();
					}

					/**
					 * Caso em que é recebido o comando para excluir uma peça.
					 * Juntamente ao comando é recebido o índice de qual peça deverá ser excluída.
					 */
					else if(acao.equals("exc")){
						ind = Integer.parseInt(textoJogo.substring(3));
						MyButton btn = (MyButton) tabuleiro.getComponent(ind);

						btn.setVisible(false);
						tabuleiro.remove(ind);

						ImageIcon iv = new ImageIcon(getClass().getResource("/Vazio.png"));
						Image imagemVazio = iv.getImage();
						btn = new MyButton(imagemVazio);

						btn.putClientProperty("indice", new Point(ind, 0));
						btn.setBorder(BorderFactory.createLineBorder(Color.gray));
						btn.setEnabled(false);
						tabuleiro.add(btn, ind);
						tabuleiro.validate();

						btn.beenDisabled = true;
						pecasTabuleiro.get(ind).beenDisabled = true;

						if(minhaVez.equals("Jogador1"))
							placar1++;

						else if(minhaVez.equals("Jogador2"))
							placar2++;
					}

					/**
					 * Caso em que é recebido o comando informando que os dados foram jogados.
					 * Juntamente ao comando é recebido o valor referente ao soma dos dados.
					 */
					else if(acao.equals("dad")){
						ind = Integer.parseInt(textoJogo.substring(3));
						valorDados.setText(Integer.toString(ind));
					}

					/**
					 * Caso em que é recebido o comando informando que houve um vencedor.
					 * Juntamente ao comando é recebido o índice informando quem foi o vencedor.
					 */
					else if(acao.equals("ven")){
						ind = Integer.parseInt(textoJogo.substring(3));

						if(ind == 1){
							JOptionPane.showMessageDialog(null, "Jogador1 é o Vencedor!", "Fim", 0);
						}
						else if(ind == 2){
							JOptionPane.showMessageDialog(null, "Jogador2 é o Vencedor!", "Fim", 0);
						}
						else if(ind == 3){
							JOptionPane.showMessageDialog(null, "Ocorreu Empate!", "Fim", 0);
						}

						/**
						 * Quando alguém termina ou desiste da partida, desabilito os botões finalizar jogada
						 *  e jogar dados e habilito o botão iniciar.
						 */
						botaoFinalizarJogada.setEnabled(false);
						botaoJogarDados.setEnabled(false);
						botaoReiniciar.setEnabled(true);
					}

					/**
					 * Caso em que é recebido o comando informando que a partida será iniciada.
					 * Juntamente ao comando é recebido o índice informando quem iniciou.
					 */
					else if(acao.equals("ini")){
						ind = Integer.parseInt(textoJogo.substring(3));

						botaoIniciar.setEnabled(false);

						/**
						 * Variável minhaVez ajudando novamente no controle de fluxo.
						 */
						if(ind == 1){
							minhaVez = "Jogador1";
							jogadorAtual.setText(minhaVez);
						}

						if(ind == 2){
							minhaVez = "Jogador2";
							jogadorAtual.setText(minhaVez);
						}

						if(!nomeJogador.equals(minhaVez)){
							desabilitarBotoes();
						}

						if(nomeJogador.equals(minhaVez)){
							habilitarBotoes();
						}
						
						botaoIniciar.setEnabled(false);
					}

					/**
					 * Caso em que é recebido o comando para que a partida seja reiniciada.
					 */
					else if(acao.equals("rei")){
						reiniciarJogo();
					}

					/**
					 * Caso em que é recebido o comando para que seja avisado sobre a conexão
					 * do segundo jogador ao GameServer.
					 */
					else if(acao.equals("ale")){
						String alertaJogo = "2º jogador conectado ao GameServer. Jogo pronto para ser iniciado!";
						textoRecebido.append(alertaJogo + "\n");
						textoRecebido.setCaretPosition(textoRecebido.getText().length()-1);
					}
				}

			} catch (Exception e) {
				System.err.println(e+" EscutaServidorJogo");
			}
		}
	}

	/**
	 * Método que verificará se a partida terminou após a finalização de cada jogada.
	 */
	private void verificarTermino(){

		int placar1 = Integer.parseInt(placarJogador1.getText());
		int placar2 = Integer.parseInt(placarJogador2.getText());

		if(placar1+placar2 == pecasTabuleiro.size()){

			if(placar1 > placar2){
				try {
					outputJogo.writeUTF("ven1");
				} catch (IOException e) {
					System.err.println(e+"Botão terminar: 1º venceu");
				}
			}

			else if(placar2 > placar1){

				try {
					outputJogo.writeUTF("ven2");
				} catch (IOException e) {
					System.err.println(e+"Botão terminar: 2º venceu");
				}
			}

			else if(placar1 == placar2){

				try {
					outputJogo.writeUTF("ven3");
				} catch (IOException e) {
					System.err.println(e+"Botão terminar: Empate");
				}
			}
			habilitarBotoes();
		}
	}

	/**
	 * Método que reiniciará o jogo quando necessário.
	 */
	private void reiniciarJogo(){

		placar1 = 0;
		placar2 = 0;
		habilitarBotoes();
		botaoIniciar.setEnabled(true);
		botaoDesistir.setEnabled(true);
		numDados = 0;
		numDesv = 0;
		jogadorAtual.setText("Jogador Atual");
		valorDados.setText("Valor dos Dados");
		placarJogador1.setText("0");
		placarJogador2.setText("0");
		tabuleiro.removeAll();
		tabuleiro.validate();
		for (MyButton myButton : pecasTabuleiro) {
			myButton.beenDisabled = false;
		}
		iniciarTabuleiro();

	}

	/**
	 * Método que inicia o tabuleiro com peças vazias
	 */
	private void iniciarTabuleiro(){

		for (int i = 0; i < 64; i++) {
			MyButton btn = new MyButton();
			btn.setBorder(BorderFactory.createLineBorder(Color.gray));
			btn.putClientProperty("indice", new Point(i, 0));
			btn.hasImage = false;
			btn.beenDisabled = false;
			tabuleiro.add(btn);
		}
		tabuleiro.validate();
	}

	/**
	 * Método que desabilita os botões que julguei necessário no controle de turno.
	 */
	private void desabilitarBotoes(){
		botaoReiniciar.setEnabled(false);
		botaoJogarDados.setEnabled(false);
		botaoFinalizarJogada.setEnabled(false);
	}

	/**
	 * Método que habilita os botões que julguei necessário no controle de turno.
	 */
	private void habilitarBotoes(){
		botaoReiniciar.setEnabled(true);
		botaoJogarDados.setEnabled(true);
		botaoFinalizarJogada.setEnabled(true);
	}

	/**
	 * Classe personalizada que possui as configurações de um botão(MyButton)
	 */
	class MyButton extends JButton {

		boolean hasImage = false;
		boolean beenDisabled = false;

		public MyButton() {

			super();
			initUI();
		}

		public MyButton(Image image) {

			super(new ImageIcon(image));

			initUI();
		}

		private void initUI() {

			BorderFactory.createLineBorder(Color.gray);

			addMouseListener(new MouseAdapter()); 
		}

		class MouseAdapter implements MouseListener{

			long tempoInicial, tempoFinal, resultado;

			/**
			 * Método chamado quando uma peça é clicada
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				MyButton btn = (MyButton)e.getSource();
				Point p = (Point) btn.getClientProperty("indice");
				int indice = p.x;

				/**
				 * Quando o botão possui imagem, a ação para desvirar a peça é enviada ao servidor
				 */
				if(btn.hasImage == true && btn.beenDisabled == false){

					if(numDesv > 0){
						try {
							outputJogo.writeUTF("des"+indice);
						} catch (IOException e1) {
							System.err.println(e1+"Envia comando desvira");
						}
						numDesv --;
					}
				}

				/**
				 * Quando o botão não possui imagem, a ação para virar a peça é enviada ao servidor
				 */
				else if(btn.hasImage == false && btn.beenDisabled == false){
					if(numDados > 0){

						try {
							outputJogo.writeUTF("vir"+indice);
						} catch (IOException e1) {
							System.err.println(e1+"Envio comando vira");
						}
						numDados--;
					}
				}
			}


			/**
			 * Método que deveria ser chamado quando uma peça é pressionada por um tempo determinado.
			 * Esse método é muito parecido com o método mouseClicked e é utilizado para pegar 
			 * o tempo do sistema quando uma peça é pressionada. 
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				tempoInicial = (long) System.currentTimeMillis();
			}

			/**
			 * Método chamado quando uma peça é liberada.
			 * Nesse método, pego novamente o tempo do sistema para tentar simular um long press.
			 * Com os valores obtidos, é realizada uma subtração e verificado se esse resultado
			 * é maior do que um determinado valor, no caso 300, que julguei ser equivalente a um long press.
			 */
			@Override
			public void mouseReleased(MouseEvent e) {

				MyButton btn = (MyButton)e.getSource();
				Point p = (Point) btn.getClientProperty("indice");
				int indice = p.x;
				tempoFinal = (long) System.currentTimeMillis();
				resultado = tempoFinal - tempoInicial;

				/**
				 * Caso o "long press" seja confirmado, a ação para excluir a peça é enviada ao servidor
				 */
				if(resultado >= 250 && btn.hasImage == true){

					if(numDesv > 0){
						/**
						 * É necessário mudar o estado do booleano beenDisable para 
						 * evitar a entrada no método mouseClicked e mande o comando para desvirar a peça
						 */
						btn.beenDisabled = true;
						try {
							outputJogo.writeUTF("exc"+indice);
						} catch (IOException e1) {
							System.err.println(e1+"Envio comando exclui");
						}
						numDesv--;
					}
				}
			}

			/**
			 * Método não utilizado
			 */
			@Override
			public void mouseEntered(MouseEvent e) {}

			/**
			 * Método não utilizado
			 */
			@Override
			public void mouseExited(MouseEvent e) {}
		}
	}

	public static void main(String[] args) {

		new ChatCliente();
	}
}
