package presentation_layer.Controllers;

import org.example.domain_layer.Medicamentos;
import org.example.domain_layer.Paciente;
import org.example.domain_layer.RecetaMedica;
import presentation_layer.Views.MedicamentosView.MedicamentosView;
import presentation_layer.Views.PacienteView.PacienteView;
import presentation_layer.Views.PrescripcionView.DetalleRecetaDialog;
import service_layer.MedicamentoService;
import service_layer.PacienteService;
import service_layer.RecetaMedicaService;
import service_layer.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrescipcionController {
    private Service<RecetaMedica> recetaService;
    private Service<Paciente> pacienteService;
    private Service<Medicamentos> medicamentoService;
    private String medicoId; // ID del médico logueado
    // Variables para almacenar las selecciones actuales
    private Paciente pacienteSeleccionado;
    private Medicamentos medicamentoSeleccionado;
    private PacienteController pacienteController;
    private MedicamentosController medicamentosController;
    public PrescipcionController(Service<RecetaMedica> recetaService,
                                  Service<Paciente> pacienteService,
                                  Service<Medicamentos> medicamentoService) {
        this.recetaService = recetaService;
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.medicoId = "MED001";
    }

    public void buscarPaciente(String nombre, JComponent parent) {
        try {
            if (nombre == null || nombre.isEmpty() || nombre.length() <= 3) {
                JOptionPane.showMessageDialog(parent, "Nombre no puede estar vacio o ser muy corto", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int op = JOptionPane.showConfirmDialog(parent,
                    "¿Quiere buscar el paciente " + nombre + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (op == JOptionPane.YES_OPTION) {
                // Usar tu controller existente
                pacienteController.Buscar(nombre);

                // Obtener el resultado de la búsqueda
                List<Paciente> resultado = pacienteController.leerTodos();
                if (!resultado.isEmpty()) {
                    // Tomar el primer resultado que coincida
                    this.pacienteSeleccionado = resultado.stream()
                            .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                            .findFirst()
                            .orElse(null);

                    if (pacienteSeleccionado != null) {
                        JOptionPane.showMessageDialog(parent,
                                "Paciente encontrado: " + pacienteSeleccionado.getNombre(),
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    this.pacienteSeleccionado = null;
                    JOptionPane.showMessageDialog(parent,
                            "No se encontró ningún paciente con ese nombre",
                            "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.pacienteSeleccionado = null;
        }
    }

    public void buscarMedicamento(String nombre, JComponent parent) {
        try {
            if (nombre == null || nombre.isEmpty() || nombre.length() <= 3) {
                JOptionPane.showMessageDialog(parent, "Nombre no puede estar vacio o ser muy corto", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int op = JOptionPane.showConfirmDialog(parent,
                    "¿Quiere buscar el medicamento " + nombre + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (op == JOptionPane.YES_OPTION) {
                // Usar tu controller existente
                medicamentosController.Buscar(nombre);

                // Obtener el resultado de la búsqueda
                List<Medicamentos> resultado = medicamentosController.leerTodos();
                if (!resultado.isEmpty()) {
                    // Tomar el primer resultado que coincida
                    this.medicamentoSeleccionado = resultado.stream()
                            .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()) ||
                                    m.getCodigo().toLowerCase().contains(nombre.toLowerCase()))
                            .findFirst()
                            .orElse(null);

                    if (medicamentoSeleccionado != null) {
                        JOptionPane.showMessageDialog(parent,
                                "Medicamento encontrado: " + medicamentoSeleccionado.getNombre(),
                                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    this.medicamentoSeleccionado = null;
                    JOptionPane.showMessageDialog(parent,
                            "No se encontró ningún medicamento con ese nombre",
                            "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error al buscar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            this.medicamentoSeleccionado = null;
        }
    }
    public void crearReceta(JComponent parent) {
        try {
            if (pacienteSeleccionado == null) {
                JOptionPane.showMessageDialog(parent, "Debe buscar y seleccionar un paciente primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (medicamentoSeleccionado == null) {
                JOptionPane.showMessageDialog(parent, "Debe buscar y seleccionar un medicamento primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Usar el método existente para crear la receta
            RecetaMedica receta = agregarMedicamentoAReceta(parent, pacienteSeleccionado, medicamentoSeleccionado);

            if (receta != null) {
                guardarReceta(receta, parent);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error al crear receta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public RecetaMedica agregarMedicamentoAReceta(JComponent parent,
                                                  Paciente paciente,
                                                  Medicamentos medicamento) {
        try {
            DetalleRecetaDialog dialog = new DetalleRecetaDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(parent),
                    paciente,
                    medicamento
            );
            dialog.setVisible(true);

            if (dialog.isConfirmado()) {
                // Generar número de receta único
                String numeroReceta = generarNumeroReceta();

                // Obtener fechas
                String fechaPrescripcion = LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String fechaRetiro = LocalDate.now().plusDays(30)
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                // Crear la receta
                RecetaMedica receta = new RecetaMedica(
                        numeroReceta,
                        fechaPrescripcion,
                        fechaRetiro,
                        medicoId,
                        paciente.getID(),
                        paciente.getNombre(),
                        medicamento.getCodigo(),
                        medicamento.getNombre(),
                        medicamento.getPresentacion(),
                        dialog.getCantidad(),
                        dialog.getDuracion(),
                        dialog.getIndicaciones(),dialog.getEstado()
                );

                return receta;
            }

            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al crear receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void guardarReceta(RecetaMedica receta, JComponent parent) {
        try {
            recetaService.agregar(receta);
            JOptionPane.showMessageDialog(parent,
                    "Receta guardada correctamente con número: " + receta.getNumeroReceta(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al guardar la receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarReceta(RecetaMedica receta, JComponent parent) {
        try {
            recetaService.actualizar(receta);
            JOptionPane.showMessageDialog(parent,
                    "Receta actualizada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al actualizar la receta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generarNumeroReceta() {
        // Generar número único basado en timestamp
        return "REC-" + System.currentTimeMillis();
    }

    public String[] getFechasRetiroDisponibles() {
        // Generar fechas de retiro (7, 15, 30, 60 días)
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new String[] {
                hoy.plusDays(7).format(formatter) + " (7 días)",
                hoy.plusDays(15).format(formatter) + " (15 días)",
                hoy.plusDays(30).format(formatter) + " (30 días)",
                hoy.plusDays(60).format(formatter) + " (60 días)"
        };
    }
    public Paciente getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public Medicamentos getMedicamentoSeleccionado() {
        return medicamentoSeleccionado;
    }

    public void limpiarSelecciones() {
        this.pacienteSeleccionado = null;
        this.medicamentoSeleccionado = null;
    }

    public String getEstadoSelecciones() {
        return "Paciente: " + (pacienteSeleccionado != null ? pacienteSeleccionado.getNombre() : "No seleccionado") +
                " | Medicamento: " + (medicamentoSeleccionado != null ? medicamentoSeleccionado.getNombre() : "No seleccionado");
    }

}
