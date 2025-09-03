package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import presentation_layer.Controllers.FarmaceutaController;
import presentation_layer.Controllers.MedicoController;
import presentation_layer.Models.FarmaceutaTableModel;
import presentation_layer.Models.MedicoTableModel;
import presentation_layer.Views.FarmaceutaView.FarmaceutaView;
import presentation_layer.Views.MainWindow.MainWindow;
import presentation_layer.Views.MedicoView.MedicoView;
import service_layer.FarmaceutaService;
import service_layer.MedicoService;
import service_layer.Service;
import utilites.FileManagement;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        /// Infraestructura de los medicos
        Service<Medico> medicoService = new MedicoService(FileManagement.getMedicosFileStore("medicos.xml"));
        MedicoController medicoController = new MedicoController(medicoService);
        MedicoTableModel medicoTableModel = new MedicoTableModel();
        MedicoView medicoView = new MedicoView(medicoController, medicoTableModel, medicoController.leerTodos());
        medicoService.Observer(medicoTableModel);
        Dictionary<String, JPanel> tabs = new Hashtable<>();
        tabs.put("Medico", medicoView.getContentPanel());
        /// Infraestructura de los farmaceutas
        Service<Farmaceuta> farmaceutaService=new FarmaceutaService(FileManagement.getFarmaceutasFileStore("farmaceuticos.xml"));
        FarmaceutaController farmaceutaController=new FarmaceutaController(farmaceutaService);
        FarmaceutaTableModel  farmaceutaTableModel = new FarmaceutaTableModel();
        FarmaceutaView farmaceutaView=new FarmaceutaView(farmaceutaController, farmaceutaTableModel, farmaceutaController.leerTodos());
        farmaceutaService.Observer(farmaceutaTableModel);
        Dictionary<String, JPanel> tabs2 = new Hashtable<>();
        tabs2.put("Farmaceuta", farmaceutaView.getContentPanel());
        /// Creacion de el mainTAB
        MainWindow window = new MainWindow();
        window.agregarTabs(tabs,tabs2);
        SwingUtilities.invokeLater(() -> {window.setVisible(true);});

    }
}