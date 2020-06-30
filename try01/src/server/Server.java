package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class Server extends Thread {
	// 멤버 변수
	private Clients chatAdmin;
	private ServerSocket serverSocket;
	private int port;
	private JTextArea jta;
	private ArrayList <Socket> clients;
	
	// 생성자
	public Server(int port, JTextArea jta) {
		this.port = port;
		this.jta = jta;
		clients = new ArrayList<Socket>();
		chatAdmin = new Clients(null);
		openServer();
		this.start();
	}
	
	// 서버 오픈
	private void openServer() {
		try {
			serverSocket = new ServerSocket(port);
			jta.append("서버가 열렸습니다 \r\n");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
		
	// 관리자(서버) 메시지 전송
	public void sendAdminMsg(String msg) {
		String str = "관지자 : " + msg;
		chatAdmin.send(serverSocket.getInetAddress().getHostAddress(), str);
	}
		
	// 클라이언트 접속 대기
	public void run() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				new Clients(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	// 내부 클래스: 클라이언트의 객체
	class Clients extends Thread {
		private Socket socket;
		private BufferedReader br;
		private BufferedWriter bw;
		
		public Clients(Socket socket) {
			this.socket = socket;
		}
		
		private void send(String ip, String msg) {
			for(Socket socket : clients) {
				try {
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
					bw.write(msg);
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			jta.append(msg + "\r\n");
			jta.setCaretPosition(jta.getText().length());
		}
		
		public void run() {
			String ip = null;
			String msg;
			
			try {
				clients.add(socket);
				ip = socket.getInetAddress().getHostAddress();
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				
				msg = "(" + ip + ")님이 입장하셨습니다.";
				send(ip, msg);
				
				while((msg = br.readLine()) != null) {
					msg = "(" + ip + ") : " + msg;
					send(ip, msg);
				}
			} catch (IOException e) {
				clients.remove(socket);
				socket = null;
				
				msg = "(" + ip + ") 님이 퇴장하셨습니다.";
				send(ip, msg);
			}
		}
	}
}




