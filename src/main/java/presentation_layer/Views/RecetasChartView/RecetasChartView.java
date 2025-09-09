package presentation_layer.Views.RecetasChartView;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import presentation_layer.Controllers.RecetaMedicaChartController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import java.util.List;

public class RecetasChartView {
    private JPanel panelContenido;
    private RecetaMedicaChartController controller;

    // Componentes de filtros
    private JList<String> listaMedicamentos;
    private JSpinner spinnerFechaInicio;
    private JSpinner spinnerFechaFin;
    private JButton btnFiltrar;
    private JButton btnRefrescar;
    private JButton btnLimpiarFiltros;

    // Panels de gráficos
    private ChartPanel panelGraficoLineas;
    private ChartPanel panelGraficoPastel;
    private JPanel panelEstadisticas;

    // Labels de estadísticas
    private JLabel lblTotalRecetas;
    private JLabel lblMedicamentosUnicos;
    private JLabel lblPacientesUnicos;
    private JLabel lblCantidadTotal;

    public RecetasChartView(RecetaMedicaChartController controller) {
        this.controller = controller;
        this.controller.setChartView(this);

        inicializarComponentes();
        configurarEventos();
        inicializarDatos();
    }

    private void inicializarComponentes() {
        panelContenido = new JPanel(new BorderLayout());
        panelContenido.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior - Título y controles
        JPanel panelSuperior = createPanelSuperior();

        // Panel central - Gráficos
        JPanel panelGraficos = createPanelGraficos();

        // Panel inferior - Estadísticas
        panelEstadisticas = createPanelEstadisticas();

        panelContenido.add(panelSuperior, BorderLayout.NORTH);
        panelContenido.add(panelGraficos, BorderLayout.CENTER);
        panelContenido.add(panelEstadisticas, BorderLayout.SOUTH);
    }

    private JPanel createPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());

        // Título
        JLabel titulo = new JLabel("Estadísticas de Recetas Médicas");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel de filtros
        JPanel panelFiltros = createPanelFiltros();

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(panelFiltros, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelFiltros() {
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Filtros"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Primera fila - Medicamentos
        JPanel filaMedicamentos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaMedicamentos.add(new JLabel("Medicamentos:"));

        listaMedicamentos = new JList<>();
        listaMedicamentos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaMedicamentos.setVisibleRowCount(3);
        JScrollPane scrollMedicamentos = new JScrollPane(listaMedicamentos);
        scrollMedicamentos.setPreferredSize(new Dimension(300, 60));
        filaMedicamentos.add(scrollMedicamentos);

        // Segunda fila - Fechas
        JPanel filaFechas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaFechas.add(new JLabel("Desde:"));

        Date[] rangoDefecto = controller.getRangoFechasPorDefecto();
        spinnerFechaInicio = new JSpinner(new SpinnerDateModel(
                rangoDefecto[0], null, null, java.util.Calendar.DAY_OF_MONTH));
        spinnerFechaInicio.setEditor(new JSpinner.DateEditor(spinnerFechaInicio, "dd/MM/yyyy"));
        filaFechas.add(spinnerFechaInicio);

        filaFechas.add(new JLabel("Hasta:"));
        spinnerFechaFin = new JSpinner(new SpinnerDateModel(
                rangoDefecto[1], null, null, java.util.Calendar.DAY_OF_MONTH));
        spinnerFechaFin.setEditor(new JSpinner.DateEditor(spinnerFechaFin, "dd/MM/yyyy"));
        filaFechas.add(spinnerFechaFin);

        // Tercera fila - Botones
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));

        btnFiltrar = new JButton("Aplicar Filtros");
        btnFiltrar.setBackground(new Color(76, 175, 80));
        btnFiltrar.setForeground(Color.WHITE);

        btnRefrescar = new JButton("Refrescar Datos");
        btnRefrescar.setBackground(new Color(33, 150, 243));
        btnRefrescar.setForeground(Color.WHITE);

        btnLimpiarFiltros = new JButton("Limpiar Filtros");
        btnLimpiarFiltros.setBackground(new Color(255, 152, 0));
        btnLimpiarFiltros.setForeground(Color.WHITE);

        filaBotones.add(btnFiltrar);
        filaBotones.add(btnRefrescar);
        filaBotones.add(btnLimpiarFiltros);

        panel.add(filaMedicamentos);
        panel.add(filaFechas);
        panel.add(filaBotones);

        return panel;
    }

    private JPanel createPanelGraficos() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));

        // Panel izquierdo - Gráfico de líneas
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(new TitledBorder("Medicamentos Prescritos por Mes"));

        // Crear gráfico inicial vacío
        DefaultCategoryDataset datasetLineas = new DefaultCategoryDataset();
        JFreeChart chartLineas = ChartFactory.createLineChart(
                "Cantidad de Medicamentos por Mes",
                "Mes",
                "Cantidad",
                datasetLineas,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        configurarGraficoLineas(chartLineas);
        panelGraficoLineas = new ChartPanel(chartLineas);
        panelIzquierdo.add(panelGraficoLineas, BorderLayout.CENTER);

        // Panel derecho - Gráfico de pastel
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(new TitledBorder("Recetas por Estado"));

        // Crear gráfico de pastel inicial
        DefaultPieDataset datasetPastel = new DefaultPieDataset();
        JFreeChart chartPastel = ChartFactory.createPieChart(
                "Distribución por Estado",
                datasetPastel,
                true, true, false
        );

        configurarGraficoPastel(chartPastel);
        panelGraficoPastel = new ChartPanel(chartPastel);
        panelDerecho.add(panelGraficoPastel, BorderLayout.CENTER);

        panel.add(panelIzquierdo);
        panel.add(panelDerecho);

        return panel;
    }

    private JPanel createPanelEstadisticas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBorder(new TitledBorder("Estadísticas Generales"));

        // Estadísticas individuales
        lblTotalRecetas = createEstadisticaLabel("Total Recetas", "0");
        lblMedicamentosUnicos = createEstadisticaLabel("Medicamentos Únicos", "0");
        lblPacientesUnicos = createEstadisticaLabel("Pacientes Únicos", "0");
        lblCantidadTotal = createEstadisticaLabel("Cantidad Total", "0");

        panel.add(createEstadisticaPanel(lblTotalRecetas));
        panel.add(createEstadisticaPanel(lblMedicamentosUnicos));
        panel.add(createEstadisticaPanel(lblPacientesUnicos));
        panel.add(createEstadisticaPanel(lblCantidadTotal));

        return panel;
    }

    private JLabel createEstadisticaLabel(String titulo, String valor) {
        JLabel label = new JLabel("<html><center><b>" + titulo + "</b><br><font size='5'>" + valor + "</font></center></html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createEstadisticaPanel(JLabel label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void configurarGraficoLineas(JFreeChart chart) {
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        // Ajustar el rango del eje Y para mostrar mejor los datos
        plot.getRangeAxis().setAutoRange(true);
        plot.getRangeAxis().setLowerMargin(0.1);
        plot.getRangeAxis().setUpperMargin(0.1);

        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void configurarGraficoPastel(JFreeChart chart) {
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        // Colores personalizados para estados típicos
        plot.setSectionPaint("Pendiente", new Color(255, 193, 7));
        plot.setSectionPaint("En Preparación", new Color(33, 150, 243));
        plot.setSectionPaint("Lista", new Color(76, 175, 80));
        plot.setSectionPaint("Entregada", new Color(139, 195, 74));
        plot.setSectionPaint("Cancelada", new Color(244, 67, 54));

        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void configurarEventos() {
        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarFiltros();
            }
        });

        btnRefrescar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refrescarDatos();
            }
        });

        btnLimpiarFiltros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFiltros();
            }
        });
    }

    private void aplicarFiltros() {
        java.util.List<String> medicamentosSeleccionados = listaMedicamentos.getSelectedValuesList();
        Date fechaInicio = (Date) spinnerFechaInicio.getValue();
        Date fechaFin = (Date) spinnerFechaFin.getValue();

        controller.filtrarYActualizar(medicamentosSeleccionados, fechaInicio, fechaFin, null);
        actualizarEstadisticas();
    }

    private void refrescarDatos() {
        controller.refrescarDatos();
        inicializarDatos();
    }

    private void limpiarFiltros() {
        listaMedicamentos.clearSelection();
        Date[] rangoDefecto = controller.getRangoFechasPorDefecto();
        spinnerFechaInicio.setValue(rangoDefecto[0]);
        spinnerFechaFin.setValue(rangoDefecto[1]);

        controller.filtrarYActualizar(new ArrayList<>(), rangoDefecto[0], rangoDefecto[1], null);
        actualizarEstadisticas();
    }

    private void inicializarDatos() {
        // Cargar lista de medicamentos
        List<String> medicamentos = controller.getMedicamentosDisponibles();
        listaMedicamentos.setListData(medicamentos.toArray(new String[0]));

        // Cargar gráficos iniciales
        actualizarTodosLosGraficos();
        actualizarEstadisticas();
    }

    public void actualizarTodosLosGraficos() {
        Date[] rangoDefecto = controller.getRangoFechasPorDefecto();
        controller.actualizarGraficoMedicamentosPorMes(new ArrayList<>(), rangoDefecto[0], rangoDefecto[1]);
        controller.actualizarGraficoPastelEstados();
    }

    public void actualizarGraficoLineas(DefaultCategoryDataset dataset) {
        JFreeChart nuevoChart = ChartFactory.createLineChart(
                "Cantidad de Medicamentos por Mes",
                "Mes",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        configurarGraficoLineas(nuevoChart);
        panelGraficoLineas.setChart(nuevoChart);
    }

    public void actualizarGraficoPastel(DefaultPieDataset dataset) {
        JFreeChart nuevoChart = ChartFactory.createPieChart(
                "Distribución por Estado",
                dataset,
                true, true, false
        );

        configurarGraficoPastel(nuevoChart);
        panelGraficoPastel.setChart(nuevoChart);
    }

    private void actualizarEstadisticas() {
        RecetaMedicaChartController.EstadisticasGenerales stats = controller.getEstadisticasGenerales();

        lblTotalRecetas.setText("<html><center><b>Total Recetas</b><br><font size='5'>" +
                stats.getTotalRecetas() + "</font></center></html>");
        lblMedicamentosUnicos.setText("<html><center><b>Medicamentos Únicos</b><br><font size='5'>" +
                stats.getMedicamentosUnicos() + "</font></center></html>");
        lblPacientesUnicos.setText("<html><center><b>Pacientes Únicos</b><br><font size='5'>" +
                stats.getPacientesUnicos() + "</font></center></html>");
        lblCantidadTotal.setText("<html><center><b>Cantidad Total</b><br><font size='5'>" +
                stats.getCantidadTotalMedicamentos() + "</font></center></html>");
    }

    public JPanel getContentPanel() {
        return panelContenido;
    }

    public java.util.List<String> getMedicamentosSeleccionados() {
        return listaMedicamentos.getSelectedValuesList();
    }

    public Date getFechaInicio() {
        return (Date) spinnerFechaInicio.getValue();
    }

    public Date getFechaFin() {
        return (Date) spinnerFechaFin.getValue();
    }
}
