import java.sql.Connection;
import java.sql.DriverManager;

public class dbConnect {

	public Connection connect() {
		Connection conn = null;
		try {
			String serverURL = "jdbc:sqlserver://golem.csse.rose-hulman.edu";
			// TODO: Figure out how to encrypt config file to ensure user+pwd
			// data isn't visible in plaintext
			String username = "gmm";
			String pwd = "abc123";
			String connectionURL = serverURL + ";user=" + username + ";password=" + pwd + ";databaseName=GMM";

			conn = DriverManager.getConnection(connectionURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}