package utils;


import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;

public class SaveFile {
    public static void saveAsTxt(FileWriter MyWriter, DefaultListModel<String> nahodneCisla) {
        for(int i = 0; i < nahodneCisla.size(); i++) {
            try {
                MyWriter.write(nahodneCisla.get(i) + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
