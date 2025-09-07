package presentation_layer.Views.MedicamentosView;

import org.example.domain_layer.Medicamentos;
import org.example.domain_layer.Medico;
import presentation_layer.Controllers.MedicamentosController;
import presentation_layer.Controllers.MedicoController;
import presentation_layer.Models.MedicamentoTableModel;
import presentation_layer.Models.MedicoTableModel;
import utilites.PDF;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MedicamentosView {
    private JTextField Codigo_Texto;
    private JTextField Nombre_Texto;
    private JTextField Presentacion_Texto;
    private JButton Agregar_Button;
    private JButton Borrar_Button;
    private JButton Limpiar_Button;
    private JTextField Nombre_Busqueda_Texto;
    private JButton Buscar_Button;
    private JTable table1;
    private JPanel Panel_Contenido;
    private JButton Motrar_Todos_Button;
    private JButton Actualizar_Button;
    private MedicamentosController controller;
    private MedicamentoTableModel model;
    public MedicamentosView(MedicamentosController controller, MedicamentoTableModel model, List<Medicamentos> medicamentos) {
        this.controller=controller;
        this.model=model;
        addListeners();
        bind(controller,model,medicamentos);
    }
    public JPanel getContentPanel() {
        return Panel_Contenido;
    }
    private void addListeners() {
        Agregar_Button.addActionListener(e -> agregar());
        Borrar_Button.addActionListener(e -> Eliminar());
        Limpiar_Button.addActionListener(e -> limpiar());
        Buscar_Button.addActionListener(e ->Buscar());
        Motrar_Todos_Button.addActionListener(e -> mostrarTodos());
        table1.getSelectionModel().addListSelectionListener(this::onTableSelection);
        addHoverListener(Agregar_Button);
        addHoverListener(Borrar_Button);
        addHoverListener(Limpiar_Button);
        addHoverListener(Motrar_Todos_Button);
        addHoverListener(Buscar_Button);
    }
    private void addHoverListener(JButton hover_Button) {
        hover_Button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hover_Button.setBackground(Color.LIGHT_GRAY);
                hover_Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hover_Button.setBackground(UIManager.getColor("Button.background"));
            }
        });
    }
    public void bind(MedicamentosController controller, MedicamentoTableModel model, List<Medicamentos> medicamentos) {
        this.controller = controller;
        this.model = model;
        table1.setModel(model);
        if (medicamentos != null) model.setRows(medicamentos);
        Codigo_Texto.requestFocus();
    }
    private void agregar() {
        try {
            requireBound();
            DatosForm d = readForm();
            controller.agregar(d.codigo, d.nombre, d.presentacion);
            limpiar();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }

    }

    private void Actualizar() {
        try {
            requireBound();
            DatosForm d = readForm();
            controller.actualizar(d.codigo, d.nombre, d.presentacion);
            limpiar();
        } catch (Exception ex) {
            showError("Error al actualizar: " + ex.getMessage(), ex);
        }
    }
    private void mostrarTodos() {
        try {
            requireBound();
            List<Medicamentos> todosMedicamentos = controller.leerTodos();
            model.setRows(todosMedicamentos);
            Nombre_Busqueda_Texto.setText("");
            Motrar_Todos_Button.setVisible(false);
        } catch (Exception e) {
            showError("Error al cargar médicos: " + e.getMessage(), e);
        }
    }
private void Buscar() {
        try {
            requireBound();
            String Nombre = Nombre_Busqueda_Texto.getText();
            if(Nombre.isEmpty()||Nombre.length()<=3){
                warn("Nombre no puede estar vacio o ser muy corto");
                return;
            }
            int op = JOptionPane.showConfirmDialog(Panel_Contenido,
                    "¿Quiere buscar el medicamento " + Nombre + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                controller.Buscar(Nombre);
                Motrar_Todos_Button.setVisible(true);
                limpiar();
            }
        } catch (Exception e) {
            showError("Error al buscar: " + e.getMessage(),e);
        }
}
    private void Eliminar() {
        try {
            requireBound();
             String id = Codigo_Texto.getText();
            if (id == null || id.length() <= 3) {
                warn("Codigo Invalido.");
                return;
            }
            int op = JOptionPane.showConfirmDialog(Panel_Contenido,
                    "¿Eliminar Medicamento con codigo" + id + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                controller.borrar(id);
                limpiar();
            }
        } catch (Exception ex) {
            showError("Error al borrar: " + ex.getMessage(), ex);
        }
    }

    private void limpiar() {
        table1.clearSelection();
        Codigo_Texto.setText("");
        Nombre_Texto.setText("");
        Presentacion_Texto.setText("");
        Codigo_Texto.requestFocus();
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (model== null) return;
        int row = table1.getSelectedRow();
        if (row < 0) return;
        Medicamentos c = model.getAt(row);
        if (c == null) return;

        Codigo_Texto.setText(String.valueOf(c.getCodigo()));
        Nombre_Texto.setText(String.valueOf(c.getNombre()));
        Presentacion_Texto.setText(String.valueOf(c.getPresentacion()));

    }

    private static class DatosForm {
        String codigo;
        String nombre;
        String presentacion;

    }
    public Medicamentos getMedicamentoSeleccionado() {
        int row = table1.getSelectedRow();
        if (row >= 0) {
            return model.getAt(row);
        }
        return null;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.codigo= safe(Codigo_Texto.getText());
        d.nombre= safe(Nombre_Texto.getText());
        d.presentacion  = safe(Presentacion_Texto.getText());
        if (d.codigo.length() <= 0) throw new IllegalArgumentException("El codigo debe ser mayor que 0.");
        if (d.nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        return d;
    }


    private void requireBound() {
        if (controller == null || model == null)
            throw new IllegalStateException("Medico no está enlazado (bind) a controller/model.");
    }

    private String safe(String s) { return s == null ? "" : s.trim(); }

    private void showError(String msg, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(Panel_Contenido, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void warn(String msg) {
        JOptionPane.showMessageDialog(Panel_Contenido, msg, "Atención", JOptionPane.WARNING_MESSAGE);
    }
}

