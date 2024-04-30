import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    public MainFrame() {
        //Základní nastavení okna
        setTitle("Název");
        setVisible(true);
        setBackground(Color.white);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
}
