package model;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Interface for a resourceProvider, returning a parsed SchedulePackage
 */
public interface ResourceProvider {

    SchedulePackage PullNewDataPackage()
            throws ParserConfigurationException, IOException, SAXException;

}
