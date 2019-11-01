package Utils.Database;

import java.sql.*;
import java.util.*;


/**
 * Database manager responsible for high score database communication
 */
@SuppressWarnings("FieldCanBeLocal")
public class DatabaseManager implements DatabaseInterface {

    private Connection conn;

    private String databaseURL = "jdbc:postgresql://130.239.40.69:5432/c5dv135_ht18_g8";
    private String databaseUser = "c5dv135_ht18_g8";
    private String databasePwd = "UazAaMNTb9yt";


    /**
     * DB Test method
     * @param args Input arguments
     * @throws SQLException If the communication to the database failed
     */
    public static void main(String[] args) throws SQLException {

        DatabaseManager dm = new DatabaseManager();

        boolean showNegativeScores = true;

        ArrayList<HighScoreEntryStruct> highScoreList = dm.getHighScoreList(showNegativeScores);

        System.out.println(highScoreList);

        HighScoreEntryStruct n = new HighScoreEntryStruct();
        n.setName("Joshiie");
        n.setScore(-1337);

        dm.setHighScoreEntry(n);

    }

    /**
     * Establish connection to database and returns high score list
     * @param showNegativeScore Flag for returning negative scores or not
     * @return high score list
     * @throws SQLException If the communication to the database failed
     */
    // Used to retrieve sorted highScore list as HighScoreEntryStruct:s
    // showNegativeScore if you want negative scores
    public ArrayList<HighScoreEntryStruct> getHighScoreList(boolean showNegativeScore) throws SQLException {
        String getQuery = "SELECT * FROM \"TBL_HighScore\"" + " ORDER BY \"Score\" DESC;";

        return performGetQuery(getQuery, showNegativeScore);
    }

    /**
     * Establish connection to database and add hoverX Name+Score
     * @param name Name to be added in DB post
     * @param score Score to be added in DB post
     * @throws SQLException If the communication to the database failed
     */
    public void setHighScoreEntry(String name, int score) throws SQLException {
        HighScoreEntryStruct entryStruct = new HighScoreEntryStruct();

        entryStruct.setName(name);
        entryStruct.setScore(score);

        setHighScoreEntry(entryStruct);
    }

    /**
     * Establish connection to database and add hoverX HighScoreEntryStruct
     * @param newScore name + score to be added in DB table
     * @throws SQLException If the communication to the database failed
     */
    public void setHighScoreEntry(HighScoreEntryStruct newScore) throws SQLException {
        String addQuery = "INSERT INTO \"TBL_HighScore\"(\"Name\", \"Score\") VALUES(?, ?)";

        performSetQuery(addQuery,newScore);

    }

    private void performSetQuery(String addQuery, HighScoreEntryStruct newEntry) throws SQLException {

        this.establishConnectionToDatabase();

        PreparedStatement pst = conn.prepareStatement(addQuery);

        pst.setString(1, newEntry.getName());
        pst.setInt(2, newEntry.getScore());

        pst.executeUpdate();
    }

    private ArrayList<HighScoreEntryStruct> performGetQuery(String query, boolean showNegativeScore) throws SQLException {

        this.establishConnectionToDatabase();

        ArrayList<HighScoreEntryStruct> highScoreList = new ArrayList<>();

        ResultSet rs = this.sendQueryToDB(query);

        while(rs.next()) {
            HighScoreEntryStruct highScoreEntry = new HighScoreEntryStruct();

            highScoreEntry.setName(rs.getString("Name"));
            highScoreEntry.setScore(Integer.parseInt(rs.getString("Score")));

            if (highScoreEntry.getScore() >= 0 || showNegativeScore) {
                highScoreList.add(highScoreEntry);
            }
        }

        return highScoreList;
    }

    private ResultSet sendQueryToDB(String query) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery(query);
    }

    private void establishConnectionToDatabase() throws SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("error class not found exception");
            e.printStackTrace();
        }

        conn = DriverManager.getConnection(databaseURL, databaseUser, databasePwd);
    }

}