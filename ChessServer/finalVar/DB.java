package finalVar;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A class Database that holds API to access the database. The database contains
 * a list of users and passwords along with their statistics of played games.
 * It's a static class that holds functions that import and export information
 * in database files.
 *
 * @author Ariel Mazar (mazarariel@gmail.com)
 */
public class DB {

    /**
     * Configures if there is a connection to the database
     *
     * @param dbPath A complete path to the database which created a link.
     * @return the connection with the database if there is no connection then
     * return null.
     */
    public static Connection getConnection(String dbPath) {
        String dbUserName = "";
        String dbPassword = "";
        String dbFilePath = "";
        Connection dbCon = null;

        try {
            // for run in developing mode 
            // -------------------------------------
            Path path = Paths.get(DB.class.getResource(dbPath).toURI());
            dbFilePath = path.toString().replace("build\\classes", "src");

            // for run in stand alone jar, remove the comment from the nex line
            // ----------------------------------------------------------------
//            dbFilePath = new File("mydb.accdb").getPath();
            String dbURL = "jdbc:ucanaccess://" + dbFilePath;
            dbCon = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error DB Connection!", JOptionPane.ERROR_MESSAGE);
        } catch (URISyntaxException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dbCon;
    }

    /**
     * Runs the SQL code in the database to extract the specific data which is
     * necessary for the game.
     *
     * @param sqlQuery the line of code that is being run in the database
     * access.
     * @return the specific data that was extracted or requested from the SQL
     * line code.
     * @throws SQLException
     */
    public static ResultSet runSqlQuery(String sqlQuery) throws SQLException {
        Connection con = getConnection("/assets/mydb.accdb");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sqlQuery);
        st.close();
        con.close();
        return rs;
    }

    /**
     * Runs SQL code in the database access to update the database as we enter
     * the line of code.
     *
     * @param sqlQuery the line of code that is being run in the database
     * access.
     * @throws SQLException
     */
    public static void updateSqlQuery(String sqlQuery) throws SQLException {
        Connection con = getConnection("/assets/mydb.accdb");
        Statement st = con.createStatement();
        st.executeUpdate(sqlQuery);

        st.close();
        con.close();
    }

    /**
     * Checks if the user exists in the database, this is a part of the login
     * process and used to check if users exist or signed in with the game.
     *
     * @param un the username of the player inserted.
     * @param pw the password of the player inserted.
     * @return true if user exists inside the database, false if the user
     * doesn't exist in the database.
     */
    public static Boolean isUserExists(String un, String pw) {
        Boolean result = null;
        try {
            String query = "SELECT * FROM users WHERE un='" + un + "' AND pw='" + pw + "'";
            //System.out.println("query="+query);
            ResultSet rs = DB.runSqlQuery(query);
            result = rs.next();
        } catch (SQLException ex) {
        }

        return result;
    }

    /**
     * Inserts a newly finished game with a victorious player between the match
     * of players into the database statistics.
     *
     * @param wPlayer The white player during the match.
     * @param bPlayer The black player during the match.
     * @param winner The winner between the players that played the game.
     */
    public static void insertGameToDB(Player wPlayer, Player bPlayer, Player winner) {
        LocalDateTime rn = LocalDateTime.now();
        if (wPlayer.getType().equals(Const.AI) || wPlayer.getType().equals(Const.GUEST)
                && bPlayer.getType().equals(Const.AI) || bPlayer.getType().equals(Const.GUEST)) {
//            System.out.println("it worked");
            return;
        }

        try {
            //The runned SQL code in the database access
            String query = "INSERT INTO Statistics (game_date, white, black, winner)"
                    + " VALUES ('" + rn + "', '" + wPlayer.getNameID() + "', '" + bPlayer.getNameID() + "', '" + winner.getNameID() + "');";
            updateSqlQuery(query);

        } catch (SQLException ex) {
            System.out.println("I got this");
        }
    }

    /**
     * Extracts the table of player game history to be shown in the statistics.
     *
     * @param player Requested player of the game history
     * @return A string that forms a template of a table which shows the history
     * games of the requested player.
     */
    public static String getDataGamesFromPlayer(String player) {
        String stats = ""; //All the statistics 
        int count = 1; // this variable is to count how many games exist and give them a number
        try {

            //The SQL code that is runned in the database access.
            String query = "SELECT * FROM Statistics WHERE white='" + player + "' OR black='" + player + "';";
            ResultSet rs = DB.runSqlQuery(query);

            while (rs.next()) {
                stats += count++ + ":\t    ";
                if (rs.getString("white").equals(player)) {
                    stats += rs.getString("white") + "\t   ";
                    if (rs.getString("winner").equals(player)) {
                        stats += "1 - 0\t   " + rs.getString("black") + "\t   ";
                    } else {
                        stats += "0 - 1\t   " + rs.getString("black") + "\t   ";
                    }
                }
                if (rs.getString("black").equals(player)) {
                    stats += rs.getString("white") + "\t   ";
                    if (rs.getString("winner").equals(player)) {
                        stats += "0 - 1\t   " + rs.getString("black") + "\t   ";
                    } else {
                        stats += "1 - 0\t   " + rs.getString("black") + "\t   ";
                    }
                }
                stats += rs.getString("game_date") + "\n";
            }
        } catch (SQLException e) {
        }
        return stats;
    }
}
