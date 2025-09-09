package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.example.domain_layer.*;
import presentation_layer.Controllers.*;
import presentation_layer.Models.*;
import presentation_layer.Views.DespachoView.DespachoView;
import presentation_layer.Views.FarmaceutaView.FarmaceutaView;
import presentation_layer.Views.MainWindow.MainWindow;
import presentation_layer.Views.MedicamentosView.MedicamentosView;
import presentation_layer.Views.MedicoView.MedicoView;
import presentation_layer.Views.PacienteView.PacienteView;
import presentation_layer.Views.LoginView.LoginView;
import presentation_layer.Views.PrescripcionView.PrescripcionView;
import presentation_layer.Views.RecetasChartView.RecetasChartView;
import presentation_layer.Views.RecetasHistoricoView.RecetasHistoricoView;
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
    private static Service<RecetaMedica> recetaMedicaService;
//Llamadas a clases


    // Vistas globales
    private static MedicoView medicoView;
    private static FarmaceutaView farmaceutaView;
    private static PacienteView pacienteView;
    private static MedicamentosView  medicamentosView;
    private static PrescripcionView prescripcionView;
    private static RecetasHistoricoView recetasHistoricoView;
    private static RecetasChartView recetasChartView;
    private static DespachoView despachoView;
    private static MainWindow mainWindow;

    // Diccionarios de tabs (manteniendo la estructura original)
    private static Dictionary<String, JPanel> tabs;
    private static Dictionary<String, JPanel> tabs2;
    private static Dictionary<String, JPanel> tabs3;
    private static Dictionary<String, JPanel> tabs4;
    private static Dictionary<String, JPanel> tabs5;
    private static Dictionary<String, JPanel> tabs6;
    private static Dictionary<String, JPanel> tabs7;
    //Para el usuario Logeado
    private static Object usuarioLogueado;
//Funcion main donde se llaman todos los metodos para su ejecucion
    public static void main(String[] args) {
        configurarLookAndFeel();
        inicializarServicios();
        inicializarVistas();
        mostrarLogin();
    }
//Metodo para configurar la vista de la interfaz grafica
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
        //Inicializar servicio de medicamentos
        medicamentosService = new MedicamentoService(FileManagement.getMedicamentosFileStore("medicamentos.xml"));
        //Inicializar servicio de Receta Medica
recetaMedicaService = new RecetaMedicaService(FileManagement.getRecetaMedicaFileStore("recetas.xml"));



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

        // 1. Controller para RecetaMedica (para el histórico)
        RecetaMedicaController recetaMedicaController = new RecetaMedicaController(recetaMedicaService);
        RecetaMedicaTableModel recetaMedicaTableModel = new RecetaMedicaTableModel();

        // 2. Controller para Prescripcion (para crear recetas)
        PrescipcionController prescripcionController = new PrescipcionController(
                recetaMedicaService,
                pacienteService,
                medicamentosService
        );

        // 3. Vista de prescripción (donde se crean las recetas)
        prescripcionView = new PrescripcionView(
                prescripcionController,
                pacienteService,
                medicamentosService,pacienteController,medicamentosController,pacienteTableModel,
                medicamentoTableModel,recetaMedicaController,recetaMedicaTableModel,null
        );

        // 4. Vista de histórico (donde se ven las recetas guardadas)
        recetasHistoricoView = new RecetasHistoricoView(
                recetaMedicaController,
                recetaMedicaTableModel,
                recetaMedicaController.leerTodos()
        );
        recetaMedicaService.Observer(recetaMedicaTableModel);
        //5. Vista de el dashboard
        RecetaMedicaChartController recetaMedicaChartController = new RecetaMedicaChartController(recetaMedicaService);
        recetasChartView = new RecetasChartView(recetaMedicaChartController);
        //Inicializo los tabs6 y los agrego a las vistas
        tabs6 = new Hashtable<>();
        tabs6.put("Dashboard", recetasChartView.getContentPanel());
        // Inicializar tabs5 y agregar las vistas
        tabs5 = new Hashtable<>();
        tabs5.put("Prescribir", prescripcionView.getContentPanel());
        tabs5.put("Historico", recetasHistoricoView.getContentPanel());
        //Inicializo los tabs 7 y los agrego a las vistas
        //Con el controller y el view del Despacho
        DespachoController despachoController = new DespachoController(recetaMedicaService);
        despachoView = new DespachoView(despachoController,null);
        tabs7 = new Hashtable<>();
        tabs7.put("Despacho", despachoView.getContentPanel());

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
        establecerUsuarioLogueado(usuario, tipoUsuario);

        switch (tipoUsuario) {
            case ADMINISTRADOR:
                // El administrador puede ver todos los tabs
                mainWindow.agregarTabs(tabs, tabs2, tabs3,tabs4, tabs5,tabs6,tabs7);
                break;

            case MEDICO:
                // Los médicos pueden ver médicos y pacientes
                Dictionary<String, JPanel> tabsVacios = new Hashtable<>();
                Dictionary<String, JPanel> tabsVacios2 = new Hashtable<>();
                Dictionary<String, JPanel> tabsVacios3 = new Hashtable<>();
                Dictionary<String, JPanel> tabsVacios4 = new Hashtable<>();
                Dictionary<String, JPanel> tabsVacios7 = new Hashtable<>();

                mainWindow.agregarTabs(tabsVacios,tabsVacios2, tabsVacios3,tabsVacios4,tabs5,tabs6,tabsVacios7);
                break;

            case FARMACEUTA:
                // Los farmaceutas pueden ver farmaceutas y pacientes
                Dictionary<String, JPanel> tabsVacios2_ = new Hashtable<>();
                Dictionary<String, JPanel> tabsVacios4_ = new Hashtable<>();
                Dictionary<String, JPanel> tabsVacios5 = new Hashtable<>();
                mainWindow.agregarTabs(tabsVacios2_, tabs2, tabs3,tabsVacios4_, tabsVacios5,tabs6,tabs7);
                break;

            default:
                JOptionPane.showMessageDialog(mainWindow, "Tipo de usuario no reconocido");
                break;
        }
    }
    private static void establecerUsuarioLogueado(String usuario, UserType tipoUsuario) {
        try {
            switch (tipoUsuario) {
                case MEDICO:
                    boolean medicoEncontrado = false;
                    for (Medico medico : medicoService.LeerTodo()) {
                        if (medico.getID().equals(usuario) || medico.getNombre().equals(usuario)) {
                            usuarioLogueado = medico;
                            prescripcionView.setMedicoActual(medico);
                            medicoEncontrado = true;
                            System.out.println("Médico logueado: " + medico.getNombre() + " (ID: " + medico.getID() + ")");
                            break;
                        }
                    }

                    if (!medicoEncontrado) {
                        System.out.println("Advertencia: No se encontró el médico con usuario: " + usuario);
                        // Para administradores que no sean médicos, crear un médico temporal
                        if (tipoUsuario == UserType.ADMINISTRADOR) {
                            Medico medicoTemporal = new Medico("ADMIN-001", "Administrador",
                                    "Cardiologo", "ADMIN-001");
                            prescripcionView.setMedicoActual(medicoTemporal);
                            usuarioLogueado = medicoTemporal;
                        }
                    }
                    break;

                case FARMACEUTA:
                    // Buscar el farmaceuta en el servicio
                    boolean farmaceutaEncontrado = false;
                    for (Farmaceuta farmaceuta : farmaceutaService.LeerTodo()) {
                        if (farmaceuta.getID().equals(usuario) || farmaceuta.getNombre().equals(usuario)) {
                            usuarioLogueado = farmaceuta;
                            if (despachoView != null) {
                                despachoView.setFarmaceutaActual(farmaceuta);
                            }
                            farmaceutaEncontrado = true;
                            System.out.println("Farmaceuta logueado: " + farmaceuta.getNombre());
                            break;
                        }
                        if (!farmaceutaEncontrado) {
                            System.out.println("Advertencia: No se encontró el farmaceuta con usuario: " + usuario);
                            // Crear farmaceuta temporal si es necesario
                            Farmaceuta farmaceutaTemporal = new Farmaceuta("FARM-001","1234", "Farmaceuta Temporal");
                            usuarioLogueado = farmaceutaTemporal;
                            if (despachoView != null) {
                                despachoView.setFarmaceutaActual(farmaceutaTemporal);
                            }
                        }

                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error al establecer usuario logueado: " + e.getMessage());
            e.printStackTrace();
        }
    }

}