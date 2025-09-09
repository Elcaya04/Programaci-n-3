package presentation_layer.Views.PrescripcionView;

import org.example.domain_layer.Medicamentos;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import org.example.domain_layer.RecetaMedica;
import presentation_layer.Controllers.MedicamentosController;
import presentation_layer.Controllers.PrescipcionController;
import presentation_layer.Controllers.RecetaMedicaController;
import presentation_layer.Models.MedicamentoTableModel;
import presentation_layer.Models.PacienteTableModel;
import presentation_layer.Models.RecetaMedicaTableModel;
import service_layer.Service;
import utilites.PrescriptionState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrescripcionView {
    // Componentes principales
    private JButton buscarPacienteButton;
    private JButton agregarMedicamentoButton;
    private JComboBox<String> fechaRetiroBox;
    private JButton guardarButton;
    private JButton limpiarButton;

    // Labels para mostrar información
    private JLabel nombrePaciente;
    private JLabel cedulaPaciente;
    private JLabel telefonoPaciente;
    private JLabel fechaActualLabel;

    // Tabla para los medicamentos de la receta
    private JTable tablaRecetaMedica;
    private DefaultTableModel modeloTabla;

    // Panel principal
    private JPanel panelContenido;

    // Controlador y servicios
    private PrescipcionController controller;

    // Datos actuales
    private Paciente pacienteActual;
    private List<RecetaMedica> medicamentosReceta;
    private Medico medicoActual;
    //Servicios
    private Service<Paciente> pacienteService;
    private Service<Medicamentos> medicamentosService;
    //Controllers
    private PacienteController pacienteController;
    private MedicamentosController medicamentosController;
    private RecetaMedicaController recetaMedicaController;
    //Table model
    private PacienteTableModel pacienteTableModel;
    private MedicamentoTableModel medicamentosTableModel;
    private RecetaMedicaTableModel recetaMedicaTableModel;

    public PrescripcionView(PrescipcionController controller,
                            Service<Paciente> pacienteService,
                            Service<Medicamentos> medicamentosService,PacienteController pacienteController,MedicamentosController medicamentosController,
                            PacienteTableModel pacienteTableModel,MedicamentoTableModel medicamentosTableModel,
                            RecetaMedicaController recetaMedicaController,RecetaMedicaTableModel recetaMedicaTableModel,Medico medicoActual) {
        this.controller = controller;
        this.pacienteController = pacienteController;
        this.medicamentosController = medicamentosController;
        this.recetaMedicaController = recetaMedicaController;
        this.pacienteService = pacienteService;
        this.medicamentosService = medicamentosService;
        this.pacienteTableModel = pacienteTableModel;
        this.medicamentosTableModel = medicamentosTableModel;
        this.recetaMedicaTableModel = recetaMedicaTableModel;
        this.medicoActual = medicoActual;
        this.medicamentosReceta = new ArrayList<>();


        inicializarComponentes();
        configurarEventos();
        inicializarDatos();
    }

    private void inicializarComponentes() {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior - Título
        JPanel panelTitulo = createTituloPanel();

        // Panel central - Formulario
        JPanel panelFormulario = createFormularioPanel();

        // Panel inferior - Tabla
        JPanel panelTabla = createTablaPanel();

        panelContenido.add(panelTitulo, BorderLayout.NORTH);
        panelContenido.add(panelFormulario, BorderLayout.CENTER);
        panelContenido.add(panelTabla, BorderLayout.SOUTH);
    }

    private JPanel createTituloPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("Nueva Prescripción Médica");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        fechaActualLabel = new JLabel();
        actualizarFechaActual();
        fechaActualLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        fechaActualLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        panel.add(titulo, BorderLayout.CENTER);
        panel.add(fechaActualLabel, BorderLayout.EAST);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        return panel;
    }

    private JPanel createFormularioPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Sección del paciente
        JPanel panelPaciente = createPacientePanel();
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(panelPaciente, gbc);

        // Sección de medicamento
        JPanel panelMedicamento = createMedicamentoPanel();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.6;
        panel.add(panelMedicamento, gbc);

        // Sección de fecha de retiro
        JPanel panelFecha = createFechaPanel();
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0.4;
        panel.add(panelFecha, gbc);

        // Sección de botones
        JPanel panelBotones = createBotonesPanel();
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(panelBotones, gbc);

        return panel;
    }

    private JPanel createPacientePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información del Paciente"));

        // Panel superior con botón
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscarPacienteButton = new JButton("Buscar Paciente");
        buscarPacienteButton.setPreferredSize(new Dimension(140, 30));
        buscarPacienteButton.setBackground(new Color(33, 150, 243));
        buscarPacienteButton.setForeground(Color.WHITE);
        panelSuperior.add(buscarPacienteButton);

        // Panel inferior con información del paciente
        JPanel panelInfo = new JPanel(new GridLayout(3, 2, 5, 5));
        panelInfo.setBorder(new EmptyBorder(10, 10, 10, 10));

        panelInfo.add(new JLabel("Nombre:"));
        nombrePaciente = new JLabel("No seleccionado");
        nombrePaciente.setFont(nombrePaciente.getFont().deriveFont(Font.BOLD));
        panelInfo.add(nombrePaciente);

        panelInfo.add(new JLabel("Cédula:"));
        cedulaPaciente = new JLabel("-");
        panelInfo.add(cedulaPaciente);

        panelInfo.add(new JLabel("Teléfono:"));
        telefonoPaciente = new JLabel("-");
        panelInfo.add(telefonoPaciente);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMedicamentoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agregar Medicamento"));

        agregarMedicamentoButton = new JButton("Agregar Medicamento");
        agregarMedicamentoButton.setPreferredSize(new Dimension(160, 35));
        agregarMedicamentoButton.setBackground(new Color(76, 175, 80));
        agregarMedicamentoButton.setForeground(Color.WHITE);
        agregarMedicamentoButton.setEnabled(false); // Habilitado solo cuando hay paciente

        JPanel panelCentro = new JPanel(new FlowLayout());
        panelCentro.add(agregarMedicamentoButton);

        panel.add(panelCentro, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFechaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Fecha de Retiro"));

        fechaRetiroBox = new JComboBox<>();
        cargarFechasRetiro();
        fechaRetiroBox.setPreferredSize(new Dimension(200, 25));

        JPanel panelCentro = new JPanel(new FlowLayout());
        panelCentro.add(fechaRetiroBox);

        panel.add(panelCentro, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBotonesPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        guardarButton = new JButton("Guardar Receta");
        guardarButton.setPreferredSize(new Dimension(130, 35));
        guardarButton.setBackground(new Color(46, 125, 50));
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setEnabled(false);

        limpiarButton = new JButton("Limpiar");
        limpiarButton.setPreferredSize(new Dimension(100, 35));
        limpiarButton.setBackground(new Color(255, 152, 0));
        limpiarButton.setForeground(Color.WHITE);

        panel.add(guardarButton);
        panel.add(limpiarButton);

        return panel;
    }

    private JPanel createTablaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Medicamentos en la Receta"));

        // Crear modelo de tabla
        String[] columnas = {"Medicamento", "Presentación", "Cantidad", "Duración", "Indicaciones","Estado","Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna de acciones es editable
            }
        };

        tablaRecetaMedica = new JTable(modeloTabla);
        tablaRecetaMedica.setRowHeight(25);

        // Configurar anchos de columnas
        tablaRecetaMedica.getColumnModel().getColumn(0).setPreferredWidth(200); // Medicamento
        tablaRecetaMedica.getColumnModel().getColumn(1).setPreferredWidth(120); // Presentación
        tablaRecetaMedica.getColumnModel().getColumn(2).setPreferredWidth(80);  // Cantidad
        tablaRecetaMedica.getColumnModel().getColumn(3).setPreferredWidth(80);  // Duración
        tablaRecetaMedica.getColumnModel().getColumn(4).setPreferredWidth(250); // Indicaciones
        tablaRecetaMedica.getColumnModel().getColumn(5).setPreferredWidth(100);// Estado
        tablaRecetaMedica.getColumnModel().getColumn(6).setPreferredWidth(80);// Acciones

        JScrollPane scrollPane = new JScrollPane(tablaRecetaMedica);
        scrollPane.setPreferredSize(new Dimension(0, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void configurarEventos() {
        buscarPacienteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarPaciente();
            }
        });

        agregarMedicamentoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarMedicamento();
            }
        });

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarReceta();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });
    }

    private void buscarPaciente() {
        try {
            // Cargar todos los pacientes
            List<Paciente> pacientes = pacienteService.LeerTodo();

            // Abrir dialog de selección
            PacienteDialog dialog = new PacienteDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(panelContenido),
                    pacienteController,
                    pacienteTableModel,
                    pacientes
            );

            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                Paciente pacienteSeleccionado = dialog.getPacienteSeleccionado();
                if (pacienteSeleccionado != null) {
                    this.pacienteActual = pacienteSeleccionado;
                    actualizarDatosPaciente(pacienteSeleccionado);
                    agregarMedicamentoButton.setEnabled(true);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelContenido,
                    "Error al cargar pacientes: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarMedicamento() {
        if (pacienteActual == null) {
            JOptionPane.showMessageDialog(panelContenido,
                    "Primero debe seleccionar un paciente",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Cargar todos los medicamentos
            List<Medicamentos> medicamentos = medicamentosService.LeerTodo();

            // Abrir dialog de selección de medicamentos
            MedicamentoDialog dialog = new MedicamentoDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(panelContenido),
                    medicamentosController,
                    medicamentosTableModel,
                    medicamentos
            );

            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                Medicamentos medicamentoSeleccionado = dialog.getMedicamentoSeleccionado();
                if (medicamentoSeleccionado != null) {
                    // Abrir dialog de detalles de la receta
                    DetalleRecetaDialog detalleDialog = new DetalleRecetaDialog(
                            (JFrame) SwingUtilities.getWindowAncestor(panelContenido),
                            pacienteActual,
                            medicamentoSeleccionado
                    );

                    detalleDialog.setVisible(true);

                    if (detalleDialog.isConfirmado()) {
                        agregarMedicamentoATabla(
                                medicamentoSeleccionado,
                                detalleDialog.getCantidad(),
                                detalleDialog.getDuracion(),
                                detalleDialog.getIndicaciones(),detalleDialog.getEstado()
                        );
                    }
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelContenido,
                    "Error al cargar medicamentos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarReceta() {
        if (medicoActual == null) {
            JOptionPane.showMessageDialog(panelContenido,
                    "Error: No hay médico asignado. Por favor, reinicie la sesión.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(panelContenido,
                    "Debe agregar al menos un medicamento a la receta",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(panelContenido,
                "¿Está seguro de que desea guardar esta receta?",
                "Confirmar Guardado",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String fechaRetiro = extraerFechaRetiro((String) fechaRetiroBox.getSelectedItem());

                // Guardar cada medicamento como una receta separada
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    // Generar número único para cada receta
                    String numeroReceta = "REC-" + System.currentTimeMillis() + "-" + String.format("%03d", i);

                    // Obtener datos de la tabla
                    String nombreMedicamento = (String) modeloTabla.getValueAt(i, 0);
                    String presentacion = (String) modeloTabla.getValueAt(i, 1);
                    int cantidad = (Integer) modeloTabla.getValueAt(i, 2);
                    String duracionTexto = (String) modeloTabla.getValueAt(i, 3);
                    int duracion = Integer.parseInt(duracionTexto.replace(" días", ""));
                    String indicaciones = (String) modeloTabla.getValueAt(i, 4);
                    String estadoTexto = (String) modeloTabla.getValueAt(i, 5);
                    PrescriptionState estado = parseEstadoFromString(estadoTexto);

                    // Buscar el medicamento completo para obtener el código
                    String codigoMedicamento = "";
                    try {
                        List<Medicamentos> medicamentos = medicamentosService.LeerTodo();
                        for (Medicamentos med : medicamentos) {
                            if (med.getNombre().equals(nombreMedicamento)) {
                                codigoMedicamento = med.getCodigo();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        codigoMedicamento = "MED-" + i; // Código temporal si no se encuentra
                    }

                    // Llamar al controller para agregar la receta
                    recetaMedicaController.agregar(
                            numeroReceta,           // numeroReceta
                            fechaHoy,              // fechaPrescripcion
                            fechaRetiro,           // fechaRetiro
                            medicoActual.getID(),              // medicoId (deberías obtenerlo del médico logueado)
                            pacienteActual.getID(),        // pacienteId
                            pacienteActual.getNombre(),    // pacienteNombre
                            codigoMedicamento,     // codigoMedicamento
                            nombreMedicamento,     // nombreMedicamento
                            presentacion,          // presentacion
                            cantidad,              // cantidad
                            duracion,              // duracionDias
                            indicaciones,  // indicaciones
                            estado
                    );


                    Thread.sleep(1);
                }

                JOptionPane.showMessageDialog(panelContenido,
                        "Receta(s) guardada(s) exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                limpiarFormulario();

            } catch (Exception e) {
                e.printStackTrace(); // Para debug
                JOptionPane.showMessageDialog(panelContenido,
                        "Error al guardar la receta: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiarFormulario() {
        // Limpiar datos del paciente
        pacienteActual = null;
        nombrePaciente.setText("No seleccionado");
        cedulaPaciente.setText("-");
        telefonoPaciente.setText("-");

        // Limpiar tabla
        modeloTabla.setRowCount(0);

        // Resetear fechas
        cargarFechasRetiro();

        // Deshabilitar botones
        agregarMedicamentoButton.setEnabled(false);
        guardarButton.setEnabled(false);
    }

    private void actualizarDatosPaciente(Paciente paciente) {
        if (paciente != null) {
            nombrePaciente.setText(paciente.getNombre());
            cedulaPaciente.setText(paciente.getID());
            telefonoPaciente.setText(paciente.getNum_Telefono());

            // Cambiar color para indicar que hay paciente seleccionado
            nombrePaciente.setForeground(new Color(46, 125, 50));
        }
    }

    private void cargarFechasRetiro() {
        fechaRetiroBox.removeAllItems();
        String[] fechas = controller.getFechasRetiroDisponibles();
        for (String fecha : fechas) {
            fechaRetiroBox.addItem(fecha);
        }
    }

    private void actualizarFechaActual() {
        String fechaHoy = LocalDate.now().format(
                DateTimeFormatter.ofPattern("'Fecha: 'dd 'de' MMMM 'de' yyyy")
        );
        fechaActualLabel.setText(fechaHoy);
    }

    private String extraerFechaRetiro(String fechaConTexto) {
        // Extraer solo la fecha del texto "dd/MM/yyyy (X días)"
        if (fechaConTexto != null && fechaConTexto.contains("(")) {
            return fechaConTexto.substring(0, fechaConTexto.indexOf("(")).trim();
        }
        return fechaConTexto;
    }

    private void inicializarDatos() {
        actualizarFechaActual();
        cargarFechasRetiro();
    }

    // Métodos públicos para integración
    public JPanel getContentPanel() {
        return panelContenido;
    }

    public void setPacienteSeleccionado(Paciente paciente) {
        this.pacienteActual = paciente;
        actualizarDatosPaciente(paciente);
        agregarMedicamentoButton.setEnabled(paciente != null);
    }

    public Paciente getPacienteActual() {
        return pacienteActual;
    }

    public JTable getTablaRecetaMedica() {
        return tablaRecetaMedica;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }
    public void setMedicoActual(Medico medico) {
        this.medicoActual = medico;
        System.out.println("Médico establecido: " + (medico != null ? medico.getNombre() : "null"));
    }

    public void agregarMedicamentoATabla(Medicamentos medicamento, int cantidad,
                                         int duracion, String indicaciones, PrescriptionState estado) {
        String estadoFormateado = formatStateName(estado);
        Object[] fila = {
                medicamento.getNombre(),
                medicamento.getPresentacion(),
                cantidad,
                duracion + " días",
                indicaciones,estadoFormateado,
                "Eliminar"
        };

        modeloTabla.addRow(fila);
        guardarButton.setEnabled(true);

        // Crear la receta en la lista temporal
        String numeroReceta = "TEMP-" + System.currentTimeMillis();
        String fechaHoy = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String fechaRetiro = extraerFechaRetiro((String) fechaRetiroBox.getSelectedItem());

        RecetaMedica receta = new RecetaMedica(
                numeroReceta, fechaHoy, fechaRetiro, medicoActual.getID(), // ID del médico
                pacienteActual.getID(), pacienteActual.getNombre(),
                medicamento.getCodigo(), medicamento.getNombre(),
                medicamento.getPresentacion(), cantidad, duracion, indicaciones,estado
        );

        medicamentosReceta.add(receta);
    }

    private String formatStateName(PrescriptionState state) {
        switch (state) {
            case PENDING:
                return "Pendiente";
            case PROCESSING:
                return "En Proceso";
            case READY:
                return "Lista";
            case DELIVERED:
                return "Entregada";
            default:
                return state.toString();
        }
    }
    private PrescriptionState parseEstadoFromString(String estadoTexto) {
        switch (estadoTexto) {
            case "Pendiente":
                return PrescriptionState.PENDING;
            case "En Proceso":
                return PrescriptionState.PROCESSING;
            case "Lista":
                return PrescriptionState.READY;
            case "Entregada":
                return PrescriptionState.DELIVERED;
            case "Expirado":
                return PrescriptionState.EXPIRED;
            default:
                return PrescriptionState.PENDING;
        }
    }
    public void eliminarMedicamentoDeLaTabla(int fila) {
        if (fila >= 0 && fila < modeloTabla.getRowCount()) {
            modeloTabla.removeRow(fila);
            medicamentosReceta.remove(fila);

            if (modeloTabla.getRowCount() == 0) {
                guardarButton.setEnabled(false);
            }
        }
    }

}
