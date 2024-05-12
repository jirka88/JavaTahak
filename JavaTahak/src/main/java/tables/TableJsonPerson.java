package tables;

import jsonClasses.Person;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.List;

public class TableJsonPerson extends AbstractTableModel {
    private final List<Person> persons;
    private final Field[] columnNames = Person.class.getDeclaredFields();
    public TableJsonPerson(List<Person> persons) {
        this.persons = persons;
    }
    @Override
    public int getRowCount() {
            return persons.size() ;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex].getName();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return persons.get(rowIndex).getFirstName();
            case 1:
                return persons.get(rowIndex).getLastName();
            case 2:
                return persons.get(rowIndex).getCity();
            case 3:
                return persons.get(rowIndex).getCountry();
            case 4:
                return persons.get(rowIndex).getCountryCode();
            case 5:
                return persons.get(rowIndex).getSalary();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                persons.get(rowIndex).setFirstName(aValue.toString());
                break;
            case 1:
                persons.get(rowIndex).setLastName(aValue.toString());
                break;
            case 2:
                persons.get(rowIndex).setCity(aValue.toString());
                break;
            case 3:
                persons.get(rowIndex).setCountry(aValue.toString());
                break;
            case 4:
                persons.get(rowIndex).setCountryCode(aValue.toString());
                break;
            case 5:
                persons.get(rowIndex).setSalary(Integer.parseInt(aValue.toString()));
                break;
        }
    }
}
