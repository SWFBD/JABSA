package com.example.jabsa.model;

import java.util.UUID;

public class Categoria {
    private UUID mId;
    private String mNombre;

    public Categoria(){
        this(UUID.randomUUID());
    }

    public Categoria(UUID randomUUID){
        mId = randomUUID;
    }

    public String getmNombre() {
        return mNombre;
    }

    public void setmNombre(String mNombre) {
        this.mNombre = mNombre;
    }

    public UUID getmId() {
        return mId;
    }
}
