package com.example.jabsa.database;

import static com.example.jabsa.database.CategoriaDbSchema.*;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.jabsa.model.Categoria;

import java.util.UUID;

public class CategoriaCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CategoriaCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Categoria getCategoria(){
        String uuidString = getString(getColumnIndex(CategoriaTable.Cols.UUID));
        String nombre = getString(getColumnIndex(CategoriaTable.Cols.NOMBRE));

        Categoria categoria = new Categoria(UUID.fromString(uuidString));

        categoria.setmNombre(nombre);

        return categoria;
    }
}
