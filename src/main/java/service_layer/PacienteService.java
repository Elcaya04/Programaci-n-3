package service_layer;

import org.example.data_access_layer.FileStore;


import org.example.domain_layer.Medico;
import org.example.domain_layer.Paciente;
import utilites.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class PacienteService implements Service<Paciente> {
    private final FileStore<Paciente> fileStore;
    private final List<ServiceObserver<Paciente>> listeners=new ArrayList<>();

    public PacienteService(FileStore<Paciente> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void buscar(String nombre) {
        List<Paciente> pacientes = fileStore.Leer();
        Paciente paciente = null;
        for(int i=0;i<pacientes.size();i++){
            if(pacientes.get(i).getNombre().equals(nombre)){
                paciente=pacientes.get(i);
                break;
            }
        }
        if(paciente!=null) {
            notifyObservers(ChangeType.SEARCH, paciente);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND,null);
        }
    }

    @Override
    public void agregar(Paciente entity) {
        List<Paciente> pacientes = fileStore.Leer();
        pacientes.add(entity);
        fileStore.Escribir(pacientes);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String id) {
        List<Paciente> pacientes = fileStore.Leer();
       Paciente borrar=null;
        for(int i=0;i<pacientes.size();i++){
            if(pacientes.get(i).getID().equals(id)){
                borrar=pacientes.remove(i);
                break;
            }
        }

        fileStore.Escribir(pacientes);
        if (borrar != null) notifyObservers(ChangeType.DELETE, borrar);
    }

    @Override
    public void actualizar(Paciente entity) {
        List<Paciente> pacientes = fileStore.Leer();
        for (int i = 0; i < pacientes.size(); i++) {
            if (pacientes.get(i).getID() == entity.getID()) {
                pacientes.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(pacientes);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public List<Paciente> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Paciente LeerID(String id) {
        return fileStore.Leer()
                .stream()
                .filter(c -> c.getID() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Paciente> listener) {
        if(listener!=null) listeners.add(listener);
    }
    private void notifyObservers(ChangeType type, Paciente entity) {
        for (ServiceObserver<Paciente> l : listeners) l.DataChanged(type, entity);
    }
}
