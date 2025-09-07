package org.example.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="receta")
@XmlAccessorType(XmlAccessType.FIELD)
public class RecetaMedica {

    @XmlElement(name = "numeroReceta")
    String NumeroReceta;

    @XmlElement(name = "fechaPrescripcion")
    String FechaPrescripcion;

    @XmlElement(name = "fechaRetiro")
    String FechaRetiro;

    @XmlElement(name = "medicoId")
    String MedicoId;

    @XmlElement(name = "pacienteId")
    String PacienteId;

    @XmlElement(name = "pacienteNombre")
    String PacienteNombre;

    @XmlElement(name = "codigoMedicamento")
    String CodigoMedicamento;

    @XmlElement(name = "nombreMedicamento")
    String NombreMedicamento;

    @XmlElement(name = "presentacion")
    String Presentacion;

    @XmlElement(name = "cantidad")
    int Cantidad;

    @XmlElement(name = "duracionDias")
    int DuracionDias;

    @XmlElement(name = "indicaciones")
    String Indicaciones;

    @XmlElement(name = "estado")
    String Estado; // "PENDIENTE", "DESPACHADA", "VENCIDA"

    @XmlElement(name = "farmaceutaDespacho")
    String FarmaceutaDespacho;

    public RecetaMedica() {}

    public RecetaMedica(String numeroReceta, String fechaPrescripcion, String fechaRetiro,
                        String medicoId, String pacienteId, String pacienteNombre,
                        String codigoMedicamento, String nombreMedicamento, String presentacion,
                        int cantidad, int duracionDias, String indicaciones) {
        NumeroReceta = numeroReceta;
        FechaPrescripcion = fechaPrescripcion;
        FechaRetiro = fechaRetiro;
        MedicoId = medicoId;
        PacienteId = pacienteId;
        PacienteNombre = pacienteNombre;
        CodigoMedicamento = codigoMedicamento;
        NombreMedicamento = nombreMedicamento;
        Presentacion = presentacion;
        Cantidad = cantidad;
        DuracionDias = duracionDias;
        Indicaciones = indicaciones;
        Estado = "PENDIENTE";
    }

    // Getters y Setters
    public String getNumeroReceta() { return NumeroReceta; }
    public void setNumeroReceta(String numeroReceta) { NumeroReceta = numeroReceta; }

    public String getFechaPrescripcion() { return FechaPrescripcion; }
    public void setFechaPrescripcion(String fechaPrescripcion) { FechaPrescripcion = fechaPrescripcion; }

    public String getFechaRetiro() { return FechaRetiro; }
    public void setFechaRetiro(String fechaRetiro) { FechaRetiro = fechaRetiro; }

    public String getMedicoId() { return MedicoId; }
    public void setMedicoId(String medicoId) { MedicoId = medicoId; }

    public String getPacienteId() { return PacienteId; }
    public void setPacienteId(String pacienteId) { PacienteId = pacienteId; }

    public String getPacienteNombre() { return PacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { PacienteNombre = pacienteNombre; }

    public String getCodigoMedicamento() { return CodigoMedicamento; }
    public void setCodigoMedicamento(String codigoMedicamento) { CodigoMedicamento = codigoMedicamento; }

    public String getNombreMedicamento() { return NombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { NombreMedicamento = nombreMedicamento; }

    public String getPresentacion() { return Presentacion; }
    public void setPresentacion(String presentacion) { Presentacion = presentacion; }

    public int getCantidad() { return Cantidad; }
    public void setCantidad(int cantidad) { Cantidad = cantidad; }

    public int getDuracionDias() { return DuracionDias; }
    public void setDuracionDias(int duracionDias) { DuracionDias = duracionDias; }

    public String getIndicaciones() { return Indicaciones; }
    public void setIndicaciones(String indicaciones) { Indicaciones = indicaciones; }

    public String getEstado() { return Estado; }
    public void setEstado(String estado) { Estado = estado; }

    public String getFarmaceutaDespacho() { return FarmaceutaDespacho; }
    public void setFarmaceutaDespacho(String farmaceutaDespacho) { FarmaceutaDespacho = farmaceutaDespacho; }

    @Override
    public String toString() {
        return String.format("Receta %s - %s para %s", NumeroReceta, NombreMedicamento, PacienteNombre);
    }
}
