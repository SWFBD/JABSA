package com.example.jabsa.database;

import static com.example.jabsa.database.TareaDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.jabsa.model.Tarea;

import java.util.Date;
import java.util.UUID;

public class TareaCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */

    // Constructor de la clase que recibe un cursor y lo envía al constructor de la superclase
    public TareaCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // Método que devuelve una Tarea a partir de los datos obtenidos del cursor
    public Tarea getTarea(){

        // Obtenemos los valores de cada columna del cursor a partir de su nombre
        String uuidString = getString(getColumnIndex(TareaTable.Cols.UUID));
        String titulo = getString(getColumnIndex(TareaTable.Cols.TITULO));
        String descripcion = getString(getColumnIndex(TareaTable.Cols.DESCRIPCION));
        long fecha = getLong(getColumnIndex(TareaTable.Cols.FECHA));
        String hora = getString(getColumnIndex(TareaTable.Cols.HORA));
        int esCompletado = getInt(getColumnIndex(TareaTable.Cols.COMPLETADO));
        int esLlamada_activada = getInt(getColumnIndex(TareaTable.Cols.LLAMADA_ACTIVADA));
        String contacto = getString(getColumnIndex(TareaTable.Cols.CONTACTO));
        String numero = getString(getColumnIndex(TareaTable.Cols.NUMERO));

        // Creamos una nueva instancia de Tarea a partir del UUID

        Tarea tarea = new Tarea(UUID.fromString(uuidString));

        // Establecemos los valores de cada atributo de la tarea a partir de los valores obtenidos del cursor

        tarea.setmTitulo(titulo);
        tarea.setmDescripcion(descripcion);
        tarea.setmFecha(new Date(fecha));
        tarea.setmHora(hora);
        tarea.setmCompletado(esCompletado != 0);
        tarea.setmLlamada_activada(esLlamada_activada != 0);
        tarea.setmContacto(contacto);
        tarea.setmNumero(numero);

        // Devolvemos la tarea creada
        return tarea;

    }
}
