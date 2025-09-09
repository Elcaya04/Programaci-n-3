package presentation_layer.Views.PrescripcionView;

import org.example.domain_layer.Paciente;
import presentation_layer.Models.PacienteTableModel;
import presentation_layer.Views.PacienteView.PacienteView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PacienteDialog extends Dialog {
    private PacienteView pacienteView;
    private Paciente pacienteSeleccionado;
    private boolean confirmado = false;

    public PacienteDialog(JFrame parent, PacienteController controller,
                                   PacienteTableModel model, List<Paciente> pacientes) {
        super(parent, "Seleccionar Paciente", true);

        // Crear la vista de pacientes
        pacienteView = new PacienteView(controller, model, pacientes);

        inicializarComponentes();
        configurarEventos();

        setSize(800, 600);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        JLabel titulo = new JLabel("Seleccione un paciente de la lista");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(titulo);

        // Panel central con la vista de pacientes
        JPanel panelCentral = pacienteView.getContentPanel();

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSeleccionar.setBackground(new Color(33, 150, 243));
        btnSeleccionar.setForeground(Color.WHITE);
        btnCancelar.setBackground(new Color(244, 67, 54));
        btnCancelar.setForeground(Color.WHITE);

        panelBotones.add(btnSeleccionar);
        panelBotones.add(btnCancelar);

        // Agregar componentes
        add(panelTitulo, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos de botones
        btnSeleccionar.addActionListener(e -> seleccionar());
        btnCancelar.addActionListener(e -> cancelar());
    }

    private void configurarEventos() {
        // Doble clic en tabla para seleccionar
        if (pacienteView != null) {
            JTable tabla = getTablaFromPacienteView();
            if (tabla != null) {
                tabla.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            seleccionar();
                        }
                    }
                });
            }
        }
    }

    private JTable getTablaFromPacienteView() {
        // Buscar la tabla en los componentes de la vista
        return findJTable(pacienteView.getContentPanel());
    }

    private JTable findJTable(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTable) {
                return (JTable) component;
            } else if (component instanceof Container) {
                JTable found = findJTable((Container) component);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void seleccionar() {
        pacienteSeleccionado = pacienteView.getPacienteSeleccionado();
        if (pacienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un paciente de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmado = true;
        dispose();
    }

    private void cancelar() {
        confirmado = false;
        pacienteSeleccionado = null;
        dispose();
    }

    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

}
