package Utils.Database;

/**
 * Struct for highScore entry
 */
public class HighScoreEntryStruct {

    public String name;
    public int score;


    /**
     * Returns the name of the highScore entry
     * @return Name
     */
    public String getName() {
        return name;
    }


    /**
     * Set name of highScore entry
     * @param name Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the score of the highScore entry
     * @return Score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set score of highScore entry
     * @param score score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns string representation of highScore entry
     * @return String representation
     */
    @Override
    public String toString() {
        return "HighScoreEntryStruct{" +
                "name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}
