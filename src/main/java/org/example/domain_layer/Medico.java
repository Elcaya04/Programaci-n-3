package org.example.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="medico")
@XmlAccessorType(XmlAccessType.FIELD)
public class Medico {
    @XmlElement(name = "id")
    String ID;

    @XmlElement(name = "nombre")
    String Nombre;

    @XmlElement(name = "especialidad")
    String Especialidad;

    @XmlElement(name = "clave")
    String Clave;

    public Medico() {}


    public Medico(String ID, String Nombre, String Especialidad,String Clave) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Especialidad = Especialidad;
        this.Clave = ID;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    public String getEspecialidad() {
        return Especialidad;
    }
    public void setEspecialidad(String Especialidad) {
        this.Especialidad = Especialidad;
    }
    public String getClave() {
        return Clave;
    }
    public void setClave(String Clave) {
        this.Clave = Clave;
    }

}

