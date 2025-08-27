package org.example.data_access_layer;

import java.util.List;

public interface FileStore<T> {
    List<T> Leer();
    void Escribir(List<T> lista);

}
