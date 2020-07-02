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

	private ConnectDB conDB;
	private Clients chatAdmin;
	private ServerSocket serverSocket;
	private int port;
	private JTextArea jta;
	private ArrayList<Socket> clients;
	
	// 2. Server 초기화 및 서버 시작
	public Server(int port, JTextArea jta) {
		this.port = port;
		this.jta = jta;
		clients = new ArrayList<Socket>();
		chatAdmin = new Clients(null);
		conDB = new ConnectDB();
		openServer();
		this.start();
	}
	
	// 3. 서버 오픈
	private void openServer() {
		try {
			serverSocket = new ServerSocket(port);
			jta.append("서버가 열렸습니다 \r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 5. 관리자(서버) 메시지 전송
	public void sendAdminMsg(String msg) {
		String str = "관리자 : " + msg;
		chatAdmin.send(serverSocket.getInetAddress().getHostAddress(), str);
	}
	
	// 4. 클라이언트 접속 대기
	public void run() {
		while(true) {
			try {
				// 클라이언트 요청을 받아들인다
				Socket socket = serverSocket.accept();
				// 클래이언트에게 서비스 제공 및 Clients 스레드 실행
				new Clients(socket).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 내부 클래스 : 클라이언트 객체
	// 6. Server 스레드에 의해 실행된 Clients 스레드
	class Clients extends Thread {
		private Socket socket;
		private BufferedReader br;	//입력
		private BufferedWriter bw;	//출력
		
		public Clients(Socket socket) {
			this.socket = socket;
		}
		
		// 메시지 전송
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
			// setCaretPosition: 스크롤을 데이터양에 따라 자동으로 움직인다 
			jta.setCaretPosition(jta.getText().length());
			conDB.insert(ip, msg);
		}
		
		// 한명의 클라이언트로부터 받은 메시지를 서버에 접속해 있는 모든 클라이언트에게 전송
		public void run() {
			String ip = null;
			String msg;
			
			try {
				// 클라이언트 추가
				clients.add(socket);
				ip = socket.getInetAddress().getHostAddress();
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				
				msg = "(" + ip +") 님이 입장하셨습니다.";
				send(ip, msg);
				
				// 클라이언트 메시지 읽기
				while((msg = br.readLine()) != null) {	//입력이 비어있지 않다면 실행
					// 모든 클라이언트들에게 메시지 전송
					msg = "(" + ip + ") : " + msg; 
					send(ip, msg);
				}
			} catch (IOException e) {
				// 클라이언트 제거
				clients.remove(socket);
				socket = null;
				
				msg = "(" + ip +") 님이 퇴장하셨습니다.";
				send(ip, msg);
			}
		}
	}
}








