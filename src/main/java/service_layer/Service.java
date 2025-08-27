package service_layer;

import java.util.List;

public interface Service<T> {
    void buscar(String nombre);
    void agregar(T entity);
    void eliminar(String id);
    void actualizar(T entity);
    List<T> LeerTodo();
    T LeerID(String id);
    void Observer(ServiceObserver<T> listener);
}
