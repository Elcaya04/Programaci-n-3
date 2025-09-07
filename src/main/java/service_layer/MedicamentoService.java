package service_layer;

import org.example.data_access_layer.FileStore;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medicamentos;
import utilites.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class MedicamentoService implements Service<Medicamentos> {
    private final FileStore<Medicamentos> fileStore;
    private final List<ServiceObserver<Medicamentos>> listeners=new ArrayList<>();


    public MedicamentoService(FileStore<Medicamentos> fileStore) {
        this.fileStore = fileStore;
    }

    @Override
    public void buscar(String nombre) {
        List<Medicamentos> medicamentos = fileStore.Leer();
        Medicamentos medicamento = null;
        for(int i=0;i<medicamentos.size();i++){
            if(medicamentos.get(i).getNombre().equals(nombre)){
                medicamento=medicamentos.get(i);
                break;
            }
        }
        if( medicamento!=null) {
            notifyObservers(ChangeType.SEARCH,  medicamento);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND,null);
        }
    }

    @Override
    public void agregar(Medicamentos entity) {
        List<Medicamentos> medicamentos = fileStore.Leer();
        medicamentos.add(entity);
        fileStore.Escribir(medicamentos);
        notifyObservers(ChangeType.CREATE, entity);
    }

    @Override
    public void eliminar(String codigo) {
        List<Medicamentos> medicamentos = fileStore.Leer();
        Medicamentos borrar=null;
        for(int i=0;i<medicamentos.size();i++){
            if(medicamentos.get(i).getCodigo().equals(codigo)){
                borrar=medicamentos.remove(i);
                break;
            }
        }

        fileStore.Escribir(medicamentos);
        if (borrar != null) notifyObservers(ChangeType.DELETE, borrar);
    }

    @Override
    public void actualizar(Medicamentos entity) {
        List<Medicamentos> medicamentos = fileStore.Leer();
        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(entity.getCodigo())) {
                medicamentos.set(i, entity);
                break;
            }
        }
        fileStore.Escribir(medicamentos);
        notifyObservers(ChangeType.UPDATE, entity);
    }

    @Override
    public List<Medicamentos> LeerTodo() {
        return fileStore.Leer();
    }

    @Override
    public Medicamentos LeerID(String codigo) {
        return fileStore.Leer()
                .stream()
                .filter(c -> c.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void Observer(ServiceObserver<Medicamentos> listener) {
        if(listener!=null) listeners.add(listener);
    }

    @Override
    public Medicamentos Buscar_porID(String codigo) {
        List<Medicamentos> medicamentos = fileStore.Leer();
        Medicamentos medicamento = null;
        for(int i = 0; i < medicamentos.size(); i++){
            if(medicamentos.get(i).getCodigo().equals(codigo)){  // Cambio aquí: getID() en lugar de getNombre()
                medicamento = medicamentos.get(i);
                break;
            }
        }
        if(medicamento != null) {
            notifyObservers(ChangeType.SEARCH, medicamento);
        }
        else {
            notifyObservers(ChangeType.NOT_FOUND, null);
        }
        return medicamento;
    }
    private void notifyObservers(ChangeType type,Medicamentos entity) {
        for (ServiceObserver<Medicamentos> l : listeners) l.DataChanged(type, entity);
    }
}
