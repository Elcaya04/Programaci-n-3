package presentation_layer.Controllers;

import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import service_layer.Service;

import java.util.List;

public class PacienteController {
    private final Service<Paciente> service;

    public PacienteController(Service<Paciente> service) {
        this.service = service;
    }
    public void agregar(String id, String nombre, String Fecha_Nacimiento, String Num_Telefono) {
        validarId(id);
        validarNombre(nombre);
        Paciente c = new Paciente(id, nombre, Fecha_Nacimiento,Num_Telefono);
        service.agregar(c);
    }
    public List<Paciente> leerTodos() {
        return service.LeerTodo();
    }
    public void borrar(String id) {
        validarId(id);
        service.eliminar(id);
    }
    public void Buscar(String nombre) {
        validarNombre(nombre);
        service.buscar(nombre);

    }
    public void actualizar(String id, String nombre,String Fecha_Nacimiento, String Num_Telefono) {
        validarId(id);
        validarNombre(nombre);
        Paciente c = new Paciente(id, nombre, Fecha_Nacimiento,Num_Telefono);
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
