import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

public class dbConnect {

	/**
	 * Attempts to connect to the database using an encrypted configuration file
	 * to obtain connection details. Also handles decryption and use of
	 * encrypted configuration fields.
	 * 
	 * @return the connection object, null if connection is unsuccessful
	 * @author Ishan Saraf
	 */
	public Connection connect() {
		Connection conn = null;
		try {
			// Creating the encryptor, and setting key used for decryption
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("jasyptEncrypt");

			// Loading properties file used for connection
			Properties p = new EncryptableProperties(encryptor);
			p.load(new FileInputStream("config.properties"));

			// Getting fields from properties file
			String serverURL = p.getProperty("server");
			String username = p.getProperty("username");
			String pwd = p.getProperty("password");
			String connectionURL = serverURL + ";user=" + username + ";password=" + pwd + ";databaseName=GMM";

			conn = DriverManager.getConnection(connectionURL);
			
			// Blanking out password string
			pwd = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}