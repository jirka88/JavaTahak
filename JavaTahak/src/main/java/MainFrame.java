import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import jsonClasses.Person;
import tables.TableForNames;
import utils.SaveFile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
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
    private JList<String> list;
    private List<Person> data;

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
        importSouboru.addActionListener(new ActionListener() {
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
        });
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
        exportSouboru.addActionListener(new ActionListener() {
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
        });
        //topPanel.add(exportSouboru);


        //Načtení souboru JSON
        JButton importJson = new JButton("Import do json");
        importJson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON", "json");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Gson gson = new Gson();
                    java.lang.reflect.Type targetClassType = new TypeToken<List<Person>>() {
                    }.getType();
                    try {
                        Reader reader = new FileReader(selectedFile);
                        data = gson.fromJson(new JsonReader(reader), targetClassType);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        topPanel.add(importJson);

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
