package tables;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.HashMap;

public class TableForNames extends AbstractTableModel {
    private HashMap<String, Integer> names;
    private String[] namess;
    private String[] columnNames = {"Jméno", "Počet"};

    public TableForNames(HashMap<String, Integer> names) {
        this.names = names;
        namess = new String[names.size()];
        int count = 0;
        for (String i : this.names.keySet()) {
            namess[count] = i;
            count++;
        }
        Arrays.sort(namess);
    }

    @Override
    public int getRowCount() {
        return names.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    //vrátí jména x počet nalezení
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            String name = namess[rowIndex];
            return name.substring(0,1).toUpperCase() + name.substring(1);
        }
        else {
            return names.get(namess[rowIndex]);
        }
    }
}
