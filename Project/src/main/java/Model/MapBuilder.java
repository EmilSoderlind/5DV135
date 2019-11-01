package Model;

import Model.Units.Tile;
import Model.Units.TileInvokeException;
import Utils.TILESTATUS;
import Utils.ValidationErrorHandler;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Mapbuilder is responsible for building maps from an XML-file.
 */
public class MapBuilder {
    private ArrayList<Exception> exceptions = new ArrayList<>();
    private int width;

    /**
     * Loads a level XML-file from a path and validates it. If valid, return
     * an array with Map-files.
     *
     * @param fileName name of with path
     * @param width    of map
     * @return Array with map objects from file
     * @throws SAXException If validation fails
     */
    public Map[] fromFile(String fileName, int width) throws SAXException {
        try {
            this.width = width;
            String SCHEMA_DIR = "levels/level_schema.xsd";
            InputStream schemaInputStream = getFileInputStream(SCHEMA_DIR);
            InputStream mapInputStream = getFileInputStream(fileName);
            Source xmlMapStream = new StreamSource(mapInputStream);
            // define the type of schema - we use W3C:
            String schemaLang = "http://www.w3.org/2001/XMLSchema";
            // get validation driver:
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
            // create schema by reading it from an XSD file:
            Schema schema = factory.newSchema(
                    new StreamSource(schemaInputStream));

            Validator validator = schema.newValidator();

            // Error handler that appends any exceptions to the exception list
            ValidationErrorHandler errorHandler = new ValidationErrorHandler(
                    exceptions::add
            );
            // Errors will be reported in errorHandler first.
            validator.setErrorHandler(errorHandler);
            validator.validate(xmlMapStream);

            if (errorHandler.isValid()) {
                // Parse xml
                return getMapsFromFile(fileName);
            }
        } catch (IOException e) {
            exceptions.add(new FileNotFoundException(
                    String.format("Could not locate the file [%s]",
                            fileName)));
        } catch (ParserConfigurationException e) {
            exceptions.add(e);
        }
        return null;
    }

    private boolean isResourcePath(String path) {
        final String patternTest = "levels/test/([a-zA-Z0-9_\\s-]+).xml$";
        final String patternRes = "levels/([a-zA-Z0-9_\\s-]+).xml$";
        final String patternSchema = "levels/([a-zA-Z0-9_\\s-]+).xsd";

        return path.matches(patternRes) || path.matches(patternTest)
                || path.matches(patternSchema);
    }

    /**
     * @return Any exceptions that were thrown during the build
     */
    public ArrayList<Exception> getExceptions() {
        return exceptions;
    }

    private Map[] getMapsFromFile(String filename)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(getFileInputStream(filename));
        NodeList mapNodes = doc.getDocumentElement()
                .getElementsByTagName("map");

        return IntStream.range(0, mapNodes.getLength())
                .mapToObj(mapNodes::item)
                .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                .map(n -> fromMapNode(n, exceptions::add))
                .filter(m -> m != null)
                .filter(m -> m.validateMap(exceptions::add))
                .peek(m -> new PathSetter(
                        new ManyToOnePathSetterStrategy()).setPaths(m))
                .peek(m -> new TileRotator(m).setRotationTiles())
                .toArray(Map[]::new);
    }

    private Map fromMapNode(Node n, Consumer<Exception> e) {
        HashMap<Integer, Tile> hashMap = new HashMap<>();
        ArrayList<Integer> tileIdentifiers = new ArrayList<>();

        NamedNodeMap mapAttributes = n.getAttributes();

        int size = Integer.parseInt(mapAttributes.getNamedItem("row")
                .getNodeValue());
        String mapName = mapAttributes.getNamedItem("name").getNodeValue();
        int tileSize = Integer.parseInt(mapAttributes.getNamedItem("tileSize")
                .getNodeValue());

        if (Math.floorMod(width, tileSize) != 0) {
            String message = "The tile size has to be dividable with dim %d.";
            e.accept(new IllegalStateException(String.format(message, width)));
            return null;
        }

        Map map = new Map(size, size, mapName);

        NodeList nodeList = n.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("tileset")) {
                    extractTileSetFromNode(node, tileIdentifiers, hashMap);
                } else if (node.getNodeName().equals("data")) {
                    insertTilesToMap(hashMap, size, tileSize, map, node);
                }
            }
        }
        return map;
    }

    private void insertTilesToMap(HashMap<Integer, Tile> hashMap, int size,
                                  int tileSize, Map map, Node node) {
        String csv = node.getTextContent();

        int[] idGridWithTiles = parseToIntArray(csv);

        for (int index = 0; index < idGridWithTiles.length; index++) {
            int row = index / size;
            int col = Math.floorMod(index, size);
            int tileIndex = idGridWithTiles[index];
            if (hashMap.get(tileIndex) != null) {
                Tile tile = hashMap.get(tileIndex).copy();
                tile.setFrame(new Rectangle(col, row, tileSize, tileSize));
                map.setTile(tile, exceptions::add);
            }
        }
    }

    private void extractTileSetFromNode(Node node,
                                        ArrayList<Integer> tileIdentifiers,
                                        HashMap<Integer, Tile> hashMap) {
        NamedNodeMap attributes = node.getAttributes();

        int tileId = Integer.parseInt(attributes.getNamedItem("id")
                .getNodeValue());
        String className = attributes.getNamedItem("class")
                .getNodeValue();
        String status = attributes.getNamedItem("status")
                .getNodeValue();

        tileIdentifiers.add(tileId);
        Tile tile = invokeTile(className, new Rectangle(0, 0),
                TILESTATUS.valueOf(status));
        hashMap.put(tileId, tile);
    }

    private int[] parseToIntArray(String csvData) {
        String[] array = csvData.split(",");
        int[] data = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            data[i] = Integer.parseInt(array[i].trim());
        }
        return data;
    }

    private Tile invokeTile(String name, Rectangle frame, TILESTATUS status) {
        try {
            Class<?> tileClass = Class.forName(name);
            if (Tile.class.isAssignableFrom(tileClass)) {
                try {
                    Constructor<?> con = tileClass
                            .getConstructor(Rectangle.class, TILESTATUS.class);
                    return (Tile) con.newInstance(frame, status);

                } catch (NoSuchMethodException | IllegalAccessException e) {
                    exceptions.add(new TileInvokeException(
                            String.format("Tile with class name [ %s ] had " +
                                    "no valid constructor.", name), e));
                } catch (InstantiationException e) {
                    exceptions.add(new TileInvokeException(
                            String.format("[ %s ] is not a valid class type.",
                                    name), e));
                } catch (InvocationTargetException e) {
                    exceptions.add(new TileInvokeException(
                            String.format("[ %s ] threw an exception when " +
                                    "invoked.", name), e.getCause()));
                }
            } else { /* If the tile don't extend from 'Tile' */
                exceptions.add(new TileInvokeException(
                        String.format("[ %s ] needs to extend 'Tile'.", name)));
            }
        } catch (ClassNotFoundException e) {
            exceptions.add(new TileInvokeException(
                    String.format("[ %s ] was not found.", name), e));
        }
        return null;
    }

    /**
     * Grabs a file from either the resources directory or from full path
     *
     * @param fileName
     * @return InputStream of file
     * @throws IOException if the given file doesn't exist
     */
    private InputStream getFileInputStream(String fileName) throws IOException {
        InputStream stream;
        if (isResourcePath(fileName)) {
            // Load file from resources
            ClassLoader classLoader = MapBuilder.class.getClassLoader();
            stream = classLoader.getResourceAsStream(fileName);
        } else {
            stream = new FileInputStream(fileName);
        }

        if (stream == null) {
            throw new IOException("Cannot find file " + fileName);
        }
        return stream;
    }
}

