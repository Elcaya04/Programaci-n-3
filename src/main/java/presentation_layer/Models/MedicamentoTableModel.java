package presentation_layer.Models;

import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medicamentos;
import service_layer.ServiceObserver;
import utilites.ChangeType;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoTableModel extends AbstractTableModel implements ServiceObserver<Medicamentos> {
    private final String[] cols = { "Codigo", "Nombre", "Presentacion"};
    private final Class<?>[] types = { String.class, String.class, String.class };
    private final List<Medicamentos> rows=  new ArrayList<>();
    public void setRows(List<Medicamentos> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }
    public Medicamentos getAt(int row) {
        return (row >= 0 && row < rows.size()) ? rows.get(row) : null;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }
    @Override public String getColumnName(int c) {
        return cols[c];
    }
    @Override public Class<?> getColumnClass(int c) {
        return types[c];
    }
    @Override public boolean isCellEditable(int r, int c) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Medicamentos x = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return x.getNombre();
            case 1: return x.getCodigo();
            case 2: return x.getPresentacion();
            default: return null;
        }
    }

    @Override
    public void DataChanged(ChangeType type, Medicamentos entity) {
        switch (type) {
            case CREATE: {
                rows.add(entity);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }
            case UPDATE: {
                int i = indexOf(entity.getNombre());
                if (i >= 0) {
                    rows.set(i, entity);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            case DELETE: {
                int i = indexOf(entity.getNombre());
                if (i >= 0) {
                    rows.remove(i);
                    fireTableRowsDeleted(i, i);
                }
                break;
            }
            case SEARCH: {
                rows.clear();
                if (entity != null) rows.add(entity);
                fireTableDataChanged();
                break;
            }
            case NOT_FOUND: {
                rows.clear();
                fireTableDataChanged();
                break;
            }
        }
    }
    private int indexOf(String nombre) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getNombre().equals(nombre)) return i;
        }
        return -1;
    }
}
