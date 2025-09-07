package service_layer;


import org.example.data_access_layer.FileStore;
import org.example.domain_layer.Paciente;
import org.example.domain_layer.RecetaMedica;
import utilites.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class RecetaMedicaService implements Service<RecetaMedica> {
    private final FileStore<RecetaMedica> fileStore;
    private final List<ServiceObserver<RecetaMedica>> listeners=new ArrayList<>();

    public RecetaMedicaService(FileStore<RecetaMedica> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void buscar(String Numero_Receta) {
        List<RecetaMedica> recetas = fileStore.Leer();
        RecetaMedica receta = null;
        for(int i=0;i<recetas.size();i++){
            if(recetas.get(i).getNumeroReceta().equals(Numero_Receta)){
                receta=recetas.get(i);
                break;
            }
        }
        if(receta!=null) {
            notifyObservers(ChangeType.SEARCH, receta);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND,null);
        }
    }

    @Override
    public void agregar(RecetaMedica entity) {
        List<RecetaMedica> recetas = fileStore.Leer();
        recetas.add(entity);
        fileStore.Escribir(recetas);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String numero_Receta) {
        List<RecetaMedica> recetas = fileStore.Leer();
        RecetaMedica borrar=null;
        for(int i=0;i<recetas.size();i++){
            if(recetas.get(i).getNumeroReceta().equals(numero_Receta)){
                borrar=recetas.remove(i);
                break;
            }
        }

        fileStore.Escribir(recetas);
        if (borrar != null) notifyObservers(ChangeType.DELETE, borrar);
    }

    @Override
    public void actualizar(RecetaMedica entity) {
        List<RecetaMedica> recetas = fileStore.Leer();
        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getNumeroReceta().equals(entity.getNumeroReceta())) {
                recetas.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(recetas);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public List<RecetaMedica> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public RecetaMedica LeerID(String Numero_Receta) {
        return fileStore.Leer()
                .stream()
                .filter(c -> c.getNumeroReceta().equals(Numero_Receta))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<RecetaMedica> listener) {
        if(listener!=null) listeners.add(listener);
    }

    @Override
    public RecetaMedica Buscar_porID(String id) {
        return null;
    }
    private void notifyObservers(ChangeType type, RecetaMedica entity) {
        for (ServiceObserver<RecetaMedica> l : listeners) l.DataChanged(type, entity);
    }
}
