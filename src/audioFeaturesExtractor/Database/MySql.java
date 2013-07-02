package audioFeaturesExtractor.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySql {
	
	public static boolean connectToDatabase(){
			boolean isConnected = true;
		    Connection con = null;
	        Statement st = null;
	        ResultSet rs = null;

	        String url = "jdbc:mysql://localhost/FireFly3";
	        String user = "root";
	        String password = "";

	        try {
	            con = DriverManager.getConnection(url, user, password);
	            st = con.createStatement();
	            rs = st.executeQuery("SELECT VERSION()");

	            if (rs.next()) {
	                System.out.println(rs.getString(1));
	            }

	        } catch (SQLException ex) {
	            Logger lgr = Logger.getLogger(MySql.class.getName());
	            lgr.log(Level.SEVERE, ex.getMessage(), ex);
	            isConnected = false;

	        } finally {
	            try {
	                if (rs != null) {
	                    rs.close();
	                }
	                if (st != null) {
	                    st.close();
	                }
	                if (con != null) {
	                    con.close();
	                }

	            } catch (SQLException ex) {
	                Logger lgr = Logger.getLogger(MySql.class.getName());
	                lgr.log(Level.WARNING, ex.getMessage(), ex);
	            }
	        }
	        return isConnected;
	}

}
