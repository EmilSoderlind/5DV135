package model;


import java.util.ArrayList;
import java.util.Date;

/**
 * Contains all available channels full schedule
 */
public class SchedulePackage {

    private Date recieved;
    private ArrayList<ChannelSchedule> channels = new ArrayList<>();

    /**
     * Returns number of channels
     * @return number of channels
     */
    public int numberOfChannels(){
        return channels.size();
    }

    /**
     * Return parse date of object
     * @return parse date
     */
    public Date getRecievedDate() {
        return recieved;
    }

    /**
     * Set Date when object was parsed
     * @param recieved parse date
     */
    public void setRecievedDate(Date recieved) {
        this.recieved = recieved;
    }

    /**
     * Appending ChannelSchedule to channels-list
     * @param ct ChannelSchedule to append
     */
    public void appendChannel(ChannelSchedule ct){
        channels.add(ct);
    }

    /**
     * Returns ChannelSchedule at index in channels-list
     * @param index index to return
     * @return ChannelSchedule at index
     */
    public ChannelSchedule channelAt(int index){
        if(index == -1){
            return null;
        }else {
            return channels.get(index);
        }
    }

    /**
     * Return string representation of object
     * @return string representation
     */
    @Override
    public String toString() {

        String outStr = "";

        for (ChannelSchedule cs : channels) {
            outStr += "\n " + cs;
        }

        return "-- SchedulePackage --\n" +
                "Retrieved " + recieved +"\n" +
                "--------------------\n" +
                outStr + "\n" +
                "--------------------\n";

    }
}
