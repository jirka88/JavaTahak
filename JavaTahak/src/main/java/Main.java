import javax.swing.*;

public class Main {
    //Vytvoření nového swing okna
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame();
            } catch (Exception e) {
                System.out.println("GUI ERROR: "+e);
            }
        });

    }
}