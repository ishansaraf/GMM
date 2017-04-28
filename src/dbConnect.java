import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class dbConnect {

	@SuppressWarnings("resource")
	public Connection connect() {
		try {
			String serverURL = "jdbc:sqlserver://golem.csse.rose-hulman.edu";
			// TODO: Figure out how to encrypt config file to ensure user+pwd
			// data isn't visible in plaintext
			String username = "gmm";
			String pwd = "333project";
			String connectionURL = serverURL + ";user=" + username + ";password=" + pwd;

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			Connection conn = DriverManager.getConnection(connectionURL);

			Statement test = conn.createStatement();
			String testQuery = "SELECT * FROM Item";
			ResultSet rs = test.executeQuery(testQuery);
			while (rs.next()) {
				System.out.println(rs.getString("Name"));
				System.out.println(rs.getString("Description"));
				System.out.println(rs.getString("BaseValue"));
			}
			
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			return null;
		}
	}
}