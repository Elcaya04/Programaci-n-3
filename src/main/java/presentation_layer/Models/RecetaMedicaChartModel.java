package presentation_layer.Models;

import org.example.domain_layer.RecetaMedica;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class RecetaMedicaChartModel {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM yyyy");

    /**
     * Genera dataset para gráfico de líneas de medicamentos prescritos por mes
     * @param recetas Lista de recetas médicas
     * @param medicamentosSeleccionados Lista de medicamentos a incluir (null = todos)
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Dataset para gráfico de líneas
     */
    public DefaultCategoryDataset createMedicamentosPorMesDataset(List<RecetaMedica> recetas,
                                                                  List<String> medicamentosSeleccionados,
                                                                  Date fechaInicio,
                                                                  Date fechaFin) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (recetas == null || recetas.isEmpty()) {
            return dataset;
        }

        // Filtrar recetas por fecha y medicamentos
        List<RecetaMedica> recetasFiltradas = recetas.stream()
                .filter(r -> esFechaEnRango(r.getFechaPrescripcion(), fechaInicio, fechaFin))
                .filter(r -> medicamentosSeleccionados == null ||
                        medicamentosSeleccionados.isEmpty() ||
                        medicamentosSeleccionados.contains(r.getNombreMedicamento()))
                .collect(Collectors.toList());

        // Agrupar por medicamento y mes
        Map<String, Map<String, Integer>> medicamentosPorMes = new TreeMap<>();

        for (RecetaMedica receta : recetasFiltradas) {
            String medicamento = receta.getNombreMedicamento();
            String mesAnio = getMesAnio(receta.getFechaPrescripcion());

            medicamentosPorMes.computeIfAbsent(medicamento, k -> new TreeMap<>())
                    .merge(mesAnio, receta.getCantidad(), Integer::sum);
        }

        // Llenar el dataset
        for (Map.Entry<String, Map<String, Integer>> medicamentoEntry : medicamentosPorMes.entrySet()) {
            String medicamento = medicamentoEntry.getKey();
            for (Map.Entry<String, Integer> mesEntry : medicamentoEntry.getValue().entrySet()) {
                String mes = mesEntry.getKey();
                Integer cantidad = mesEntry.getValue();
                dataset.addValue(cantidad, medicamento, mes);
            }
        }

        return dataset;
    }

    /**
     * Genera dataset para gráfico de pastel de recetas por estado
     * @param recetas Lista de recetas médicas
     * @return Dataset para gráfico de pastel
     */
    public DefaultPieDataset createRecetasPorEstadoDataset(List<RecetaMedica> recetas) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        if (recetas == null || recetas.isEmpty()) {
            return dataset;
        }

        // Contar recetas por estado
        Map<String, Integer> estadoCount = recetas.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEstado() != null ? r.getEstado() : "Sin Estado",
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));

        // Llenar el dataset
        for (Map.Entry<String, Integer> entry : estadoCount.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        return dataset;
    }

    /**
     * Obtiene la lista de medicamentos únicos de las recetas
     */
    public List<String> getMedicamentosUnicos(List<RecetaMedica> recetas) {
        if (recetas == null || recetas.isEmpty()) {
            return new ArrayList<>();
        }

        return recetas.stream()
                .map(RecetaMedica::getNombreMedicamento)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de estados únicos de las recetas
     */
    public List<String> getEstadosUnicos(List<RecetaMedica> recetas) {
        if (recetas == null || recetas.isEmpty()) {
            return new ArrayList<>();
        }

        return recetas.stream()
                .map(r -> r.getEstado() != null ? r.getEstado() : "Sin Estado")
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Convierte fecha string a mes-año para agrupación
     */
    private String getMesAnio(String fechaStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = inputFormat.parse(fechaStr);
            return displayFormat.format(fecha);
        } catch (Exception e) {
            return "Fecha inválida";
        }
    }

    /**
     * Verifica si una fecha está en el rango especificado
     */
    private boolean esFechaEnRango(String fechaStr, Date fechaInicio, Date fechaFin) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fecha = inputFormat.parse(fechaStr);

            return (fechaInicio == null || !fecha.before(fechaInicio)) &&
                    (fechaFin == null || !fecha.after(fechaFin));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Genera rango de meses entre dos fechas
     */
    public List<String> generarRangoMeses(Date fechaInicio, Date fechaFin) {
        List<String> meses = new ArrayList<>();

        if (fechaInicio == null || fechaFin == null) {
            return meses;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);

        Date fechaActual = fechaInicio;
        while (!fechaActual.after(fechaFin)) {
            meses.add(displayFormat.format(fechaActual));
            cal.add(Calendar.MONTH, 1);
            fechaActual = cal.getTime();
        }

        return meses;
    }
}
