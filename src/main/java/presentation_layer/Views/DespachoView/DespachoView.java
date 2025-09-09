package presentation_layer.Views.DespachoView;

import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.RecetaMedica;
import presentation_layer.Controllers.DespachoController;
import utilites.PrescriptionState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DespachoView {
    private JPanel panelContenido;
    private DespachoController controller;
    private Farmaceuta farmaceutaActual;

    // Componentes de búsqueda
    private JTextField txtCedulaPaciente;
    private JButton btnBuscar;
    private JButton btnMostrarTodas;

    // Tabla de recetas
    private JTable tablaRecetas;
    private DefaultTableModel modeloTabla;

    // Botones de acción
    private JButton btnPonerEnProceso;
    private JButton btnMarcarLista;
    private JButton btnEntregar;
    private JButton btnRefrescar;

    // Labels de estadísticas
    private JLabel lblPendientes;
    private JLabel lblEnProceso;
    private JLabel lblListas;
    private JLabel lblEntregadas;

    // Panel de información del paciente
    private JLabel lblInfoPaciente;

    public DespachoView(DespachoController controller, Farmaceuta farmaceuta) {
        this.controller = controller;
        this.farmaceutaActual = farmaceuta;
        inicializarComponentes();
        configurarEventos();
        actualizarEstadisticas();
        SwingUtilities.invokeLater(() -> {
            mostrarTodasLasRecetas();
            actualizarEstadisticas();
        });
    }

    private void cargarRecetasEnTabla(List<RecetaMedica> recetas) {
        modeloTabla.setRowCount(0);

        for (RecetaMedica receta : recetas) {
            Object[] fila = {
                    receta.getNumeroReceta(),
                    receta.getPacienteNombre(),
                    receta.getNombreMedicamento(),
                    receta.getCantidad() + " " + receta.getPresentacion(),
                    receta.getFechaPrescripcion(),
                    receta.getFechaRetiro(),
                    DespachoController.formatearEstado(receta.getEstado()),
                    receta.getIndicaciones()
            };
            modeloTabla.addRow(fila);
        }

        actualizarBotonesSegunSeleccion();
    }

    private void actualizarBotonesSegunSeleccion() {
        int filaSeleccionada = tablaRecetas.getSelectedRow();

        if (filaSeleccionada == -1) {
            btnPonerEnProceso.setEnabled(false);
            btnMarcarLista.setEnabled(false);
            btnEntregar.setEnabled(false);
            return;
        }

        String estadoTexto = (String) modeloTabla.getValueAt(filaSeleccionada, 6);
        PrescriptionState estado = DespachoController.parsearEstado(estadoTexto);

        // Habilitar botones según el estado actual
        btnPonerEnProceso.setEnabled(estado == PrescriptionState.PENDING);
        btnMarcarLista.setEnabled(estado == PrescriptionState.PROCESSING);
        btnEntregar.setEnabled(estado == PrescriptionState.READY);
    }

    private void actualizarEstadisticas() {
        try {
            System.out.println("Actualizando estadísticas...");
            DespachoController.EstadisticasDespacho stats = controller.obtenerEstadisticas();

            lblPendientes.setText("<html><center><b>Pendientes</b><br>" +
                    "<font size='6' color='#FFC107'>" + stats.getPendientes() + "</font></center></html>");

            lblEnProceso.setText("<html><center><b>En Proceso</b><br>" +
                    "<font size='6' color='#2196F3'>" + stats.getEnProceso() + "</font></center></html>");

            lblListas.setText("<html><center><b>Listas</b><br>" +
                    "<font size='6' color='#4CAF50'>" + stats.getListas() + "</font></center></html>");

            lblEntregadas.setText("<html><center><b>Entregadas</b><br>" +
                    "<font size='6' color='#8BC34A'>" + stats.getEntregadas() + "</font></center></html>");
            System.out.println("Estadísticas actualizadas correctamente");
        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos públicos para integración
    public JPanel getContentPanel() {
        return panelContenido;
    }

    public void setFarmaceutaActual(Farmaceuta farmaceuta) {
        this.farmaceutaActual = farmaceuta;
        SwingUtilities.invokeLater(() -> {
            if (panelContenido != null) {
                actualizarLabelFarmaceuta();
            }
        });
    }
    private void actualizarLabelFarmaceuta() {


        String nombreFarmaceuta = (farmaceutaActual != null && farmaceutaActual.getNombre() != null)
                ? farmaceutaActual.getNombre()
                : "No definido";

        System.out.println("Farmaceuta actualizado: " + nombreFarmaceuta);


    }

    private void inicializarComponentes() {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel superior - Título y búsqueda
        JPanel panelSuperior = createPanelSuperior();

        // Panel central - Tabla y estadísticas
        JPanel panelCentral = createPanelCentral();

        // Panel inferior - Botones
        JPanel panelInferior = createPanelBotones();

        panelContenido.add(panelSuperior, BorderLayout.NORTH);
        panelContenido.add(panelCentral, BorderLayout.CENTER);
        panelContenido.add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel createPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Despacho de Recetas Médicas");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel de búsqueda
        JPanel panelBusqueda = createPanelBusqueda();

        // Panel de información del farmaceuta
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblFarmaceuta = new JLabel("Farmaceuta: " +
                (farmaceutaActual != null ? farmaceutaActual.getNombre() : "No definido"));
        lblFarmaceuta.setFont(new Font("Arial", Font.ITALIC, 12));
        panelInfo.add(lblFarmaceuta);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(panelBusqueda, BorderLayout.CENTER);
        panel.add(panelInfo, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));

        return panel;
    }

    private JPanel createPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Buscar Recetas"));

        JPanel panelCampos=new JPanel(new FlowLayout());
        panel.add(new JLabel("Cédula del Paciente:"));

        txtCedulaPaciente = new JTextField(15);
        panelCampos.add(txtCedulaPaciente);

        btnBuscar = new JButton("Buscar Recetas");
        btnBuscar.setBackground(new Color(33, 150, 243));
        btnBuscar.setForeground(Color.WHITE);
        panelCampos.add(btnBuscar);

        btnMostrarTodas = new JButton("Mostrar Todas");
        btnMostrarTodas.setBackground(new Color(76, 175, 80));
        btnMostrarTodas.setForeground(Color.WHITE);
        panelCampos.add(btnMostrarTodas);

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Panel de información del paciente
        lblInfoPaciente = new JLabel("Presione 'Mostrar Todas' para cargar recetas o ingrese cédula para buscar");
        lblInfoPaciente.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfoPaciente.setForeground(new Color(100, 100, 100)); // Color más visible
        panelInfo.add(lblInfoPaciente);

        panel.add(panelCampos, BorderLayout.NORTH);
        panel.add(panelInfo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de estadísticas
        JPanel panelEstadisticas = createPanelEstadisticas();

        // Panel de tabla
        JPanel panelTabla = createPanelTabla();

        panel.add(panelEstadisticas, BorderLayout.NORTH);
        panel.add(panelTabla, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBorder(new TitledBorder("Estadísticas de Despacho"));

        lblPendientes = createEstadisticaLabel("Pendientes", "0", new Color(255, 193, 7));
        lblEnProceso = createEstadisticaLabel("En Proceso", "0", new Color(33, 150, 243));
        lblListas = createEstadisticaLabel("Listas", "0", new Color(76, 175, 80));
        lblEntregadas = createEstadisticaLabel("Entregadas", "0", new Color(139, 195, 74));

        panel.add(createEstadisticaPanel(lblPendientes));
        panel.add(createEstadisticaPanel(lblEnProceso));
        panel.add(createEstadisticaPanel(lblListas));
        panel.add(createEstadisticaPanel(lblEntregadas));

        return panel;
    }

    private JLabel createEstadisticaLabel(String titulo, String valor, Color color) {
        JLabel label = new JLabel("<html><center><b>" + titulo + "</b><br>" +
                "<font size='6' color='" + String.format("#%06X", color.getRGB() & 0xFFFFFF) + "'>" +
                valor + "</font></center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createEstadisticaPanel(JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.setBackground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Recetas"));

        // Crear modelo de tabla
        String[] columnas = {
                "N° Receta", "Paciente", "Medicamento", "Cantidad",
                "Fecha Prescripción", "Fecha Retiro", "Estado", "Indicaciones"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRecetas = new JTable(modeloTabla);
        tablaRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaRecetas.setRowHeight(25);

        // Configurar anchos de columnas
        tablaRecetas.getColumnModel().getColumn(0).setPreferredWidth(120); // N° Receta
        tablaRecetas.getColumnModel().getColumn(1).setPreferredWidth(120); // Paciente
        tablaRecetas.getColumnModel().getColumn(2).setPreferredWidth(150); // Medicamento
        tablaRecetas.getColumnModel().getColumn(3).setPreferredWidth(80);  // Cantidad
        tablaRecetas.getColumnModel().getColumn(4).setPreferredWidth(100); // Fecha Prescripción
        tablaRecetas.getColumnModel().getColumn(5).setPreferredWidth(100); // Fecha Retiro
        tablaRecetas.getColumnModel().getColumn(6).setPreferredWidth(100); // Estado
        tablaRecetas.getColumnModel().getColumn(7).setPreferredWidth(200); // Indicaciones

        JScrollPane scrollPane = new JScrollPane(tablaRecetas);
        scrollPane.setPreferredSize(new Dimension(0, 350));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout());

        btnPonerEnProceso = new JButton("Poner en Proceso");
        btnPonerEnProceso.setBackground(new Color(255, 152, 0));
        btnPonerEnProceso.setForeground(Color.WHITE);
        btnPonerEnProceso.setEnabled(false);

        btnMarcarLista = new JButton("Marcar como Lista");
        btnMarcarLista.setBackground(new Color(76, 175, 80));
        btnMarcarLista.setForeground(Color.WHITE);
        btnMarcarLista.setEnabled(false);

        btnEntregar = new JButton("Entregar Receta");
        btnEntregar.setBackground(new Color(46, 125, 50));
        btnEntregar.setForeground(Color.WHITE);
        btnEntregar.setEnabled(false);

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setBackground(new Color(96, 125, 139));
        btnRefrescar.setForeground(Color.WHITE);

        panel.add(btnPonerEnProceso);
        panel.add(btnMarcarLista);
        panel.add(btnEntregar);
        panel.add(btnRefrescar);

        return panel;
    }

    private void configurarEventos() {
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });

        btnMostrarTodas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTodasLasRecetas();
            }
        });

        btnPonerEnProceso.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ponerEnProceso();
            }
        });

        btnMarcarLista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                marcarComoLista();
            }
        });

        btnEntregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entregarReceta();
            }
        });

        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescar();
            }
        });

        // Event listener para la selección de tabla
        tablaRecetas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarBotonesSegunSeleccion();
            }
        });

        // Enter en campo de texto para buscar
        txtCedulaPaciente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarRecetas();
            }
        });
    }

    private void buscarRecetas() {
        String cedula = txtCedulaPaciente.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(panelContenido,
                    "Por favor ingrese una cédula para buscar",
                    "Campo Requerido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            System.out.println("Buscando recetas para cédula: " + cedula);
            // Buscar recetas pendientes y en proceso
            List<RecetaMedica> recetasPendientes = controller.buscarRecetasParaDespacho(cedula);
            List<RecetaMedica> recetasEnProceso = controller.buscarRecetasEnProceso(cedula);
            List<RecetaMedica> recetasListas = controller.buscarRecetasListas(cedula);
            System.out.println("Recetas pendientes: " + recetasPendientes.size());
            System.out.println("Recetas en proceso: " + recetasEnProceso.size());
            System.out.println("Recetas listas: " + recetasListas.size());
            // Combinar listas
            recetasPendientes.addAll(recetasEnProceso);

            cargarRecetasEnTabla(recetasPendientes);

            if (recetasPendientes.isEmpty()) {
                lblInfoPaciente.setText("No se encontraron recetas disponibles para: " + cedula);
            } else {
                lblInfoPaciente.setText("Se encontraron " + recetasPendientes.size() +
                        " receta(s) para el paciente: " + cedula);
            }

            actualizarEstadisticas();

        } catch (Exception e) {
            System.err.println("Error al buscar recetas: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelContenido,
                    "Error al buscar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarTodasLasRecetas() {
        try {
            System.out.println("Cargando todas las recetas...");

            List<RecetaMedica> pendientes = controller.obtenerRecetasPorEstado(PrescriptionState.PENDING);
            List<RecetaMedica> enProceso = controller.obtenerRecetasPorEstado(PrescriptionState.PROCESSING);
            List<RecetaMedica> listas = controller.obtenerRecetasPorEstado(PrescriptionState.READY);

            System.out.println("Recetas por estado - P:" + pendientes.size() + ", EP:" + enProceso.size() + ", L:" + listas.size());

            // Combinar todas las listas
            pendientes.addAll(enProceso);
            pendientes.addAll(listas);

            cargarRecetasEnTabla(pendientes);

            lblInfoPaciente.setText("Mostrando todas las recetas disponibles para despacho (" + pendientes.size() + " recetas)");
            txtCedulaPaciente.setText("");

            actualizarEstadisticas();

        } catch (Exception e) {
            System.err.println("Error al cargar recetas: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelContenido,
                    "Error al cargar recetas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            lblInfoPaciente.setText("Error al cargar las recetas: " + e.getMessage());
        }
    }

    private void ponerEnProceso() {
        int filaSeleccionada = tablaRecetas.getSelectedRow();
        if (filaSeleccionada == -1) return;

        String numeroReceta = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(panelContenido,
                "¿Confirma que desea poner en proceso la receta " + numeroReceta + "?",
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.ponerEnProceso(numeroReceta, farmaceutaActual.getID());

                JOptionPane.showMessageDialog(panelContenido,
                        "Receta puesta en proceso exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                refrescar();
                actualizarEstadisticas();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelContenido,
                        "Error al poner en proceso: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void marcarComoLista() {
        int filaSeleccionada = tablaRecetas.getSelectedRow();
        if (filaSeleccionada == -1) return;

        String numeroReceta = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(panelContenido,
                "¿Confirma que los medicamentos están listos para la receta " + numeroReceta + "?",
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.marcarComoLista(numeroReceta, farmaceutaActual.getID());

                JOptionPane.showMessageDialog(panelContenido,
                        "Receta marcada como lista exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                refrescar();
                actualizarEstadisticas();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelContenido,
                        "Error al marcar como lista: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void entregarReceta() {
        int filaSeleccionada = tablaRecetas.getSelectedRow();
        if (filaSeleccionada == -1) return;

        String numeroReceta = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String paciente = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(panelContenido,
                "¿Confirma la entrega de la receta " + numeroReceta + " al paciente " + paciente + "?",
                "Confirmar Entrega",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                controller.entregar(numeroReceta, farmaceutaActual.getID());

                JOptionPane.showMessageDialog(panelContenido,
                        "Receta entregada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                refrescar();
                actualizarEstadisticas();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(panelContenido,
                        "Error al entregar receta: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refrescar() {
        String cedulaActual = txtCedulaPaciente.getText().trim();

        if (!cedulaActual.isEmpty()) {
            buscarRecetas();
        } else {
            mostrarTodasLasRecetas();
        }
actualizarEstadisticas();

    }
}
