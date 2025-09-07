package presentation_layer.Models;

import org.example.domain_layer.RecetaMedica;
import service_layer.ServiceObserver;
import utilites.ChangeType;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RecetaMedicaTableModel extends AbstractTableModel implements ServiceObserver<RecetaMedica> {
    private final String[] cols = { "N° Receta", "Fecha Prescripción", "Médico", "Paciente", "Medicamento", "Cantidad", "Duración", "Estado"};
    private final Class<?>[] types = { String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class };
    private final List<RecetaMedica> rows = new ArrayList<>();

    public void setRows(List<RecetaMedica> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public RecetaMedica getAt(int row) {
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

    @Override
    public String getColumnName(int c) {
        return cols[c];
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return types[c];
    }

    @Override
    public boolean isCellEditable(int r, int c) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RecetaMedica x = rows.get(rowIndex);
        switch (columnIndex) {
            case 0: return x.getNumeroReceta();
            case 1: return x.getFechaPrescripcion();
            case 2: return x.getMedicoId();
            case 3: return x.getPacienteNombre();
            case 4: return x.getNombreMedicamento();
            case 5: return x.getCantidad() + " " + x.getPresentacion();
            case 6: return x.getDuracionDias() + " días";
            case 7: return x.getEstado();
            default: return null;
        }
    }

    @Override
    public void DataChanged(ChangeType type, RecetaMedica entity) {
        switch (type) {
            case CREATE: {
                rows.add(entity);
                int i = rows.size() - 1;
                fireTableRowsInserted(i, i);
                break;
            }
            case UPDATE: {
                int i = indexOf(entity.getNumeroReceta());
                if (i >= 0) {
                    rows.set(i, entity);
                    fireTableRowsUpdated(i, i);
                }
                break;
            }
            case DELETE: {
                int i = indexOf(entity.getNumeroReceta());
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

    private int indexOf(String numeroReceta) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).getNumeroReceta().equals(numeroReceta)) return i;
        }
        return -1;
    }
}
