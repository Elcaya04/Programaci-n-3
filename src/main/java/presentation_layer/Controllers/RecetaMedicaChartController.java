package presentation_layer.Controllers;

import org.example.domain_layer.RecetaMedica;
import presentation_layer.Models.RecetaMedicaChartModel;
import presentation_layer.Views.RecetasChartView.RecetasChartView;
import service_layer.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecetaMedicaChartController {
    private final Service<RecetaMedica> recetaService;
    private final RecetaMedicaChartModel chartModel;
    private RecetasChartView chartView;
    private List<RecetaMedica> allRecetas;

    public RecetaMedicaChartController(Service<RecetaMedica> recetaService) {
        this.recetaService = recetaService;
        this.chartModel = new RecetaMedicaChartModel();
        cargarDatos();
    }

    public void setChartView(RecetasChartView chartView) {
        this.chartView = chartView;
    }

    /**
     * Carga todos los datos de recetas desde el servicio
     */
    private void cargarDatos() {
        try {
            this.allRecetas = recetaService.LeerTodo();
        } catch (Exception e) {
            this.allRecetas = List.of(); // Lista vacía si hay error
            System.err.println("Error cargando recetas: " + e.getMessage());
        }
    }

    /**
     * Actualiza el gráfico de medicamentos por mes con filtros aplicados
     */
    public void actualizarGraficoMedicamentosPorMes(List<String> medicamentosSeleccionados,
                                                    Date fechaInicio,
                                                    Date fechaFin) {
        if (chartView != null) {
            var dataset = chartModel.createMedicamentosPorMesDataset(
                    allRecetas, medicamentosSeleccionados, fechaInicio, fechaFin
            );
            chartView.actualizarGraficoLineas(dataset);
        }
    }

    /**
     * Actualiza el gráfico de pastel de recetas por estado
     */
    public void actualizarGraficoPastelEstados() {
        if (chartView != null) {
            var dataset = chartModel.createRecetasPorEstadoDataset(allRecetas);
            chartView.actualizarGraficoPastel(dataset);
        }
    }

    /**
     * Obtiene la lista de medicamentos únicos para el filtro
     */
    public List<String> getMedicamentosDisponibles() {
        return chartModel.getMedicamentosUnicos(allRecetas);
    }

    /**
     * Obtiene la lista de estados únicos
     */
    public List<String> getEstadosDisponibles() {
        return chartModel.getEstadosUnicos(allRecetas);
    }

    /**
     * Obtiene el rango de fechas por defecto (últimos 12 meses)
     */
    public Date[] getRangoFechasPorDefecto() {
        Calendar cal = Calendar.getInstance();
        Date fechaFin = cal.getTime();

        cal.add(Calendar.MONTH, -12);
        Date fechaInicio = cal.getTime();

        return new Date[]{fechaInicio, fechaFin};
    }

    /**
     * Refresca los datos desde la base de datos
     */
    public void refrescarDatos() {
        cargarDatos();
        if (chartView != null) {
            chartView.actualizarTodosLosGraficos();
        }
    }

    /**
     * Obtiene estadísticas generales
     */
    public EstadisticasGenerales getEstadisticasGenerales() {
        if (allRecetas == null || allRecetas.isEmpty()) {
            return new EstadisticasGenerales(0, 0, 0, 0);
        }

        int totalRecetas = allRecetas.size();
        int medicamentosUnicos = (int) allRecetas.stream()
                .map(RecetaMedica::getNombreMedicamento)
                .distinct()
                .count();
        int pacientesUnicos = (int) allRecetas.stream()
                .map(RecetaMedica::getPacienteNombre)
                .distinct()
                .count();
        int cantidadTotalMedicamentos = allRecetas.stream()
                .mapToInt(RecetaMedica::getCantidad)
                .sum();

        return new EstadisticasGenerales(totalRecetas, medicamentosUnicos,
                pacientesUnicos, cantidadTotalMedicamentos);
    }

    /**
     * Clase para encapsular estadísticas generales
     */
    public static class EstadisticasGenerales {
        private final int totalRecetas;
        private final int medicamentosUnicos;
        private final int pacientesUnicos;
        private final int cantidadTotalMedicamentos;

        public EstadisticasGenerales(int totalRecetas, int medicamentosUnicos,
                                     int pacientesUnicos, int cantidadTotalMedicamentos) {
            this.totalRecetas = totalRecetas;
            this.medicamentosUnicos = medicamentosUnicos;
            this.pacientesUnicos = pacientesUnicos;
            this.cantidadTotalMedicamentos = cantidadTotalMedicamentos;
        }

        public int getTotalRecetas() { return totalRecetas; }
        public int getMedicamentosUnicos() { return medicamentosUnicos; }
        public int getPacientesUnicos() { return pacientesUnicos; }
        public int getCantidadTotalMedicamentos() { return cantidadTotalMedicamentos; }
    }

    /**
     * Filtra recetas por criterios específicos
     */
    public void filtrarYActualizar(List<String> medicamentos,
                                   Date fechaInicio,
                                   Date fechaFin,
                                   String estado) {
        actualizarGraficoMedicamentosPorMes(medicamentos, fechaInicio, fechaFin);
        actualizarGraficoPastelEstados();
    }

    /**
     * Exporta datos filtrados para reportes
     */
    public List<RecetaMedica> exportarDatosFiltrados(List<String> medicamentos,
                                                     Date fechaInicio,
                                                     Date fechaFin) {
        return allRecetas.stream()
                .filter(r -> medicamentos == null || medicamentos.isEmpty() ||
                        medicamentos.contains(r.getNombreMedicamento()))
                .filter(r -> esFechaEnRango(r.getFechaPrescripcion(), fechaInicio, fechaFin))
                .toList();
    }

    private boolean esFechaEnRango(String fechaStr, Date fechaInicio, Date fechaFin) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            Date fecha = sdf.parse(fechaStr);

            return (fechaInicio == null || !fecha.before(fechaInicio)) &&
                    (fechaFin == null || !fecha.after(fechaFin));
        } catch (Exception e) {
            return false;
        }
    }
}
