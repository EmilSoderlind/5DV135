package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Contains a channels; scheduled programs, ID and logo-image.
 */
public class ChannelSchedule {

    int channelID;
    private String channel;
    private ArrayList<Program> programs;

    Image image;

    public ChannelSchedule(int channelID, String channel) {

        this.channelID = channelID;
        this.channel = channel;


        programs = new ArrayList<>();
    }

    /**
     * Getter for channels logo image
     * @return logo image
     */
    public Image getImage() {
        return image;
    }


    /**
     * Set channels logo image
     * @param image logo image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Return string representation of channel
     * @return string representation
     */
    @Override
    public String toString() {

        String outStr = "";

        for (Program past : programs) {
            outStr += "\n " + past;
        }

        return "Channel='" + channel + "{\n" +
                outStr +
                "\n";
    }

    /**
     * Append program to channel
     * - Save program if no older than 12h and no newer than 12h.
     *
     * @param program program to append
     */
    public void append(Program program){

        Date nowDate = new Date();
        long programEpoch = program.getEndTime().getTime();
        long currEpoch = nowDate.getTime();

        // Past +/-12h
        if(Math.abs(currEpoch-programEpoch) > 12*60*60*1000){
            return;
        }

        if(program.getEndTime().before(new Date())){
            program.setBroadcasted(true);
        }else{
            program.setBroadcasted(false);
        }

        this.programs.add(program);

    }

    /**
     * Get number of programs in channels program-schedule
     * @return number of programs
     */
    public int numberOfprograms(){
        return programs.size();
    }


    /**
     * Getter for program at index
     * @param index index in schedule-list
     * @return program at index
     */
    public Program getProgramAtIndex(int index){

        if(index == -1){
            return null;
        }else{
            return programs.get(index);
        }

    }


    /**
     * Getter for channels name
     * @return channels name
     */
    public String getChannelName() {
        return channel;
    }
}
