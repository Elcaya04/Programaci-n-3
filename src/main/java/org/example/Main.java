package org.example;

import org.example.domain_layer.Medico;
import presentation_layer.Controllers.MedicoController;
import presentation_layer.Models.MedicoTableModel;
import presentation_layer.Views.MainWindow.MainWindow;
import presentation_layer.Views.MedicoView.MedicoView;
import service_layer.MedicoService;
import service_layer.Service;
import utilites.FileManagement;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Main {
    public static void main(String[] args) {
        /// Infraestructura de los medicos
        Service<Medico> medicoService = new MedicoService(FileManagement.getMedicosFileStore("medicos.xml"));
        MedicoController medicoController = new MedicoController(medicoService);
        MedicoTableModel medicoTableModel = new MedicoTableModel();
        MedicoView medicoView = new MedicoView(medicoController, medicoTableModel, medicoController.leerTodos());
        medicoService.Observer(medicoTableModel);
        Dictionary<String, JPanel> tabs = new Hashtable<>();
        tabs.put("Medico", medicoView.getContentPanel());
        /// Creacion de el mainTAB
        MainWindow window = new MainWindow();
        window.agregarTabs(tabs);
        window.setVisible(true);
    }
}