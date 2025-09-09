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

    public void agregarTabs(Dictionary<String, JPanel> tabs, Dictionary<String, JPanel> tabs2, Dictionary<String, JPanel> tabs3,
                            Dictionary<String, JPanel> tabs4,Dictionary<String, JPanel> tabs5,Dictionary<String,JPanel> tabs6,
                            Dictionary<String, JPanel> tabs7) {
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
            if(tabs4 != null && !tabs4.isEmpty()) {
                Enumeration<String> keys4 = tabs4.keys();
                while (keys4.hasMoreElements()) {
                    String titulo4 = keys4.nextElement();
                    JPanel contenido4 = tabs4.get(titulo4);
                    MainTabPanel.addTab(titulo4, contenido4);
                }
            }
            if(tabs5 != null && !tabs5.isEmpty()) {
                Enumeration<String> keys5 = tabs5.keys();
                while (keys5.hasMoreElements()) {
                    String titulo5 = keys5.nextElement();
                    JPanel contenido5 = tabs5.get(titulo5);
                    MainTabPanel.addTab(titulo5, contenido5);
                }

            }
            if (tabs6 != null && !tabs6.isEmpty()) {
                Enumeration<String> keys6 = tabs6.keys();
                while (keys6.hasMoreElements()) {
                    String titulo6 = keys6.nextElement();
                    JPanel contenido6 = tabs6.get(titulo6);
                    MainTabPanel.addTab(titulo6, contenido6);
                }
            }
            if (tabs7 != null && !tabs7.isEmpty()) {
                Enumeration<String> keys7 = tabs7.keys();
                while (keys7.hasMoreElements()) {
                    String titulo7 = keys7.nextElement();
                    JPanel contenido7 = tabs7.get(titulo7);
                    MainTabPanel.addTab(titulo7, contenido7);
                }
            }

        }
    }


