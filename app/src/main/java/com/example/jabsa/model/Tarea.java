package com.example.jabsa.model;

import java.util.Date;
import java.util.UUID;

public class Tarea {

    // Atributos de la clase Tarea
    private UUID mId;
    private String mTitulo;
    private String mDescripcion;
    private Date mFecha;
    private String mHora;
    private boolean mCompletado;
    private boolean mLlamada_activada;
    private String mContacto;
    private String mNumero;

    // Constructor de la clase Tarea
    public Tarea(){
        this(UUID.randomUUID());
    }


    // Constructor sobrecargado de la clase Tarea
    public Tarea(UUID randomUUID){
        // Asigna el UUID proporcionado al atributo mId
        mId = randomUUID;
        // Inicializa el atributo mFecha con la fecha actual
        mFecha = new Date();
    }
    // Métodos getters y setters para los atributos de la clase Tarea
    public UUID getmId() {
        return mId;
    }
    public String getmTitulo() {
        return mTitulo;
    }

    public void setmTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getmDescripcion() {
        return mDescripcion;
    }

    public void setmDescripcion(String mDescripcion) {
        this.mDescripcion = mDescripcion;
    }

    public Date getmFecha() {
        return mFecha;
    }

    public void setmFecha(Date mFecha) {
        this.mFecha = mFecha;
    }

    public String getmHora() {
        return mHora;
    }

    public void setmHora(String mHora) {
        this.mHora = mHora;
    }

    public Boolean getmCompletado() {
        return mCompletado;
    }

    public void setmCompletado(Boolean mCompletado) {
        this.mCompletado = mCompletado;
    }

    public Boolean getmLlamada_activada() {
        return mLlamada_activada;
    }

    public void setmLlamada_activada(Boolean mLlamada_activada) {
        this.mLlamada_activada = mLlamada_activada;
    }

    public String getmContacto() {
        return mContacto;
    }

    public void setmContacto(String mContacto) {
        this.mContacto = mContacto;
    }

    public String getmNumero() {
        return mNumero;
    }

    public void setmNumero(String mNumero) {
        this.mNumero = mNumero;
    }

    // Método que devuelve el nombre del archivo de foto para la tarea
    public String getPhotoFileName(){
        return "IMG_"+getmId().toString()+"jpg";
    }
}
