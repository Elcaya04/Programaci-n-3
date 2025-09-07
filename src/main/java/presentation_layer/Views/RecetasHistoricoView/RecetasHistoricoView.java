package presentation_layer.Views.RecetasHistoricoView;

import org.example.domain_layer.RecetaMedica;
import presentation_layer.Controllers.RecetaMedicaController;
import presentation_layer.Models.RecetaMedicaTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class RecetasHistoricoView {
    private JTable Tablal_Historico_Recetas;
    private JPanel Panel_Contenido;
    private RecetaMedicaController controller;
    private RecetaMedicaTableModel tableModel;

    public RecetasHistoricoView(RecetaMedicaController controller,
                                RecetaMedicaTableModel tableModel,
                                List<RecetaMedica> recetas) {
        this.controller = controller;
        this.tableModel = tableModel;
        inicializarComponentes();
        configurarTabla();
        cargarDatos(recetas);
    }

    private void inicializarComponentes() {
        Panel_Contenido = new JPanel(new BorderLayout());
        Panel_Contenido.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titulo = new JLabel("Histórico de Recetas Médicas");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        Panel_Contenido.add(titulo, BorderLayout.NORTH);

        // Crear tabla
        Tablal_Historico_Recetas = new JTable(tableModel);
        configurarTabla();

        // ScrollPane para la tabla
        JScrollPane scrollPane = new JScrollPane(Tablal_Historico_Recetas);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        Panel_Contenido.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> refrescarDatos());

        JButton btnEliminar = new JButton("Eliminar Seleccionada");
        btnEliminar.addActionListener(e -> eliminarRecetaSeleccionada());

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);

        Panel_Contenido.add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarTabla() {
        // Configuración general de la tabla
        Tablal_Historico_Recetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Tablal_Historico_Recetas.setRowHeight(25);
        Tablal_Historico_Recetas.getTableHeader().setReorderingAllowed(false);

        // Ajustar anchos de columnas
        TableColumnModel columnModel = Tablal_Historico_Recetas.getColumnModel();

        // Anchos específicos para cada columna
        int[] columnWidths = {100, 120, 100, 80, 150, 200, 80, 100, 200};

        for (int i = 0; i < columnWidths.length && i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Hacer que la tabla se ajuste automáticamente
        Tablal_Historico_Recetas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    private void cargarDatos(List<RecetaMedica> recetas) {
        if (recetas != null) {
            tableModel.setRows(recetas);
        }
    }

    private void refrescarDatos() {
        try {
            List<RecetaMedica> recetas = controller.leerTodos();
            tableModel.setRows(recetas);
            JOptionPane.showMessageDialog(Panel_Contenido,
                    "Datos actualizados correctamente",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Panel_Contenido,
                    "Error al cargar los datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRecetaSeleccionada() {
        int filaSeleccionada = Tablal_Historico_Recetas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(Panel_Contenido,
                    "Por favor seleccione una receta para eliminar",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        RecetaMedica recetaSeleccionada = tableModel.getAt(filaSeleccionada);
        if (recetaSeleccionada == null) return;

        int opcion = JOptionPane.showConfirmDialog(Panel_Contenido,
                "¿Está seguro de que desea eliminar la receta " +
                        recetaSeleccionada.getNumeroReceta() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                controller.borrar(recetaSeleccionada.getNumeroReceta());
                JOptionPane.showMessageDialog(Panel_Contenido,
                        "Receta eliminada correctamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Panel_Contenido,
                        "Error al eliminar la receta: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JPanel getContentPanel() {
        return Panel_Contenido;
    }

    public JTable getTabla() {
        return Tablal_Historico_Recetas;
    }
}
