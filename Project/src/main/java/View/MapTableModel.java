package View;

import Model.Map;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Optional;

/**
 * MapTableModel are responsible storing the names and images of maps
 */
public class MapTableModel extends AbstractTableModel {
    private ArrayList<Map> maps;
    private String[] columnNames;

    public MapTableModel(){
        maps = new ArrayList<>();
        columnNames = new String[]{
                "Image",
                "Description"
        };
    }

    public Optional<Map> getMap(int row){
        return Optional.of(maps.get(row));
    }


    /**
     * Add maps to MapTableModel object
     * @param maps maps to be added
     */
    public void addMaps(Map[] maps){
        for (Map map : maps) {
            this.maps.add(map);
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return maps.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Map map = maps.get(rowIndex);
        return columnIndex == 0 ? map.generateImage() : map.getName();
    }
}
