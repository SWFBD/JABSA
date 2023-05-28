package com.example.jabsa.database;

import static com.example.jabsa.database.CategoriaDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.jabsa.model.Categoria;

import java.util.UUID;

public class CategoriaCursorWrapper extends CursorWrapper {

    public CategoriaCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Categoria getCategoria(){
        // Obtener los valores de las columnas del cursor
        String uuidString = getString(getColumnIndex(CategoriaTable.Cols.UUID));
        String nombre = getString(getColumnIndex(CategoriaTable.Cols.NOMBRE));

        // Crear una nueva instancia de la clase Categoria utilizando el UUID extraído del cursor
        Categoria categoria = new Categoria(UUID.fromString(uuidString));
        // Establecer el nombre de la categoría utilizando el valor extraído del cursor
        categoria.setmNombre(nombre);

        return categoria;
    }
}
