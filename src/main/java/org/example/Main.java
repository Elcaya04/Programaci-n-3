package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medicamentos;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import presentation_layer.Controllers.FarmaceutaController;
import presentation_layer.Controllers.MedicamentosController;
import presentation_layer.Controllers.MedicoController;
import presentation_layer.Controllers.PacienteController;
import presentation_layer.Models.FarmaceutaTableModel;
import presentation_layer.Models.MedicamentoTableModel;
import presentation_layer.Models.MedicoTableModel;
import presentation_layer.Models.PacienteTableModel;
import presentation_layer.Views.FarmaceutaView.FarmaceutaView;
import presentation_layer.Views.MainWindow.MainWindow;
import presentation_layer.Views.MedicamentosView.MedicamentosView;
import presentation_layer.Views.MedicoView.MedicoView;
import presentation_layer.Views.PacienteView.PacienteView;
import presentation_layer.Views.LoginView.LoginView;
import service_layer.*;
import utilites.FileManagement;
import utilites.UserType;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Main {
    // Servicios globales
    private static Service<Medico> medicoService;
    private static Service<Farmaceuta> farmaceutaService;
    private static Service<Paciente> pacienteService;
    private static Service<Medicamentos> medicamentosService;

    // Vistas globales
    private static MedicoView medicoView;
    private static FarmaceutaView farmaceutaView;
    private static PacienteView pacienteView;
    private static MedicamentosView  medicamentosView;
    private static MainWindow mainWindow;

    // Diccionarios de tabs (manteniendo la estructura original)
    private static Dictionary<String, JPanel> tabs;
    private static Dictionary<String, JPanel> tabs2;
    private static Dictionary<String, JPanel> tabs3;
    private static Dictionary<String, JPanel> tabs4;

    public static void main(String[] args) {
        configurarLookAndFeel();
        inicializarServicios();
        inicializarVistas();
        mostrarLogin();
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void inicializarServicios() {
        // Inicializar servicio de médicos
        medicoService = new MedicoService(
                FileManagement.getMedicosFileStore("medicos.xml")
        );

        // Inicializar servicio de farmaceutas
        farmaceutaService = new FarmaceutaService(
                FileManagement.getFarmaceutasFileStore("farmaceuticos.xml")
        );

        // Inicializar servicio de pacientes
        pacienteService = new PacienteService(
                FileManagement.getPacientesFileStore("pacientes.xml")
        );
        medicamentosService = new MedicamentoService(FileManagement.getMedicamentosFileStore("medicamentos.xml"));
    }

    private static void inicializarVistas() {
        // Infraestructura medico
        MedicoController medicoController = new MedicoController(medicoService);
        MedicoTableModel medicoTableModel = new MedicoTableModel();
        medicoView = new MedicoView(
                medicoController,
                medicoTableModel,
                medicoController.leerTodos()
        );
        medicoService.Observer(medicoTableModel);
        tabs = new Hashtable<>();
        tabs.put("Medico", medicoView.getContentPanel());

        // Infraestructura farmaceuta
        FarmaceutaController farmaceutaController = new FarmaceutaController(farmaceutaService);
        FarmaceutaTableModel farmaceutaTableModel = new FarmaceutaTableModel();
        farmaceutaView = new FarmaceutaView(
                farmaceutaController,
                farmaceutaTableModel,
                farmaceutaController.leerTodos()
        );
        farmaceutaService.Observer(farmaceutaTableModel);
        tabs2 = new Hashtable<>();
        tabs2.put("Farmaceuta", farmaceutaView.getContentPanel());

        // Infraestructura paciente
        PacienteController pacienteController = new PacienteController(pacienteService);
        PacienteTableModel pacienteTableModel = new PacienteTableModel();
        pacienteView = new PacienteView(
                pacienteController,
                pacienteTableModel,
                pacienteController.leerTodos()
        );
        pacienteService.Observer(pacienteTableModel);
        tabs3 = new Hashtable<>();
        tabs3.put("Paciente", pacienteView.getContentPanel());
        //Infraestructura de Medicamentos
        MedicamentosController medicamentosController = new MedicamentosController(medicamentosService);
        MedicamentoTableModel medicamentoTableModel = new MedicamentoTableModel();
        medicamentosView = new MedicamentosView(medicamentosController,medicamentoTableModel,medicamentosController.leerTodos());
        medicamentosService.Observer(medicamentoTableModel);
        tabs4 = new Hashtable<>();
        tabs4.put("Medicamento", medicamentosView.getContentPanel());

        // Inicializar ventana principal
        mainWindow = new MainWindow();
    }

    private static void mostrarLogin() {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView(
                    farmaceutaService,
                    medicoService,
                    new LoginView.LoginCallback() {
                        @Override
                        public void onLoginSuccess(String usuario, UserType tipoUsuario) {
                            // Configurar la ventana principal según el tipo de usuario
                            configurarInterfazSegunUsuario(usuario, tipoUsuario);
                            mainWindow.setVisible(true);
                        }
                    }
            );
            loginView.setVisible(true);
        });
    }

    private static void configurarInterfazSegunUsuario(String usuario, UserType tipoUsuario) {
        // Configurar título con el usuario
        mainWindow.setTitle("Sistema de Gestión - Usuario: " + usuario);

        switch (tipoUsuario) {
            case ADMINISTRADOR:
                // El administrador puede ver todos los tabs
                mainWindow.agregarTabs(tabs, tabs2, tabs3);
                break;

            case MEDICO:
                // Los médicos pueden ver médicos y pacientes

                mainWindow.agregarTabs(tabs, tabs4, tabs3);
                break;

            case FARMACEUTA:
                // Los farmaceutas pueden ver farmaceutas y pacientes
                Dictionary<String, JPanel> tabsVacios2 = new Hashtable<>();
                mainWindow.agregarTabs(tabsVacios2, tabs2, tabs3);
                break;

            default:
                JOptionPane.showMessageDialog(mainWindow, "Tipo de usuario no reconocido");
                break;
        }
    }

    // Métodos de utilidad para acceder a los servicios desde otras clases si es necesario
    public static Service<Medico> getMedicoService() {
        return medicoService;
    }

    public static Service<Farmaceuta> getFarmaceutaService() {
        return farmaceutaService;
    }

    public static Service<Paciente> getPacienteService() {
        return pacienteService;
    }

    // Métodos de utilidad para acceder a los diccionarios de tabs si es necesario
    public static Dictionary<String, JPanel> getTabs() {
        return tabs;
    }

    public static Dictionary<String, JPanel> getTabs2() {
        return tabs2;
    }

    public static Dictionary<String, JPanel> getTabs3() {
        return tabs3;
    }
}