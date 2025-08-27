package org.example.domain_layer;

public class Farmaceuta {
    String ID;
    String Especialidad;
    String Clave;
    public Farmaceuta(String ID, String Especialidad, String Clave) {
        this.ID = ID;
        this.Especialidad = Especialidad;
        this.Clave = Clave;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
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
