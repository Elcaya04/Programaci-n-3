package presentation_layer.Controllers;


import org.example.domain_layer.RecetaMedica;
import service_layer.Service;
import utilites.PrescriptionState;

import java.util.List;

public class RecetaMedicaController {
    private final Service<RecetaMedica> service;


    public RecetaMedicaController(Service<RecetaMedica> service) {
        this.service = service;
    }
    public void agregar(String numeroReceta, String fechaPrescripcion, String fechaRetiro,
                        String medicoId, String pacienteId, String pacienteNombre,
                        String codigoMedicamento, String nombreMedicamento, String presentacion,
                        int cantidad, int duracionDias, String indicaciones, PrescriptionState estado) {
        validarNombre(nombreMedicamento);
        RecetaMedica c = new RecetaMedica(numeroReceta, fechaPrescripcion, fechaRetiro,medicoId,pacienteId,pacienteNombre,codigoMedicamento,nombreMedicamento,presentacion,cantidad,duracionDias,indicaciones,estado);
        service.agregar(c);
    }
    public List<RecetaMedica> leerTodos() {
        return service.LeerTodo();
    }
    public void borrar(String numeroReceta) {
        validarId(numeroReceta);
        service.eliminar(numeroReceta);
    }
    public void Buscar(String nombreMedicamento) {
        validarNombre(nombreMedicamento);
        service.buscar(nombreMedicamento);

    }
    public void actualizar(String numeroReceta, String fechaPrescripcion, String fechaRetiro,
                           String medicoId, String pacienteId, String pacienteNombre,
                           String codigoMedicamento, String nombreMedicamento, String presentacion,
                           int cantidad, int duracionDias, String indicaciones) {

        validarNombre(nombreMedicamento);
        RecetaMedica c = new RecetaMedica(numeroReceta, fechaPrescripcion, fechaRetiro,medicoId,pacienteId,pacienteNombre,codigoMedicamento,nombreMedicamento,presentacion,cantidad,duracionDias,indicaciones,PrescriptionState.PENDING);
        service.actualizar(c);
    }
    private void validarId(String id) {
        if (id.length()<2) throw new IllegalArgumentException("El ID debe ser mayor que 0.");
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
    }
}
