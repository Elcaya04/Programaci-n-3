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
    public void agregarTabs(Dictionary<String, JPanel> tabs,Dictionary<String, JPanel> tabs2,Dictionary<String, JPanel> tabs3){
        /// Agregacion de los tabs
        Enumeration<String> keys = tabs.keys();
        Enumeration<String> keys2 = tabs2.keys();
        Enumeration<String> keys3 = tabs3.keys();
        while (keys.hasMoreElements()&&keys2.hasMoreElements()&&keys3.hasMoreElements()) {
            String titulo = keys.nextElement();
            JPanel contenido = tabs.get(titulo);
            String titulo2 = keys2.nextElement();
            JPanel contenido2 = tabs2.get(titulo2);
            String titulo3 = keys3.nextElement();
            JPanel contenido3 = tabs3.get(titulo3);
            MainTabPanel.addTab(titulo, contenido);
            MainTabPanel.addTab(titulo2, contenido2);
            MainTabPanel.addTab(titulo3, contenido3);
        }
    }
}
