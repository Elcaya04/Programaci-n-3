package presentation_layer.Views.FarmaceutaView;

import org.example.domain_layer.Farmaceuta;
import presentation_layer.Controllers.FarmaceutaController;
import presentation_layer.Models.FarmaceutaTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class FarmaceutaView {
    private JTextField ID_Texto;
    private JTextField Nombre_Texto;
    private JButton Borrar_Button;
    private JButton Limpiar_Button;
    private JTextField Nombre_Busqueda_Texto;
    private JButton Buscar_Button;
    private JTable table1;
    private JPanel Panel_Contenido;
    private JButton Motrar_Todos_Button;
    private JButton Agregar_Button;
    private FarmaceutaController controller;
    private FarmaceutaTableModel model;
    public FarmaceutaView(FarmaceutaController controller, FarmaceutaTableModel model, List<Farmaceuta> farmaceutas) {
        this.controller=controller;
        this.model=model;
        addListeners();
        bind(controller,model,farmaceutas);
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
    }

    public void bind(FarmaceutaController controller, FarmaceutaTableModel model, List<Farmaceuta> farmaceutas) {
        this.controller = controller;
        this.model = model;
        table1.setModel(model);
        if (farmaceutas != null) model.setRows(farmaceutas);
        ID_Texto.requestFocus();
    }
    private void agregar() {
        try {
            requireBound();
            DatosForm d = readForm();
            controller.agregar(d.id,d.clave,d.nombre);
            limpiar();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }

    }

    private void Actualizar() {
        try {
            requireBound();
            DatosForm d = readForm();
            controller.actualizar(d.id,d.clave,d.nombre);
            limpiar();
        } catch (Exception ex) {
            showError("Error al actualizar: " + ex.getMessage(), ex);
        }
    }
    private void mostrarTodos() {
        try {
            requireBound();
            List<Farmaceuta> todosFarmaceutas = controller.leerTodos();
            model.setRows(todosFarmaceutas);
            Nombre_Busqueda_Texto.setText("");
            Motrar_Todos_Button.setVisible(false);
        } catch (Exception e) {
            showError("Error al cargar Farmaceutas: " + e.getMessage(), e);
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
                    "¿Quiere buscar el farmaceuta " + Nombre + "?", "Confirmar",
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
             String id = ID_Texto.getText();
            if (id == null || id.length() <= 3) {
                warn("ID inválido.");
                return;
            }
            int op = JOptionPane.showConfirmDialog(Panel_Contenido,
                    "¿Eliminar Farmaceuta con ID " + id + "?", "Confirmar",
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
        ID_Texto.setText("");
        Nombre_Texto.setText("");
        ID_Texto.requestFocus();
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (model== null) return;
        int row = table1.getSelectedRow();
        if (row < 0) return;
        Farmaceuta c = model.getAt(row);
        if (c == null) return;

        ID_Texto.setText(String.valueOf(c.getID()));
        Nombre_Texto.setText(String.valueOf(c.getNombre()));

    }

    private static class DatosForm {
        String id;
        String nombre;
        String clave;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.id= safe(ID_Texto.getText());
        d.nombre= safe(Nombre_Texto.getText());
        if (d.id.length() <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
        if (d.nombre.isEmpty()) throw new IllegalArgumentException("El nombre es obligatorio.");
        return d;
    }


    private void requireBound() {
        if (controller == null || model == null)
            throw new IllegalStateException("Farmaceuta no está enlazado (bind) a controller/model.");
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

