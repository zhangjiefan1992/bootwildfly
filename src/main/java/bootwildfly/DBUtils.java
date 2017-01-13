package bootwildfly;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtils {
	private static String url;
	private static String user;
	private static String pwd;

	static {
		Properties pro = PropertiesUtils.getProperties("database.properties");
		url = PropertiesUtils.getProperties("database.url", pro);
		user = PropertiesUtils.getProperties("database.user", pro);
		pwd = PropertiesUtils.getProperties("database.password", pro);
		try {
			Class.forName(PropertiesUtils.getProperties("database.driver", pro));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url, user, pwd);
		conn.setAutoCommit(false);
		return conn;
	}
	public static void release(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
