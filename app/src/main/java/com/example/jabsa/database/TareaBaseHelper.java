package com.example.jabsa.database;

import static com.example.jabsa.database.TareaDbSchema.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TareaBaseHelper extends SQLiteOpenHelper {

    // Definimos la versión de la base de datos y el nombre del archivo de base de datos
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "tareaBase.db";

    // Constructor de la clase, que recibe el contexto de la aplicación
    public TareaBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Método que se llama al crear la base de datos por primera vez
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ejecutamos una sentencia SQL para crear la tabla de tareas, especificando los nombres de las columnas y sus tipos de datos
        db.execSQL("create table "+ TareaTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                TareaTable.Cols.UUID+", "+
                TareaTable.Cols.TITULO+", "+
                TareaTable.Cols.DESCRIPCION+", "+
                TareaTable.Cols.FECHA+", "+
                TareaTable.Cols.HORA+", "+
                TareaTable.Cols.COMPLETADO+", "+
                TareaTable.Cols.LLAMADA_ACTIVADA+", "+
                TareaTable.Cols.CONTACTO+", "+
                TareaTable.Cols.IDCATEGORIA+", "+
                TareaTable.Cols.ALARMA_ACTIVADA+", "+
                TareaTable.Cols.NUMERO+ ")"
        );

        db.execSQL(
                "create table "+ CategoriaDbSchema.CategoriaTable.NAME + "(" +
                        "_id integer primary key autoincrement, "+
                        CategoriaDbSchema.CategoriaTable.Cols.UUID+", "+
                        CategoriaDbSchema.CategoriaTable.Cols.NOMBRE+")"
        );
    }
    // Método que se llama al actualizar la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hacemos nada aquí, ya que esta es la primera versión de la base de datos
    }
}
