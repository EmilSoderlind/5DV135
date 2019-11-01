package model;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Representing a certain program on a channel in the schedule
 * Have title, description, start/end-time know if if has been broadcated
 */
public class Program {

    private Boolean broadcasted = false;
    private String title = "<Titel saknas>";
    private String description = "<Beskrivning saknas>";

    private Date startTime = new Date();
    private Date endTime = new Date();

    /**
     * Set if program have been broadcasted
     * @param broadcasted broadcasted boolean
     */
    public void setBroadcasted(Boolean broadcasted) {
        this.broadcasted = broadcasted;
    }

    /**
     * Get boolean if program have been broadcasted
     * @return broadcasted or not
     */
    public Boolean getBroadcasted() {
        return broadcasted;
    }

    /**
     * Getter for programs title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title of program
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for programs description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description string of program
     * @param description description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for end time as Date
     * @return end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * Return presentable representation of start time
     * @return presentable representation
     */
    public String getPrettyStartTime(){

        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat ft = new SimpleDateFormat("kk:mm");
        String outStr = ft.format(this.startTime);

        return outStr;
    }

    /**
     * Return presentable representation of end time
     * @return presentable representation
     */
    public String getPrettyEndTime(){

        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat ft = new SimpleDateFormat("kk:mm");
        String outStr = ft.format(this.endTime);

        return outStr;
    }

    /**
     * Set programs start time
     * @param time start time
     */
    public void setStartTime(String time){

        SimpleDateFormat inputFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            this.startTime = inputFormat.parse(time);
        } catch (ParseException ignored) { }

    }

    /**
     * Set programs end time
     * @param time end time
     */
    public void setEndTime(String time){

        SimpleDateFormat inputFormat =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        try {
            this.endTime = inputFormat.parse(time);
        } catch (ParseException ignored) { }
    }

    /**
     * Returns string representation of Program
     * @return String representation
     */
    @Override
    public String toString() {
        return "Program{" +
                "broadcasted=" + broadcasted +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

