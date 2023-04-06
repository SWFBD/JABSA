package com.example.jabsa.model;

import static com.example.jabsa.database.TareaDbSchema.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.example.jabsa.database.TareaBaseHelper;
import com.example.jabsa.database.TareaCursorWrapper;
import com.example.jabsa.database.TareaDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Esta clase maneja las operaciones de la base de datos para la entidad Tarea

public class TareaLab {

    private static TareaLab sTareaLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    // El método get() devuelve la instancia única de TareaLab

    public static TareaLab get(Context context){
        if(sTareaLab == null){
            sTareaLab = new TareaLab(context);
        }
        return sTareaLab;
    }

    // Constructor privado para evitar la creación de múltiples instancias de TareaLab

    private TareaLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new TareaBaseHelper(mContext).getWritableDatabase();

    }
    // Este método devuelve una lista de todas las tareas

    public List<Tarea> getTareas(){
        List<Tarea> tareas = new ArrayList<>();

        // Realiza la consulta a la base de datos
        TareaCursorWrapper cursor = queryTareas(null, null);

        try{
            // Itera sobre el cursor y agrega cada tarea a la lista

            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                tareas.add(cursor.getTarea());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }

        return tareas;
    }
    // Este método devuelve una tarea por ID

    public Tarea getTarea(UUID id){
        TareaCursorWrapper cursor = queryTareas(
                TareaTable.Cols.UUID +"=?",
                new String[]{id.toString()}
        );
        try{
            // Si la consulta no devuelve ningún resultado, devuelve null

            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            // Devuelve la tarea obtenida de la consulta

            return cursor.getTarea();
        }finally{
            cursor.close();
        }
    }
    // Este método devuelve el archivo de foto de una tarea

    public File getPhotoFile(Tarea tarea){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, tarea.getPhotoFileName());
    }
    // Este método agrega una nueva tarea a la base de datos

    public void addTarea(Tarea tarea){
        ContentValues values = getContentValues(tarea);
        mDatabase.insert(TareaTable.NAME, null, values);
    }
    // Este método actualiza una tarea existente en la base de datos

    public void updateTarea(Tarea tarea){
        String uuidString = tarea.getmId().toString();
        ContentValues values = getContentValues(tarea);

        mDatabase.update(TareaTable.NAME, values,
                TareaTable.Cols.UUID +"=?",
                new String[]{
                        uuidString
                });
    }
    // Este método devuelve un objeto ContentValues con los valores de la tarea

    private static ContentValues getContentValues(Tarea tarea){
        ContentValues values = new ContentValues();
        values.put(TareaTable.Cols.UUID, tarea.getmId().toString());
        values.put(TareaTable.Cols.TITULO, tarea.getmTitulo());
        values.put(TareaTable.Cols.DESCRIPCION, tarea.getmDescripcion());
        values.put(TareaTable.Cols.FECHA, tarea.getmFecha().getTime());
        values.put(TareaTable.Cols.HORA, tarea.getmHora());
        values.put(TareaTable.Cols.COMPLETADO, tarea.getmCompletado()?1:0);
        values.put(TareaTable.Cols.LLAMADA_ACTIVADA, tarea.getmLlamada_activada()?1:0);
        values.put(TareaTable.Cols.CONTACTO, tarea.getmContacto());
        values.put(TareaTable.Cols.NUMERO, tarea.getmNumero());
        values.put(TareaTable.Cols.IDCATEGORIA, tarea.getmIdCategoria());
        values.put(TareaTable.Cols.ALARMA_ACTIVADA, tarea.getmAlarma_activada()?1:0);
        return values;
    }

    // Este método recupera un conjunto de tareas de la base de datos que coinciden con la cláusula "whereClause"
    // y los argumentos "whereArgs". Devuelve un objeto TareaCursorWrapper que envuelve un cursor que apunta
    // a las filas de la base de datos que coinciden con la consulta.
    private TareaCursorWrapper queryTareas(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TareaTable.NAME, // Tabla de la que se obtendrán los datos.
                null, // Columnas que se recuperarán (null para todas).
                whereClause, // Cláusula WHERE para filtrar las filas que se quieren obtener.
                whereArgs,  // Argumentos de la cláusula WHERE.
                null, // GROUP BY (no se usa en esta consulta).
                null, // HAVING (no se usa en esta consulta).
                null // ORDER BY (no se usa en esta consulta).
        );
        return new TareaCursorWrapper(cursor); // Se devuelve un nuevo objeto TareaCursorWrapper con el cursor obtenido.
    }

    // Este método elimina una tarea de la base de datos. Toma una tarea como argumento y utiliza su ID para identificar
    // la fila que debe eliminarse de la tabla. El método "delete" de SQLiteDatabase se utiliza para realizar la eliminación.
    public void deleteTask(Tarea tarea){
        String uuidString = tarea.getmId().toString(); // Se obtiene el ID de la tarea.
        mDatabase.delete(TareaTable.NAME, // Tabla de la que se eliminará la fila.
                TareaTable.Cols.UUID+  // Cláusula WHERE que indica la fila a eliminar.
                "= ?", new String[]{uuidString}); // Argumentos de la cláusula WHERE (en este caso, el ID de la tarea).
    }


}
