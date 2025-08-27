package org.example.domain_layer;

public class Paciente {
    String ID;
    String Nombre;
    String Fecha_Nacimiento;
    String Num_Telefono;
    public Paciente(String ID, String Nombre, String Fecha_Nacimiento, String Num_Telefono) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Fecha_Nacimiento = Fecha_Nacimiento;
        this.Num_Telefono = Num_Telefono;
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

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFecha_Nacimiento() {
        return Fecha_Nacimiento;
    }

    public void setFecha_Nacimiento(String fecha_Nacimiento) {
        Fecha_Nacimiento = fecha_Nacimiento;
    }

    public String getNum_Telefono() {
        return Num_Telefono;
    }

    public void setNum_Telefono(String num_Telefono) {
        Num_Telefono = num_Telefono;
    }

}
