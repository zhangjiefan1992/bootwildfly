package bootwildfly;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWildFlyController {

	@RequestMapping("hello")
	public String sayHello() {
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
		}finally{
			DBUtils.release(res, stmt, conn);
		}

		return ("Hello, SpringBoot on Wildfly  user name :"+name+" password : "+password);
	}
}