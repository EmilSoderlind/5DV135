package Utils.Database;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * An interface for setting and getting data from a database.
 */
public interface DatabaseInterface {

    /**
     * Method to add scores to a DT
     *
     * @param newScore name + score to be added in DB table
     * @throws SQLException If the communication to the database failed
     */
    void setHighScoreEntry(HighScoreEntryStruct newScore) throws SQLException;

    /**
     * Requesting high score list from a DB table
     * @param showNegativeScore Flag for returning negative scores or not
     * @return high score list
     * @throws SQLException If the communication to the database failed
     */
    ArrayList<HighScoreEntryStruct> getHighScoreList(boolean showNegativeScore)
            throws SQLException;
}
