package presentation_layer.Views.MainWindow;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Enumeration;

public class MainWindow extends JFrame {

    private JPanel Panel_Conte;
    private JTabbedPane MainTabPanel;
    public MainWindow() {
        setTitle("Sistema con Tabs");
        setContentPane(MainTabPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    public void agregarTabs(Dictionary<String, JPanel> tabs){
        Enumeration<String> keys = tabs.keys();
        while (keys.hasMoreElements()) {
            String titulo = keys.nextElement();
            JPanel contenido = tabs.get(titulo);
            MainTabPanel.addTab(titulo, contenido);
        }
    }
}
