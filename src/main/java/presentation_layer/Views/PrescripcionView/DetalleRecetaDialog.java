package presentation_layer.Views.PrescripcionView;

import org.example.domain_layer.Medicamentos;
import org.example.domain_layer.Paciente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DetalleRecetaDialog extends JDialog {
    private JTextField txtCantidad;
    private JTextField txtDuracion;
    private JTextArea txtIndicaciones;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private boolean confirmado = false;
    private int cantidad;
    private int duracion;
    private String indicaciones;

    private Paciente paciente;
    private Medicamentos medicamento;

    public DetalleRecetaDialog(JFrame parent, Paciente paciente, Medicamentos medicamento) {
        super(parent, "Detalles de la Receta", true);
        this.paciente = paciente;
        this.medicamento = medicamento;

        inicializarComponentes();
        configurarEventos();
        configurarVentana();
        cargarDatos();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel de información del paciente y medicamento
        JPanel panelInfo = createInfoPanel();

        // Panel de formulario
        JPanel panelFormulario = createFormPanel();

        // Panel de botones
        JPanel panelBotones = createButtonPanel();

        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Información de la Receta"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Información del paciente
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        JLabel lblPaciente = new JLabel(paciente.getNombre());
        lblPaciente.setFont(lblPaciente.getFont().deriveFont(Font.BOLD));
        panel.add(lblPaciente, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(paciente.getID()), gbc);

        // Información del medicamento
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Medicamento:"), gbc);
        gbc.gridx = 1;
        JLabel lblMedicamento = new JLabel(medicamento.getNombre());
        lblMedicamento.setFont(lblMedicamento.getFont().deriveFont(Font.BOLD));
        panel.add(lblMedicamento, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Presentación:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(medicamento.getPresentacion()), gbc);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalles de la Prescripción"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCantidad = new JTextField(10);
        txtCantidad.setToolTipText("Ingrese la cantidad de unidades (ej: 30 tabletas)");
        panel.add(txtCantidad, gbc);

        // Duración
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Duración (días):"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtDuracion = new JTextField(10);
        txtDuracion.setToolTipText("Ingrese la duración del tratamiento en días");
        panel.add(txtDuracion, gbc);

        // Indicaciones
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Indicaciones:"), gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        txtIndicaciones = new JTextArea(5, 30);
        txtIndicaciones.setWrapStyleWord(true);
        txtIndicaciones.setLineWrap(true);
        txtIndicaciones.setToolTipText("Escriba las instrucciones para el paciente");

        JScrollPane scrollPane = new JScrollPane(txtIndicaciones);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());

        btnGuardar = new JButton("Agregar a Receta");
        btnGuardar.setPreferredSize(new Dimension(150, 35));
        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.setBackground(new Color(211, 47, 47));
        btnCancelar.setForeground(Color.WHITE);

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        return panel;
    }

    private void configurarEventos() {
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validarDatos()) {
                    confirmado = true;
                    dispose();
                }
            }
        });

        btnCancelar.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        // Validación en tiempo real para campos numéricos
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });

        txtDuracion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });
    }

    private boolean validarDatos() {
        try {
            // Validar cantidad
            String cantidadStr = txtCantidad.getText().trim();
            if (cantidadStr.isEmpty()) {
                mostrarError("La cantidad es obligatoria");
                txtCantidad.requestFocus();
                return false;
            }

            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a 0");
                txtCantidad.requestFocus();
                return false;
            }

            // Validar duración
            String duracionStr = txtDuracion.getText().trim();
            if (duracionStr.isEmpty()) {
                mostrarError("La duración es obligatoria");
                txtDuracion.requestFocus();
                return false;
            }

            duracion = Integer.parseInt(duracionStr);
            if (duracion <= 0) {
                mostrarError("La duración debe ser mayor a 0");
                txtDuracion.requestFocus();
                return false;
            }

            // Validar indicaciones
            indicaciones = txtIndicaciones.getText().trim();
            if (indicaciones.isEmpty()) {
                mostrarError("Las indicaciones son obligatorias");
                txtIndicaciones.requestFocus();
                return false;
            }

            return true;

        } catch (NumberFormatException e) {
            mostrarError("La cantidad y duración deben ser números válidos");
            return false;
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
    }

    private void configurarVentana() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void cargarDatos() {
        // Valores por defecto
        txtCantidad.setText("30");
        txtDuracion.setText("7");
        txtIndicaciones.setText("Tomar 1 " + medicamento.getPresentacion().toLowerCase() +
                " cada 8 horas después de las comidas.");
    }

    // Getters
    public boolean isConfirmado() {
        return confirmado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }
}
