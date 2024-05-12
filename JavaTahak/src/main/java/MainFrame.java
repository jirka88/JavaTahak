import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import data.PersonList;
import jsonClasses.Person;
import tables.TableForNames;
import tables.TableJsonPerson;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;
    private JButton importSouboru;
    private JButton exportSouboru;
    private JButton addNewRandomNumber;
    private JTable tableNames = new JTable();
    private JTable jsonPersonTable = new JTable();
    private TableJsonPerson abstractModel;
    private JList<String> list;
    private JLabel salarySum = new JLabel("Celkem platy: 0");
    private JLabel countRecords = new JLabel("Počet záznamů činí: 0");
    private PersonList data = new PersonList(salarySum, countRecords);

    private Scanner sc;

    public MainFrame() {
        //Základní nastavení okna
        setTitle("Název");
        setVisible(true);
        setBackground(Color.white);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Definování panelů
        topPanel = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        mainPanel = new JPanel();
        bottomPanel = new JPanel();

        //Tlačítko pro import souboru --> vypsání kolik jmen je v souboru
        //Logika
        JFileChooser fileChooser = new JFileChooser();
        HashMap<String, Integer> names = new HashMap<String, Integer>();
        importSouboru = new JButton("Import");
        /*importSouboru.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT", "txt");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    names.clear();
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        sc = new Scanner(selectedFile);
                        while (sc.hasNextLine()) {
                            String name = sc.nextLine().toLowerCase();
                            if (name.matches("^[\\p{L}]+$")) {
                                if (names.containsKey(name)) {
                                    int frequency = names.get(name);
                                    names.put(name, ++frequency);
                                } else {
                                    names.put(name, 1);
                                }
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "Nastala chyba!");
                        throw new RuntimeException(ex);
                    }
                }
                //tabulka pro jména
                tableNames.setModel(new TableForNames(names));
            }
        });*/
        //topPanel.add(importSouboru);
        //mainPanel.add(new JScrollPane(tableNames));

        //Vygenerování náhodných čísel + JList + btn + export
        DefaultListModel<String> nahodneCisla = new DefaultListModel<>();
        int nahoda = new Random().nextInt(10) + 1;

        for (int i = 0; i < nahoda; i++) {
            Random rd = new Random();
            nahodneCisla.addElement(Integer.toString(rd.nextInt(100) + 1));
        }

        list = new JList<>(nahodneCisla);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(100, 200));
        //mainPanel.add(listScroller);

        addNewRandomNumber = new JButton("Add Random Number");
        addNewRandomNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Random rd = new Random();
                nahodneCisla.addElement(Integer.toString(rd.nextInt(100) + 1));
            }
        });
        //topPanel.add(addNewRandomNumber);

        exportSouboru = new JButton("Export");
        /*exportSouboru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userSelection = fileChooser.showSaveDialog(mainPanel);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    String fileName = fileToSave.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!extension.equalsIgnoreCase("txt")) {
                        JOptionPane.showMessageDialog(null, "Špatný formát!");
                    } else {
                        try {
                            FileWriter myWriter = new FileWriter(fileToSave.getAbsolutePath());
                            SaveFile.saveAsTxt(myWriter, nahodneCisla);
                            myWriter.close();
                            JOptionPane.showMessageDialog(null, "Uloženo!");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                }
            }
        });*/
        //topPanel.add(exportSouboru);

        //Načtení souboru JSON nebo CSV --> Řeší se zde tabulka s operacemi CRUD
        jsonPersonTable.setModel(abstractModel = new TableJsonPerson(data.getData(), data));
        JButton importJsonOrCsv = new JButton("Import");
        importJsonOrCsv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON OR CSV", "json", "csv");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileName = selectedFile.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (!extension.equalsIgnoreCase("csv") && !extension.equalsIgnoreCase("json")) {
                        JOptionPane.showMessageDialog(null, "Špatný formát!");
                        return;
                    }
                    if(extension.equalsIgnoreCase("json")) {
                        Gson gson = new Gson();
                        java.lang.reflect.Type targetClassType = new TypeToken<List<Person>>() {
                        }.getType();
                        try {
                            Reader reader = new FileReader(selectedFile);
                            data.setData(gson.fromJson(new JsonReader(reader), targetClassType));
                        } catch (FileNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, "Nastala nečekaná chyba!");
                            throw new RuntimeException(ex);
                        }
                    }
                    else {
                        try {
                            String line = "";
                            BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                            data.getData().clear();
                            while ((line = br.readLine()) != null)
                            {
                                String[] rowData = line.split(";");
                                try {
                                    data.addPerson(new Person(rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], Integer.parseInt(rowData[5])));
                                }
                                catch(NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(null, "Nastala nečekaná chyba!");
                                }
                            }
                            br.close();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Nastala nečekaná chyba!");
                            throw new RuntimeException(ex);
                        }
                    }
                }
                jsonPersonTable.setModel(abstractModel = new TableJsonPerson(data.getData(), data));
                data.changeCount();
                data.changeSum();
            }
        });
        topPanel.add(importJsonOrCsv);

        JPanel tableWithForm = new JPanel();
        mainPanel.add(tableWithForm);
        tableWithForm.setLayout(new GridLayout(2,1));
        tableWithForm.add(new JScrollPane(jsonPersonTable));

        //vytvoření formuláře pro přidání prvků do tabulky
        JLabel firstnameLbl = new JLabel("Jméno");
        JTextField firstname = new JTextField(10);
        JLabel lastnameLbl = new JLabel("Příjmení");
        JTextField lastname = new JTextField(10);
        JLabel cityLbl = new JLabel("Město");
        JTextField city = new JTextField(10);
        JLabel countryLbl = new JLabel("Stát");
        JTextField country = new JTextField(10);
        JLabel countryCodeLbl = new JLabel("Code");
        JTextField countryCode = new JTextField(10);
        JLabel salaryLbl = new JLabel("Plat");
        JTextField salary = new JTextField(10);
        salary.setText("0");
        JButton submit = new JButton("vytvořit");
        JPanel formular = new JPanel();
        formular.setLayout(new GridLayout(16, 1));

        tableWithForm.add(formular);

        formular.add(countRecords);
        formular.add(salarySum);

        formular.add(firstnameLbl);
        formular.add(firstname);
        formular.add(lastnameLbl);
        formular.add(lastname);
        formular.add(cityLbl);
        formular.add(city);
        formular.add(countryLbl);
        formular.add(country);
        formular.add(countryCodeLbl);
        formular.add(countryCode);
        formular.add(salaryLbl);
        formular.add(salary);
        formular.add(submit);
        //Přidání prvku do tabulky
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstnameText = firstname.getText();
                String lastnameText = lastname.getText();
                String cityText = city.getText();
                String countryText = country.getText();
                String countryCodeText = countryCode.getText();
                int salaryText = 0;
                try {
                   salaryText = Integer.parseInt(salary.getText());
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Zadejte číselný plat!");
                    throw new NumberFormatException();
                }
                if(!firstnameText.isBlank() && !lastnameText.isBlank() && !cityText.isBlank() && !countryText.isBlank() && !countryCodeText.isBlank()) {
                    data.addPerson(new Person(firstnameText, lastnameText, cityText, countryText, countryCodeText, salaryText));
                    JOptionPane.showMessageDialog(null, "Údaj byl přidán!");
                    abstractModel.fireTableDataChanged();
                    data.changeCount();
                    data.changeSum();

                    firstname.setText("");
                    lastname.setText("");
                    city.setText("");
                    country.setText("");
                    countryCode.setText("");
                    salary.setText("0");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Nevyplněn údaj!");
                }
            }
        });

        //vymazaní prvku z tabulky
        JButton deleteRecord = new JButton("Vymaž záznam");
        deleteRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jsonPersonTable.getSelectedRow();
                if(jsonPersonTable.getColumnCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Tabulka je prázdná!");
                    return;
                }
                if(selectedRow != -1) {
                    data.removePerson(selectedRow);
                    data.changeSum();
                    abstractModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    data.changeCount();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Nebyla vybrána hodnota!");
                }
            }
        });
        formular.add(deleteRecord);

        //Export JSON a TXT z tabulky

        JButton exportJsonBtn = new JButton("Export");

        JFileChooser fileExport = new JFileChooser();
        exportJsonBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT OR JSON", "txt", "json");
                fileExport.setFileFilter(filter);
                int userSelection = fileExport.showOpenDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileExport.getSelectedFile();
                    String fileName = fileToSave.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

                    if (!extension.equalsIgnoreCase("txt") && !extension.equalsIgnoreCase("json")) {
                        JOptionPane.showMessageDialog(null, "Špatný formát!");
                        return;
                    }
                    try {
                        FileWriter myWriter = new FileWriter(fileToSave.getAbsolutePath());
                        if(extension.equalsIgnoreCase("txt")) {
                            for (Person person : data.getData()) {
                                myWriter.write(person.getFirstName() + " " + person.getLastName() + " " + person.getCity() + " " + person.getCountry() + " " + person.getCountryCode() + " " + person.getSalary() + "\n");
                            }
                        }
                        else {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            myWriter.write(gson.toJson(data.getData()));
                        }
                        myWriter.close();
                        JOptionPane.showMessageDialog(null, "Uloženo!");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        topPanel.add(exportJsonBtn);

        //čas
        /*JLabel CurrentDateTime = new JLabel();
        JLabel CurrentDateTime2 = new JLabel();
        DateFormat dateandtime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Timer t = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                CurrentDateTime.setText(dateandtime.format(date));
                repaint();
            }
        });
        t.start();
        bottomPanel.add(CurrentDateTime);*/
        //Přidání jednotlivých prvků k MainFrame
        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
    }

}
