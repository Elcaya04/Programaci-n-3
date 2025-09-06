package service_layer;

import org.example.data_access_layer.FileStore;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import utilites.ChangeType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoService implements Service<Medico> {
    private final FileStore<Medico> fileStore;
    private final List<ServiceObserver<Medico>> listeners=new ArrayList<>();
    public MedicoService(FileStore<Medico> fileStore) {
        this.fileStore = fileStore;
    }
    @Override
    public void buscar(String nombre) {
        List<Medico> medicos = fileStore.Leer();
        Medico medico = null;
        for(int i=0;i<medicos.size();i++){
            if(medicos.get(i).getNombre().equals(nombre)){
                medico=medicos.get(i);
                break;
            }
        }
         if(medico!=null) {
             notifyObservers(ChangeType.SEARCH, medico);
         }
         else {
             notifyObservers(ChangeType.NOT_FOUND,null);
         }
    }

    @Override
    public void agregar(Medico entity) {
        List<Medico> medicos = fileStore.Leer();
        medicos.add(entity);
        fileStore.Escribir(medicos);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String id) {
        List<Medico> medicos = fileStore.Leer();
        Medico borrar=null;
        for(int i=0;i<medicos.size();i++){
            if(medicos.get(i).getID().equals(id)){
               borrar=medicos.remove(i);
               break;
            }
        }

        fileStore.Escribir(medicos);
        if (borrar != null) notifyObservers(ChangeType.DELETE, borrar);
    }

    @Override
    public void actualizar(Medico entity) {
        List<Medico> medicos = fileStore.Leer();
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getID().equals(entity.getID())) {
                medicos.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(medicos);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public List<Medico> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Medico LeerID(String id) {
        return fileStore.Leer()
                .stream()
                .filter(c -> c.getID().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Medico> listener) {
if(listener!=null) listeners.add(listener);
    }

    @Override
    public Medico Buscar_porID(String id) {
        List<Medico> medicos = fileStore.Leer();
        Medico medico = null;
        for(int i = 0; i < medicos.size(); i++){
            if(medicos.get(i).getID().equals(id)){
                medico = medicos.get(i);
                break;
            }
        }
        if(medico != null) {
            notifyObservers(ChangeType.SEARCH, medico);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND, null);
        }
        return medico;
    }


    private void notifyObservers(ChangeType type, Medico entity) {
        for (ServiceObserver<Medico> l : listeners) l.DataChanged(type, entity);
    }
}
