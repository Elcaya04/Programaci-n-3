package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import presentation_layer.Controllers.FarmaceutaController;
import presentation_layer.Controllers.MedicoController;
import presentation_layer.Controllers.PacienteController;
import presentation_layer.Models.FarmaceutaTableModel;
import presentation_layer.Models.MedicoTableModel;
import presentation_layer.Models.PacienteTableModel;
import presentation_layer.Views.FarmaceutaView.FarmaceutaView;
import presentation_layer.Views.MedicoView.MedicoView;
import presentation_layer.Views.PacienteView.PacienteView;
import presentation_layer.Views.LoginView.LoginView;
import service_layer.FarmaceutaService;
import service_layer.MedicoService;
import service_layer.PacienteService;
import service_layer.Service;
import utilites.FileManagement;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Main {
    private static MedicoView medicoView;
    private static FarmaceutaView farmaceutaView;
    private static PacienteView pacienteView;

    private static Dictionary<String, JPanel> tabs;
    private static Dictionary<String, JPanel> tabs2;
    private static Dictionary<String, JPanel> tabs3;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Infraestructura medico
        Service<Medico> medicoService =
                new MedicoService(FileManagement.getMedicosFileStore("medicos.xml"));
        MedicoController medicoController = new MedicoController(medicoService);
        MedicoTableModel medicoTableModel = new MedicoTableModel();
        medicoView = new MedicoView(medicoController, medicoTableModel, medicoController.leerTodos());
        medicoService.Observer(medicoTableModel);
        tabs = new Hashtable<>();
        tabs.put("Medico", medicoView.getContentPanel());

        // Infraestructura farmaceuta
        Service<Farmaceuta> farmaceutaService =
                new FarmaceutaService(FileManagement.getFarmaceutasFileStore("farmaceuticos.xml"));
        FarmaceutaController farmaceutaController = new FarmaceutaController(farmaceutaService);
        FarmaceutaTableModel farmaceutaTableModel = new FarmaceutaTableModel();
        farmaceutaView = new FarmaceutaView(farmaceutaController, farmaceutaTableModel, farmaceutaController.leerTodos());
        farmaceutaService.Observer(farmaceutaTableModel);
        tabs2 = new Hashtable<>();
        tabs2.put("Farmaceuta", farmaceutaView.getContentPanel());

        // Infraestructura paciente
        Service<Paciente> pacienteService =
                new PacienteService(FileManagement.getPacientesFileStore("pacientes.xml"));
        PacienteController pacienteController = new PacienteController(pacienteService);
        PacienteTableModel pacienteTableModel = new PacienteTableModel();
        pacienteView = new PacienteView(pacienteController, pacienteTableModel, pacienteController.leerTodos());
        pacienteService.Observer(pacienteTableModel);
        tabs3 = new Hashtable<>();
        tabs3.put("Paciente", pacienteView.getContentPanel());

        // Mostrar Login primero
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }

    public static MedicoView getMedicoView() { return medicoView; }
    public static FarmaceutaView getFarmaceutaView() { return farmaceutaView; }
    public static PacienteView getPacienteView() { return pacienteView; }

    public static Dictionary<String, JPanel> getTabs() { return tabs; }
    public static Dictionary<String, JPanel> getTabs2() { return tabs2; }
    public static Dictionary<String, JPanel> getTabs3() { return tabs3; }
}