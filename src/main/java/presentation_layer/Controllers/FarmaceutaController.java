package presentation_layer.Controllers;

import org.example.domain_layer.Farmaceuta;

import service_layer.Service;

import java.util.List;

public class FarmaceutaController {
    private final Service<Farmaceuta> service;
    public FarmaceutaController(Service<Farmaceuta> service) {
        this.service = service;
    }
    public void agregar(String id,String clave, String nombre) {
        String x;
        x=id;
        clave=id;
        validarNombre(nombre);
        Farmaceuta c = new Farmaceuta(x, clave, nombre);
        service.agregar(c);
    }
    public List<Farmaceuta> leerTodos() {
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
    public void actualizar(String id,String clave, String nombre) {
        validarId(id);
        validarNombre(nombre);
        Farmaceuta c = new Farmaceuta(id,clave, nombre);
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
