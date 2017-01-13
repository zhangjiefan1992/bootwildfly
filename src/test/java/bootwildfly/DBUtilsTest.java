package bootwildfly;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class DBUtilsTest {

	@Test
	public void test() {
		Connection conn = null;
		Statement stmt = null ;
		ResultSet res = null;
		String name ="";
		String password ="";
		try {
			conn = DBUtils.getConnection();
			stmt = conn.createStatement();
			res = stmt.executeQuery("select * from user");
			while(res.next()){
				name = res.getString("name");
				password = res.getString("password");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Hello, SpringBoot on Wildfly  user name :"+name+" password : "+password);
	}

}
