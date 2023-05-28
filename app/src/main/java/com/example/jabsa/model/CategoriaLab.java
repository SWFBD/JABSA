package com.example.jabsa.model;

import static com.example.jabsa.database.CategoriaDbSchema.*;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jabsa.database.CategoriaCursorWrapper;
import com.example.jabsa.database.TareaBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoriaLab {

    private static CategoriaLab sCategoriaLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CategoriaLab get(Context context){
        if(sCategoriaLab == null){
            sCategoriaLab = new CategoriaLab(context);
        }
        return sCategoriaLab;
    }

    private CategoriaLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new TareaBaseHelper(mContext).getWritableDatabase();
    }

    public List<Categoria> getCategorias(){
        List<Categoria> categorias = new ArrayList<>();
        boolean bandera = false;
        CategoriaCursorWrapper cursor = queryCategorias(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                categorias.add(cursor.getCategoria());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        for(Categoria categoria : categorias){
            if(categoria.getmNombre().equals("Sin categoria")){
                bandera = true;
            }
        }
        if(bandera == false){
            Categoria sinCategoria = new Categoria();
            sinCategoria.setmNombre("Sin categoria");
            categorias.add(sinCategoria);
            addCategoria(sinCategoria);
        }
        return categorias;
    }

    public Categoria getCategoria(UUID id){
        CategoriaCursorWrapper cursor = queryCategorias(
                CategoriaTable.Cols.UUID + "=?",
                new String[] {id.toString()}
        );
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();

            return cursor.getCategoria();
        }finally {
            cursor.close();
        }
    }

    public void addCategoria(Categoria categoria){
        ContentValues values = getContentValues(categoria);
        mDatabase.insert(CategoriaTable.NAME, null, values);
    }

    public void updateCategoria(Categoria categoria){
        String uuidString = categoria.getmId().toString();
        ContentValues values = getContentValues(categoria);

        mDatabase.update(CategoriaTable.NAME, values, CategoriaTable.Cols.UUID +"=?",
                new String[]{
                   uuidString
                });
    }
    private static ContentValues getContentValues(Categoria categoria){
        ContentValues values = new ContentValues();
        values.put(CategoriaTable.Cols.UUID, categoria.getmId().toString());
        values.put(CategoriaTable.Cols.NOMBRE, categoria.getmNombre());
        return values;
    }
    private CategoriaCursorWrapper queryCategorias(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CategoriaTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CategoriaCursorWrapper(cursor);
    }

    public void deleteCategoria(Categoria categoria){
        String uuidString = categoria.getmId().toString();
        mDatabase.delete(CategoriaTable.NAME,
                CategoriaTable.Cols.UUID+
                "= ?",
                new String[]{
                        uuidString
                });
    }


}
