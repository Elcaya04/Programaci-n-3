package org.example.domain_layer;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="farmaceuta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta {
    @XmlElement(name = "id")
    String ID;
    @XmlElement(name = "clave")
    String Clave;
    @XmlElement(name = "nombre")
    String Nombre;
    public Farmaceuta() {}
    public Farmaceuta(String ID, String Clave,String Nombre) {
        this.ID = ID;
        this.Clave = ID;
        this.Nombre = Nombre;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getClave() {
        return Clave;
    }
    public void setClave(String Clave) {
        this.Clave =Clave;
    }
    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
