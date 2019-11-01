package model.Observer_Subject;

import model.ChannelSchedule;

import java.util.ArrayList;

/**
 * Observer in observer-subject design pattern
 */
public interface Observer {

    void update(ChannelSchedule latestSelectedChannelSchedule,
                ArrayList<Integer> availableChannelIDs,
                ArrayList<String> availableChannelNames);

}