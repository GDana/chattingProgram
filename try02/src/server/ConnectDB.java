package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectDB {

	private Connection con;
	private String url = "jdbc:oracle:thin:@localhost:1521:orcl";
	private String user = "chatting";
	private String password = "chatting";
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private PreparedStatement pstmtInsert;
	
	public ConnectDB() {
		// 1. 드라이버 로딩
		try {
			Class.forName(driver);
		}catch(ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		}
		
		// 2. DB 연결
		try {
			con = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("DB 연결 실패");
		}
		
		// 3. Statement 생성
		String sqlInsert = "insert into try02(ip, chat) values(?, ?)";
		
		try {
			pstmtInsert = con.prepareStatement(sqlInsert);
		} catch (SQLException e) {
			System.out.println("Statement 생성 실패");
		}
	}
	
	public void insert(String ip, String chat) {
		// 4. Statement 실행
		try {
			pstmtInsert.setString(1, ip);
			pstmtInsert.setString(2,  chat);
			
			int cnt = pstmtInsert.executeUpdate();
			if(cnt > 0) {
				con.commit();
			}else {
				System.out.println("DB 입력 실패");
			}
		} catch (SQLException e) {
			System.out.println("DB 입력 실패");
		}
	}
}
