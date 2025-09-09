package presentation_layer.Controllers;

import org.example.domain_layer.RecetaMedica;
import service_layer.Service;
import utilites.PrescriptionState;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class DespachoController {private final Service<RecetaMedica> recetaMedicaService;


    public DespachoController(Service<RecetaMedica> recetaMedicaService) {
        this.recetaMedicaService = recetaMedicaService;
    }

    /**
     * Busca recetas disponibles para despacho por cédula del paciente
     * Solo retorna recetas en estado PENDING que estén en el rango de fechas válidas
     */
    public List<RecetaMedica> buscarRecetasParaDespacho(String idPaciente) throws Exception {
        List<RecetaMedica> todasLasRecetas = recetaMedicaService.LeerTodo();

        // Agregar debug para verificar qué estamos comparando
        System.out.println("Buscando recetas para ID paciente: '" + idPaciente + "'");

        List<RecetaMedica> recetasDelPaciente = todasLasRecetas.stream()
                .filter(receta -> {
                    String pacienteIdReceta = receta.getPacienteId();
                    boolean match = pacienteIdReceta.equals(idPaciente);
                    if (match) {
                        System.out.println("ENCONTRADA - ID: '" + pacienteIdReceta + "' | Estado: " + receta.getEstado() + " | Fecha Retiro: " + receta.getFechaRetiro());
                    }
                    return match;
                })
                .collect(Collectors.toList());

        System.out.println("Recetas del paciente " + idPaciente + " (antes de filtros): " + recetasDelPaciente.size());

        // Aplicar filtro de estado PENDING
        List<RecetaMedica> recetasPending = recetasDelPaciente.stream()
                .filter(receta -> {
                    PrescriptionState estado = receta.getEstado();
                    boolean isPending = (estado == PrescriptionState.PENDING || estado == null);
                    if (!isPending) {
                        System.out.println("FILTRADA POR ESTADO - Receta: " + receta.getNumeroReceta() + " | Estado: " + estado);
                    }
                    return isPending;
                })
                .collect(Collectors.toList());

        System.out.println("Recetas PENDING (incluyendo null): " + recetasPending.size());

        // Aplicar filtro de fecha
        List<RecetaMedica> recetasValidasFecha = recetasPending.stream()
                .filter(receta -> {
                    boolean fechaValida = esFechaValidaParaDespacho(receta);
                    if (!fechaValida) {
                        System.out.println("FILTRADA POR FECHA - Receta: " + receta.getNumeroReceta() + " | Fecha Retiro: " + receta.getFechaRetiro() + " (fuera del rango de ±15 días)");
                    }
                    return fechaValida;
                })
                .collect(Collectors.toList());

        System.out.println("Total recetas encontradas para " + idPaciente + ": " + recetasValidasFecha.size());
        return recetasValidasFecha;
    }

    /**
     * Busca todas las recetas en proceso o listas para un paciente específico
     */
    public List<RecetaMedica> buscarRecetasEnProceso(String idPaciente) throws Exception {
        List<RecetaMedica> todasLasRecetas = recetaMedicaService.LeerTodo();

        return todasLasRecetas.stream()
                .filter(receta -> receta.getPacienteId().equals(idPaciente))
                .filter(receta -> receta.getEstado() == PrescriptionState.PROCESSING ||
                        receta.getEstado() == PrescriptionState.READY)
                .collect(Collectors.toList());
    }



    /**
     * Cambia el estado de una receta a "En Proceso"
     */
    public void ponerEnProceso(String numeroReceta, String farmaceutaId) throws Exception {
        cambiarEstadoReceta(numeroReceta, PrescriptionState.PROCESSING, farmaceutaId);
    }

    /**
     * Cambia el estado de una receta a "Lista"
     */
    public void marcarComoLista(String numeroReceta, String farmaceutaId) throws Exception {
        cambiarEstadoReceta(numeroReceta, PrescriptionState.READY, farmaceutaId);
    }

    /**
     * Cambia el estado de una receta a "Entregada"
     */
    public void entregar(String numeroReceta, String farmaceutaId) throws Exception {
        cambiarEstadoReceta(numeroReceta, PrescriptionState.DELIVERED, farmaceutaId);
    }

    /**
     * Metodo para cambiar el estado de un receta
     */
    private void cambiarEstadoReceta(String numeroReceta, PrescriptionState nuevoEstado, String farmaceutaId) throws Exception {
        RecetaMedica receta = buscarRecetaPorNumero(numeroReceta);

        if (receta == null) {
            throw new Exception("Receta no encontrada: " + numeroReceta);
        }

        // Validar transición de estado
        if (!esTransicionValida(receta.getEstado(), nuevoEstado)) {
            throw new Exception("Transición de estado no válida: " +
                    formatearEstado(receta.getEstado()) + " -> " + formatearEstado(nuevoEstado));
        }

        // Crear nueva receta con el estado actualizado
        RecetaMedica recetaActualizada = new RecetaMedica(
                receta.getNumeroReceta(),
                receta.getFechaPrescripcion(),
                receta.getFechaRetiro(),
                receta.getMedicoId(),
                receta.getPacienteId(),
                receta.getPacienteNombre(),
                receta.getCodigoMedicamento(),
                receta.getNombreMedicamento(),
                receta.getPresentacion(),
                receta.getCantidad(),
                receta.getDuracionDias(),
                receta.getIndicaciones(),
                nuevoEstado
        );

        // Actualizar en el servicio
        recetaMedicaService.actualizar(recetaActualizada);
    }

    /**
     * Busca una receta específica por su número
     */
    private RecetaMedica buscarRecetaPorNumero(String numeroReceta) throws Exception {
        List<RecetaMedica> recetas = recetaMedicaService.LeerTodo();
        return recetas.stream()
                .filter(r -> r.getNumeroReceta().equals(numeroReceta))
                .findFirst()
                .orElse(null);
    }
    public List<RecetaMedica> buscarRecetasListas(String idPaciente) throws Exception {
        List<RecetaMedica> todasLasRecetas = recetaMedicaService.LeerTodo();

        return todasLasRecetas.stream()
                .filter(receta -> receta.getPacienteId().equals(idPaciente))
                .filter(receta -> receta.getEstado() == PrescriptionState.READY)
                .collect(Collectors.toList());
    }


    /**
     * Valida si la transición entre estados es permitida
     */
    private boolean esTransicionValida(PrescriptionState estadoActual, PrescriptionState nuevoEstado) {
        switch (estadoActual) {
            case PENDING:
                return nuevoEstado == PrescriptionState.PROCESSING;
            case PROCESSING:
                return nuevoEstado == PrescriptionState.READY;
            case READY:
                return nuevoEstado == PrescriptionState.DELIVERED;
            case DELIVERED:
                return false; // No se puede cambiar una receta ya entregada
            default:
                return false;
        }
    }

    /**
     * Verifica si la fecha de retiro está en el rango válido (±3 días)
     */
    private boolean esFechaValidaParaDespacho(RecetaMedica receta) {
        try {
            LocalDate hoy = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaRetiro = LocalDate.parse(receta.getFechaRetiro(), formatter);

            // Verificar si está en el rango de ±3 días
            long diferenciaDias = Math.abs(hoy.toEpochDay() - fechaRetiro.toEpochDay());
            boolean esValida = diferenciaDias <= 15;

            System.out.println("VALIDACIÓN FECHA - Receta: " + receta.getNumeroReceta() +
                    " | Hoy: " + hoy +
                    " | Fecha Retiro: " + fechaRetiro +
                    " | Diferencia días: " + diferenciaDias +
                    " | Es válida: " + esValida);

            return esValida;
        } catch (Exception e) {
            System.err.println("Error al verificar fecha para receta " + receta.getNumeroReceta() + ": " + receta.getFechaRetiro() + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las recetas según su estado
     */
    public List<RecetaMedica> obtenerRecetasPorEstado(PrescriptionState estado) throws Exception {
        List<RecetaMedica> todasLasRecetas = recetaMedicaService.LeerTodo();

        return todasLasRecetas.stream()
                .filter(receta -> receta.getEstado() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Formatea el nombre del estado para mostrar al usuario
     */
    public static String formatearEstado(PrescriptionState estado) {
        if (estado == null) return "Sin Estado";

        switch (estado) {
            case PENDING:
                return "Pendiente";
            case PROCESSING:
                return "En Proceso";
            case READY:
                return "Lista";
            case DELIVERED:
                return "Entregada";
            default:
                return estado.toString();
        }
    }

    /**
     * Convierte string de estado a enum
     */
    public static PrescriptionState parsearEstado(String estadoTexto) {
        switch (estadoTexto) {
            case "Pendiente":
                return PrescriptionState.PENDING;
            case "En Proceso":
                return PrescriptionState.PROCESSING;
            case "Lista":
                return PrescriptionState.READY;
            case "Entregada":
                return PrescriptionState.DELIVERED;
            default:
                return PrescriptionState.PENDING;
        }
    }

    /**
     * Obtiene estadísticas de despacho
     */
    public EstadisticasDespacho obtenerEstadisticas() throws Exception {
        List<RecetaMedica> todasLasRecetas = recetaMedicaService.LeerTodo();
        System.out.println("Total recetas en BD: " + todasLasRecetas.size());
        long pendientes = todasLasRecetas.stream().filter(r -> r.getEstado() == PrescriptionState.PENDING).count();
        long enProceso = todasLasRecetas.stream().filter(r -> r.getEstado() == PrescriptionState.PROCESSING).count();
        long listas = todasLasRecetas.stream().filter(r -> r.getEstado() == PrescriptionState.READY).count();
        long entregadas = todasLasRecetas.stream().filter(r -> r.getEstado() == PrescriptionState.DELIVERED).count();
        System.out.println("Estadísticas: P=" + pendientes + ", EP=" + enProceso + ", L=" + listas + ", E=" + entregadas);
        return new EstadisticasDespacho((int)pendientes, (int)enProceso, (int)listas, (int)entregadas);
    }

    /**
     * Clase para encapsular estadísticas de despacho
     */
    public static class EstadisticasDespacho {
        private final int pendientes;
        private final int enProceso;
        private final int listas;
        private final int entregadas;

        public EstadisticasDespacho(int pendientes, int enProceso, int listas, int entregadas) {
            this.pendientes = pendientes;
            this.enProceso = enProceso;
            this.listas = listas;
            this.entregadas = entregadas;
        }

        public int getPendientes() { return pendientes; }
        public int getEnProceso() { return enProceso; }
        public int getListas() { return listas; }
        public int getEntregadas() { return entregadas; }

    }

}
