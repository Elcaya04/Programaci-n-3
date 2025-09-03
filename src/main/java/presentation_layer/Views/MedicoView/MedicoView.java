package presentation_layer.Views.MedicoView;

import org.example.domain_layer.Medico;
import presentation_layer.Controllers.MedicoController;
import presentation_layer.Models.MedicoTableModel;
import utilites.PDF;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.List;

public class MedicoView {
    private JTextField ID_Texto;
    private JTextField Nombre_Texto;
    private JTextField Especialidad_Texto;
    private JButton Agregar_Button;
    private JButton Borrar_Button;
    private JButton Limpiar_Button;
    private JTextField Nombre_Busqueda_Texto;
    private JButton Buscar_Button;
    private JButton Reporte_Button;
    private JTable table1;
    private JPanel Panel_Contenido;
    private JButton Motrar_Todos_Button;
    private JButton Agregar_Button_F;
    private MedicoController controller;
    private MedicoTableModel model;
    public MedicoView(MedicoController controller, MedicoTableModel model, List<Medico> medicos) {
        this.controller=controller;
        this.model=model;
        addListeners();
        bind(controller,model,medicos);
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
        Reporte_Button.addActionListener(e -> generarPDF());
        table1.getSelectionModel().addListSelectionListener(this::onTableSelection);
        addHoverListener(Agregar_Button);
        addHoverListener(Borrar_Button);
        addHoverListener(Limpiar_Button);
        addHoverListener(Motrar_Todos_Button);
        addHoverListener(Reporte_Button);
        addHoverListener(Buscar_Button);
    }
    private void addHoverListener(JButton hover_Button) {
        hover_Button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hover_Button.setBackground(Color.LIGHT_GRAY);
                hover_Button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                hover_Button.setBackground(UIManager.getColor("Button.background"));
            }
        });
    }
    public void bind(MedicoController controller, MedicoTableModel model, List<Medico> medicos) {
        this.controller = controller;
        this.model = model;
        table1.setModel(model);
        if (medicos != null) model.setRows(medicos);
        ID_Texto.requestFocus();
    }
    private void agregar() {
        try {
            requireBound();
            DatosForm d = readForm();
            controller.agregar(d.id, d.nombre, d.especialidad,d.clave);
            limpiar();
        } catch (Exception ex) {
            showError("Error al agregar: " + ex.getMessage(), ex);
        }

    }

    private void Actualizar() {
        try {
            requireBound();
            DatosForm d = readForm();
            controller.actualizar(d.id, d.nombre, d.especialidad,d.clave);
            limpiar();
        } catch (Exception ex) {
            showError("Error al actualizar: " + ex.getMessage(), ex);
        }
    }
    private void generarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte");
        fileChooser.setSelectedFile(new java.io.File("reporte_medicos.pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                PDF.exportTable(table1, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "PDF generado con éxito!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al generar PDF: " + ex.getMessage());
            }
        }
    }
    private void mostrarTodos() {
        try {
            requireBound();
            List<Medico> todosMedicos = controller.leerTodos();
            model.setRows(todosMedicos);
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
                    "¿Quiere buscar el medico " + Nombre + "?", "Confirmar",
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
                    "¿Eliminar Medico con ID " + id + "?", "Confirmar",
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
        Especialidad_Texto.setText("");
        ID_Texto.requestFocus();
    }

    private void onTableSelection(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (model== null) return;
        int row = table1.getSelectedRow();
        if (row < 0) return;
        Medico c = model.getAt(row);
        if (c == null) return;

        ID_Texto.setText(String.valueOf(c.getID()));
        Nombre_Texto.setText(String.valueOf(c.getNombre()));
        Especialidad_Texto.setText(String.valueOf(c.getEspecialidad()));

    }

    private static class DatosForm {
        String id;
        String nombre;
        String especialidad;
        String clave;
    }

    private DatosForm readForm() {
        DatosForm d = new DatosForm();
        d.id= safe(ID_Texto.getText());
        d.nombre= safe(Nombre_Texto.getText());
        d.especialidad  = safe(Especialidad_Texto.getText());
        if (d.id.length() <= 0) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
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

