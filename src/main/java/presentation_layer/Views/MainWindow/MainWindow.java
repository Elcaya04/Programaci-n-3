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

    public void agregarTabs(Dictionary<String, JPanel> tabs, Dictionary<String, JPanel> tabs2, Dictionary<String, JPanel> tabs3,Dictionary<String, JPanel> tabs4,Dictionary<String, JPanel> tabs5) {
        // Agregar tabs del primer diccionario
        if (tabs != null && !tabs.isEmpty()) {
            Enumeration<String> keys = tabs.keys();
            while (keys.hasMoreElements()) {
                String titulo = keys.nextElement();
                JPanel contenido = tabs.get(titulo);
                MainTabPanel.addTab(titulo, contenido);
            }
        }

        // Agregar tabs del segundo diccionario
        if (tabs2 != null && !tabs2.isEmpty()) {
            Enumeration<String> keys2 = tabs2.keys();
            while (keys2.hasMoreElements()) {
                String titulo2 = keys2.nextElement();
                JPanel contenido2 = tabs2.get(titulo2);
                MainTabPanel.addTab(titulo2, contenido2);
            }
        }

        // Agregar tabs del tercer diccionario
        if (tabs3 != null && !tabs3.isEmpty()) {
            Enumeration<String> keys3 = tabs3.keys();
            while (keys3.hasMoreElements()) {
                String titulo3 = keys3.nextElement();
                JPanel contenido3 = tabs3.get(titulo3);
                MainTabPanel.addTab(titulo3, contenido3);
            }
        }
    }
}

