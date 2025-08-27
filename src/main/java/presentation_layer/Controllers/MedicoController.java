package presentation_layer.Controllers;

import org.example.domain_layer.Medico;
import service_layer.Service;

import java.util.List;

public class MedicoController {
    private final Service<Medico> service;
    public MedicoController(Service<Medico> service) {
        this.service = service;
    }
    public void agregar(String id, String nombre, String especialidad, String clave) {
        validarId(id);
        validarNombre(nombre);
        Medico c = new Medico(id, nombre, especialidad,clave);
        service.agregar(c);
    }
    public List<Medico> leerTodos() {
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
    public void actualizar(String id, String nombre,String especialidad,String clave) {
        validarId(id);
        validarNombre(nombre);
        Medico c = new Medico(id, nombre, especialidad,clave);
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
