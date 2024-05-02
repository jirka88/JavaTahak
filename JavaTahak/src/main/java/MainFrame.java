import tables.TableForNames;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;

public class MainFrame extends JFrame{
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel bottomPanel;
    private JPanel rightPanel;
    private JPanel mainPanel;
    private JButton importSouboru;
    private JTable tableNames = new JTable();

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
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        sc = new Scanner(selectedFile);
                        while (sc.hasNextLine()) {
                            String name = sc.nextLine().toLowerCase();
                            if(name.matches("^[\\p{L}]+$")) {
                                if(names.containsKey(name)) {
                                    int frequency = names.get(name);
                                    names.put(name, ++frequency);
                                }
                                else {
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
        topPanel.add(importSouboru);
        mainPanel.add(tableNames);

        //Přidání jednotlivých prvků k MainFrame
        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
    }
}
