package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	// 멤버변수
	private Client client;
	private Container ct;
	private JPanel jpTop, jpCenter, jpBottom;
	private JTextField jtfIP, jtfPort, jtfChat;
	private JButton jbConnect, jbSend;
	private JTextArea jtaLog;
	private JScrollPane jspLog;
	
	// 생성자
	Frame(String title, int width, int height){
		initComps();
		addComps();
		addActionListener();
		showWnd(title, width, height);
	}
	
	// 변수 생성
	private void initComps() {
		ct = getContentPane();
		jpTop = new JPanel();
		jpCenter = new JPanel();
		jpBottom = new JPanel();
		jtfIP = new JTextField();
		jtfPort = new JTextField();
		jtfChat = new JTextField();
		jbConnect = new JButton("접속");
		jbSend = new JButton("보내기");
		jtaLog = new JTextArea();
		jspLog = new JScrollPane(jtaLog);
	}
	
	// 변수 초기화
	private void addComps() {
		// Container
		ct.setLayout(new BorderLayout(5, 5));
		ct.add(jpTop, BorderLayout.NORTH);
		ct.add(jpCenter, BorderLayout.CENTER);
		ct.add(jpBottom, BorderLayout.SOUTH);
		
		// jpTop
		jpTop.setLayout(new GridLayout(2, 3, 5, 5));
		jpTop.add(new JLabel("Server IP", JLabel.CENTER));
		jpTop.add(new JLabel("Serever Port", JLabel.CENTER));
		jpTop.add(new JLabel(""));
		jpTop.add(jtfIP);
		jpTop.add(jtfPort);
		jpTop.add(jbConnect);
		
		// jpCenter
		jpCenter.setLayout(new BorderLayout(5, 5));
		jpCenter.add(jspLog, BorderLayout.CENTER);
		
		// jpBottom
		jpBottom.setLayout(new BorderLayout(5, 5));
		jpBottom.add(new JLabel("채팅", JLabel.CENTER), BorderLayout.WEST);
		jpBottom.add(jtfChat, BorderLayout.CENTER);
		jpBottom.add(jbSend, BorderLayout.EAST);
		jbSend.setEnabled(false);
	}
	
	// 이벤트 처리
	private void addActionListener() {
		// 접속(jbConnect)
		jbConnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				client = new Client(jtfIP.getText(), Integer.parseInt(jtfPort.getText()), jtaLog);
				jbSend.setEnabled(client.getConnect());
			}
			
		});
		
		// 보내기(jbSend)
		jbSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				client.sendMsg(jtfChat.getText());
				jtfChat.setText("");
			}
			
		});
	}
	
	// 화면 출력
	private void showWnd(String title, int width, int height) {
		setTitle(title);
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
