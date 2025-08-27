package org.example.domain_layer;

public class Medicamentos {
    String Codigo;
    String Nombre;
    String Presentacion;

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
