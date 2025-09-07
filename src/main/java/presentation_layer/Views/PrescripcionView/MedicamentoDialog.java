package presentation_layer.Views.PrescripcionView;

import org.example.domain_layer.Medicamentos;
import presentation_layer.Controllers.MedicamentosController;
import presentation_layer.Models.MedicamentoTableModel;
import presentation_layer.Views.MedicamentosView.MedicamentosView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MedicamentoDialog extends Dialog {
    private MedicamentosView medicamentosView;
    private Medicamentos medicamentoSeleccionado;
    private boolean confirmado = false;

    public MedicamentoDialog(JFrame parent, MedicamentosController controller,
                             MedicamentoTableModel model, List<Medicamentos> medicamentos) {
        super(parent, "Seleccionar Medicamento", true);

        // Crear la vista de medicamentos
        medicamentosView = new MedicamentosView(controller, model, medicamentos);

        inicializarComponentes();
        configurarEventos();

        setSize(800, 600);
        setLocationRelativeTo(parent);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel superior con título
        JPanel panelTitulo = new JPanel();
        JLabel titulo = new JLabel("Seleccione un medicamento de la lista");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(titulo);

        // Panel central con la vista de medicamentos
        JPanel panelCentral = medicamentosView.getContentPanel();

        // Panel inferior con botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnSeleccionar = new JButton("Seleccionar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSeleccionar.setBackground(new Color(76, 175, 80));
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
        if (medicamentosView != null) {
            JTable tabla = getTablaFromMedicamentosView();
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

    private JTable getTablaFromMedicamentosView() {
        return findJTable(medicamentosView.getContentPanel());
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
        medicamentoSeleccionado = medicamentosView.getMedicamentoSeleccionado();
        if (medicamentoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un medicamento de la tabla",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmado = true;
        dispose();
    }

    private void cancelar() {
        confirmado = false;
        medicamentoSeleccionado = null;
        dispose();
    }

    public Medicamentos getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public boolean isConfirmado() {
        return confirmado;
    }


}
