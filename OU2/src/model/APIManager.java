package model;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Responsible for parsing from Sveriges Radio API
 * Providing SchedulePackage with all avaliable channels
 */
public class APIManager implements ResourceProvider{

    // FULL PARSE
    // 1.   Get available channel ID:s, names and logotypes
    // 2.   For each channel --> Creating ChannelSchedule

    // 2.1. Parse for yesterday, today + tomorrow
    // 2.1.1. For each page in pagination
    // 2.1.1.1. For each field in scheduledepisode --> Create Program:s

    // 3. Creating SchedulePackage with ChannelSchedule:s with Program:s

    /**
     * Perform a parse of all channels in SR-API
     * @return SchedulePackage with all channels and their programs
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    @Override
    public SchedulePackage PullNewDataPackage() throws
            ParserConfigurationException, IOException, SAXException {

        SchedulePackage channelList = new SchedulePackage();

        Node channelsNode = getChannelsNode();

        // Parsing channel names + IDs
        for(int i = 0; i<channelsNode.getChildNodes().getLength();i++){

            Node currNode = channelsNode.getChildNodes().item(i);
            if(currNode.getChildNodes().getLength() == 0){
                continue;
            }

            // 1  -  ID + Name
            int id = Integer.parseInt(currNode.getAttributes().
                    getNamedItem("id").getNodeValue());
            String name = currNode.getAttributes().
                    getNamedItem("name").getNodeValue();

            // 1  -  PARSE IMG
            Node channelChildren = currNode.getFirstChild().getNextSibling();
            String imageURL = channelChildren.getFirstChild().getNodeValue();

            // 2  -  Parse each channel
            ChannelSchedule currChannel = parseChannel(id, name);
            currChannel.setImage(parseImageUrl(imageURL));
            channelList.appendChannel(currChannel);

        }
        channelList.setRecievedDate(new Date());
        return channelList;
    }

    private Image parseImageUrl(String urlStr){

        Image image = null;
        try {
            URL url = new URL(urlStr);
            image = ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("Could not parse image for channel");
        }

        return image;

    }

    // Parse each channels schedule
    private ChannelSchedule parseChannel(int id, String name) throws
            ParserConfigurationException, IOException, SAXException {
        ChannelSchedule currChannel = new ChannelSchedule(id,name);

        int numberOfPages = -1;

        // 2.1  -  For each day of: Yesterday, today and tomorrow
        for(int relativeDay = -1; relativeDay < 2; relativeDay++) {

            String currDateUrl =
                    "http://api.sr.se/v2/scheduledepisodes?channelid=" + id +
                            "&date=" + getDateStrForURL(relativeDay);
            Document firstDoc = getDocumentElement(currDateUrl);
            NodeList firstNodeL = firstDoc.getDocumentElement().getChildNodes();
            Node paginationNode = firstNodeL.item(3);
            Node totalPagesNode = paginationNode.getChildNodes().item(7);

            numberOfPages = Integer.parseInt(totalPagesNode.getChildNodes().
                    item(0).getNodeValue());

            // 2.1.1  -  For each page of channels schedule
            for (int page = 1; page < numberOfPages + 1; page++) {


                String currPageUrl =
                        "http://api.sr.se/v2/scheduledepisodes?channelid=" + id
                                + "&date=" + getDateStrForURL(relativeDay) +
                                "&page=" + page;

                Document doc = getDocumentElement(currPageUrl);
                NodeList nodeL = doc.getDocumentElement().getChildNodes();


                Node scheduleNode = nodeL.item(5);

                // 2.1.1.1  -  For each scheduledepisode
                int numberOfChildren = scheduleNode.getChildNodes().getLength();
                for (int t = 1; t < numberOfChildren - 1; t++) {

                    // Removing end tags
                    if (t % 2 == 0) {
                        continue;
                    }
                    Node currProgramNode = scheduleNode.getChildNodes().item(t);

                    Program currProgram = parseProgram(currProgramNode);

                    currChannel.append(currProgram);

                }
            }
        }

        return currChannel;
    }

    // Parse and creates a Program instance of Scheduleepisode-fields
    private Program parseProgram(Node currProgramNode) {
        Program currProgram = new Program();

        // For each field in scheduledepisode
        int numberOfChildren = currProgramNode.getChildNodes().getLength();
        for (int index = 1; index < numberOfChildren - 1; index++) {

            // Removing end tags
            if (index % 2 == 0) {
                continue;
            }

            Node j = currProgramNode.getChildNodes().item(index);

            switch (j.getNodeName()) {
                case "episodeid":
                    break;
                case "title":
                    currProgram.setTitle(j.getFirstChild().getNodeValue());
                    break;
                case "description":

                    if (j.getFirstChild() == null) {
                        break;
                    }

                    currProgram.setDescription(j.getFirstChild().getNodeValue());

                    break;
                case "starttimeutc":
                    currProgram.setStartTime(j.getFirstChild().getNodeValue());

                    if (currProgram.getEndTime().before(new Date())) {
                        currProgram.setBroadcasted(true);
                    } else {
                        currProgram.setBroadcasted(false);
                    }

                    break;
                case "endtimeutc":
                    currProgram.setEndTime(j.getFirstChild().getNodeValue());
                    break;
                default:
                    continue;
            }

        }
        return currProgram;
    }

    private static Document getDocumentElement(String currChannelTableURL)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(false);
        f.setValidating(false);
        DocumentBuilder b = f.newDocumentBuilder();
        URLConnection connection = new URL(currChannelTableURL).openConnection();
        connection.addRequestProperty("Accept", "application/xml");
        Document doc = b.parse(connection.getInputStream());
        doc.getDocumentElement().normalize();
        return doc;
    }

    // Get channel node from API with children
    private Node getChannelsNode()
            throws ParserConfigurationException, IOException, SAXException {
        String url = "http://api.sr.se/api/v2/channels/";
        String urlNoPag = "http://api.sr.se/api/v2/channels/?pagination=false";

        Document doc = getDocumentElement(urlNoPag);

        Node srNode = doc.getChildNodes().item(0);

        NodeList srNodeChildList = srNode.getChildNodes();

        return srNodeChildList.item(3);
    }

    private String getDateStrForURL(int relativeDay) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, relativeDay);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date relativeDate = cal.getTime();

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String outStr = ft.format(relativeDate);

        return outStr;
    }

}
