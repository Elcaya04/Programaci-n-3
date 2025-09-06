package service_layer;

import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;

import java.util.List;

public interface Service<T> {
    void buscar(String nombre);
    void agregar(T entity);
    void eliminar(String id);
    void actualizar(T entity);
    List<T> LeerTodo();
    T LeerID(String id);

    void Observer(ServiceObserver<T> listener);

    T Buscar_porID(String id);

}
