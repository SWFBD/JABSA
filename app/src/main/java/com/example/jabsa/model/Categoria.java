/*
* En este código, se define la clase Categoria que representa
* un objeto de categoría con un identificador único (mId)
* y un nombre (mNombre).
* Se proporcionan métodos para obtener y establecer el nombre
* de la categoría, así como obtener el identificador único de la categoría.

 * */
package com.example.jabsa.model;

import java.util.UUID;

public class Categoria {
    private UUID mId; // Identificador único de la categoría
    private String mNombre; // Nombre de la categoría

    public Categoria(){
        // Generar un UUID aleatorio al crear una nueva categoría sin argumentos
        this(UUID.randomUUID());
    }

    public Categoria(UUID randomUUID){
        // Asignar el UUID proporcionado al identificador de la categoría
        mId = randomUUID;
    }

    public String getmNombre() {
        // Obtener el nombre de la categoría
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        // Establecer el nombre de la categoría
        this.mNombre = mNombre;
    }

    public UUID getmId() {
        // Obtener el identificador único de la categoría
        return mId;
    }
}
