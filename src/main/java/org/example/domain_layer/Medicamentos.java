package org.example.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="medicamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class Medicamentos {
    @XmlElement(name = "codigo")
    String Codigo;
    @XmlElement(name = "nombre")
    String Nombre;
    @XmlElement(name = "presentacion")
    String Presentacion;
    public Medicamentos() {}

    public Medicamentos(String codigo, String nombre, String presentacion) {
        Codigo = codigo;
        Nombre = nombre;
        Presentacion = presentacion;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPresentacion() {
        return Presentacion;
    }

    public void setPresentacion(String presentacion) {
        Presentacion = presentacion;
    }

}
