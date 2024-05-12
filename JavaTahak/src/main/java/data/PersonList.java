package data;

import jsonClasses.Person;

import javax.swing.*;
import java.util.ArrayList;

public class PersonList {
    private ArrayList<Person> data = new ArrayList<Person>();
    private final String COUNT_RECORD_TEXT = "Počet záznamů činí: ";
    private final String SUM_RECORD_TEXT = "Celkem platy: ";
    private JLabel salarySum;
    private JLabel countRecords;
    public PersonList(JLabel salarySum, JLabel countRecords) {
        this.salarySum = salarySum;
        this.countRecords = countRecords;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }
    public void addPerson(Person person) {
        data.add(person);
    }
    public void removePerson(int index) {
        data.remove(index);
    }
    public void changeSum() {
        int sum = 0;
        for(Person person : data) {
            sum += person.getSalary();
        }
        salarySum.setText(SUM_RECORD_TEXT + sum);
    }
    public void changeCount() {
        countRecords.setText(COUNT_RECORD_TEXT + data.size());
    }
}
