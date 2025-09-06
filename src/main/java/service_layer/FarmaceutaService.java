package service_layer;

import org.example.data_access_layer.FileStore;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import utilites.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class FarmaceutaService implements Service<Farmaceuta> {
    private final FileStore<Farmaceuta> fileStore;
    private final List<ServiceObserver<Farmaceuta>> listeners=new ArrayList<>();

    public FarmaceutaService(FileStore<Farmaceuta> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void buscar(String nombre) {
        List<Farmaceuta> farmaceutas = fileStore.Leer();
        Farmaceuta farmaceuta = null;
        for(int i=0;i<farmaceutas.size();i++){
            if(farmaceutas.get(i).getNombre().equals(nombre)){
                farmaceuta=farmaceutas.get(i);
                break;
            }
        }
        if(farmaceuta!=null) {
            notifyObservers(ChangeType.SEARCH, farmaceuta);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND,null);
        }
    }


    @Override
    public void agregar(Farmaceuta entity) {
        List<Farmaceuta> farmaceutas = fileStore.Leer();
        farmaceutas.add(entity);
        fileStore.Escribir(farmaceutas);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String id) {
        List<Farmaceuta> farmaceutas = fileStore.Leer();
        Farmaceuta borrar=null;
        for(int i=0;i<farmaceutas.size();i++){
            if(farmaceutas.get(i).getID().equals(id)){
                borrar=farmaceutas.remove(i);
                break;
            }
        }

        fileStore.Escribir(farmaceutas);
        if (borrar != null) notifyObservers(ChangeType.DELETE, borrar);
    }

    @Override
    public void actualizar(Farmaceuta entity) {
        List<Farmaceuta> farmaceutas = fileStore.Leer();
        for (int i = 0; i < farmaceutas.size(); i++) {
            if (farmaceutas.get(i).getID().equals(entity.getID())) {
                farmaceutas.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(farmaceutas);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public List<Farmaceuta> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Farmaceuta LeerID(String id) {
        return fileStore.Leer()
                .stream()
                .filter(c -> c.getID().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Farmaceuta> listener) {
        if(listener!=null) listeners.add(listener);
    }

    @Override
    public Farmaceuta Buscar_porID(String id) {
        List<Farmaceuta> farmaceutas = fileStore.Leer();
        Farmaceuta farmaceuta = null;
        for(int i = 0; i < farmaceutas.size(); i++){
            if(farmaceutas.get(i).getID().equals(id)){  // Cambio aquí: getID() en lugar de getNombre()
                farmaceuta = farmaceutas.get(i);
                break;
            }
        }
        if(farmaceuta != null) {
            notifyObservers(ChangeType.SEARCH, farmaceuta);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND, null);
        }
        return farmaceuta;
    }

    private void notifyObservers(ChangeType type,Farmaceuta entity) {
        for (ServiceObserver<Farmaceuta> l : listeners) l.DataChanged(type, entity);
    }
    }

