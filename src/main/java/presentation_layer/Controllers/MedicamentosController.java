package presentation_layer.Controllers;

import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medicamentos;
import service_layer.Service;

import java.util.List;

public class MedicamentosController {
    private final Service<Medicamentos> service;

    public MedicamentosController(Service<Medicamentos> service) {
        this.service = service;
    }
    public void agregar(String codigo,String nombre, String presentacion) {

        validarNombre(nombre);
        Medicamentos c = new Medicamentos(codigo,nombre,presentacion);
        service.agregar(c);
    }
    public List<Medicamentos> leerTodos() {
        return service.LeerTodo();
    }
    public void borrar(String codigo) {
        validarId(codigo);
        service.eliminar(codigo);
    }

    public void Buscar(String nombre) {
        validarNombre(nombre);
        service.buscar(nombre);

    }
    public void actualizar(String codigo,String nombre, String presentacion) {
        validarNombre(nombre);
        Medicamentos c = new Medicamentos(codigo,nombre,presentacion);
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
