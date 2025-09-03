package presentation_layer.Models;

import org.example.domain_layer.Farmaceuta;
import service_layer.ServiceObserver;
import utilites.ChangeType;

import javax.swing.table.AbstractTableModel;

import java.util.ArrayList;
import java.util.List;

public class FarmaceutaTableModel extends AbstractTableModel implements ServiceObserver<Farmaceuta> {
    private final String[] cols = { "ID", "Clave", "Nombre"};
    private final Class<?>[] types = { String.class, String.class, String.class };
    private final List<Farmaceuta> rows=  new ArrayList<>();
    public void setRows(List<Farmaceuta> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }
    public Farmaceuta getAt(int row) {
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
        Farmaceuta x = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return x.getID();
            case 1: return x.getClave();
            case 2: return x.getNombre();
            default: return null;
        }
    }

    @Override
    public void DataChanged(ChangeType type, Farmaceuta entity) {
        switch (type) {
            case CREATE: {
                rows.add(entity);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }
            case UPDATE: {
                int i = indexOf(entity.getID());
                if (i >= 0) {
                    rows.set(i, entity);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            case DELETE: {
                int i = indexOf(entity.getID());
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
    private int indexOf(String id) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getID().equals(id)) return i;
        }
        return -1;
    }
    }

