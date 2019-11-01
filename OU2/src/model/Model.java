package model;

import model.Observer_Subject.Observer;
import model.Observer_Subject.Subject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Subject responsible for business logic of program
 *
 * Provide observers (view) with choosed channel in parsed data from APIManager
 * Contains latest parsed data
 * Requesting new data parse from APIManager every 12 hours and at user request
 */
public class Model implements Subject {

    private SchedulePackage latestParse;
    private ArrayList<Observer> observers = new ArrayList<>();
    private volatile boolean newParseRequested = false;

    private int selectedChannelID = -1;

    /**
     * Running Loop:
     * Perform initial parse and then:
     * Perform new parse every 12h or when user requests (newParseRequested)
     */
    public void runningLoop(){

        // Initial parse
        parseSchedule();
        notifyObs();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // checka av om: en timme gått ELLER parse är requested
                if(newParseRequested || timeToUpdate()){
                    parseSchedule();
                    notifyObs();
                }

            }
        }, 120, 120);

    }

    /**
     * Request a selected channel-ID to display
     * @param selectedChannelID
     */
    public void setSelectedChannelID(int selectedChannelID) {
        this.selectedChannelID = selectedChannelID;

        // Update with new channel from latest parse
        notifyObs();
    }

    /**
     * Called by controller when user have requested a new parse
     * Set newParseRequested boolean to true
     */
    public void requestNewParse(){
        newParseRequested = true;
    }

    /**
     * Perform a new parse
     */
    private void parseSchedule(){

        APIManager am = new APIManager();

        try {

            latestParse = am.PullNewDataPackage();

        } catch (ParserConfigurationException e) {
            latestParse = new SchedulePackage();

        } catch (IOException e) {
            latestParse = new SchedulePackage();

        } catch (SAXException e) {
            latestParse = new SchedulePackage();
        }

        newParseRequested = false;
    }

    /**
     * Return true last parse was more than 12 hours ago
     * @return older last parse than 12 hours
     */
    private boolean timeToUpdate(){

        if(latestParse.numberOfChannels() == 0){
            return false;
        }

        Date currTime = new Date();
        long currTimeS = currTime.getTime();

        long lastParseS = latestParse.getRecievedDate().getTime();

        long diffS = currTimeS-lastParseS;

        return Math.abs(diffS) > 60*60*1000;
    }

    /**
     * Return program at index in schedule list
     * @param rowIndex index to get
     * @return program at index
     */
    public Program programAt(int rowIndex){

        int channelIndex = -1;
        int numberOfChannels = latestParse.numberOfChannels();

        for(int chan = 0; chan< numberOfChannels; chan++){
            if(latestParse.channelAt(chan).channelID == selectedChannelID){
                channelIndex = chan;
            }
        }

        ChannelSchedule currentChannel = latestParse.channelAt(channelIndex);

        return currentChannel.getProgramAtIndex(rowIndex);
    }


    /**
     * Add observer to notify
     * @param newObs new observer
     */
    @Override
    public void addObserver(Observer newObs) {
        this.observers.add(newObs);
    }

    /**
     * Delete observer
     * @param observer observer to delete
     */
    @Override
    public void deleteObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify observers with latest parse data and requested channel
     */
    @Override
    public synchronized void notifyObs() {

        ChannelSchedule selectedChannel = null;

        ArrayList<String> channelNames = new ArrayList<>();
        ArrayList<Integer> channelIds = new ArrayList<>();

        // get selected channels schedule
        int numberOfChannels = this.latestParse.numberOfChannels();

        for(int index = 0; index < numberOfChannels; index++){

            ChannelSchedule tempChannel = latestParse.channelAt(index);

            // Set selectedChannel
            if(tempChannel.channelID == selectedChannelID){
                selectedChannel = tempChannel;
            }

            // Set available channel ids + names
            channelNames.add(tempChannel.getChannelName());
            channelIds.add(tempChannel.channelID);
        }

        // If no channel is selected -> choose first in list
        if(selectedChannel == null){
            if(latestParse.numberOfChannels() > 0) {
                selectedChannel = latestParse.channelAt(0);
                selectedChannelID = selectedChannel.channelID;
            }
        }

        for(Observer observer : observers){
            observer.update(selectedChannel, channelIds, channelNames);
        }

    }
}
