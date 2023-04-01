package com.example.jabsa;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    /**
     * Retorna un Bitmap escalado desde un archivo dado en un tamaño de destino dado
     *
     * @param path el camino al archivo de imagen
     * @param destWidth la anchura deseada del Bitmap escalado
     * @param destHeight la altura deseada del Bitmap escalado
     * @return el Bitmap escalado
     */
    public static Bitmap getScaledBitMap(String path, int destWidth, int destHeight){
        // Primero, se necesita obtener la anchura y altura originales del archivo de imagen

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // Se establece un tamaño de muestra inicial de 1, lo que significa que el Bitmap resultante será
        // del mismo tamaño que el archivo de imagen original
        int inSampleSize = 1;

        // Si el archivo de imagen es más grande que el tamaño de destino, se calcula un tamaño de muestra
        // adecuado para reducir el tamaño del Bitmap resultante mientras se mantiene la relación de aspecto

        if(srcHeight > destHeight || srcWidth > destWidth){
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;
            // Se utiliza el factor de escala más grande para calcular el tamaño de muestra adecuado
            inSampleSize = Math.round(heightScale > widthScale ? heightScale:widthScale);
        }
        // Se crea una nueva instancia de BitmapFactory.Options y se establece el tamaño de muestra adecuado
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Se decodifica el archivo de imagen original en un Bitmap escalado usando la configuración de opciones modificada
        return BitmapFactory.decodeFile(path, options);
    }
    /**
     * Retorna un Bitmap escalado desde un archivo dado en un tamaño de pantalla del dispositivo
     *
     * @param path el camino al archivo de imagen
     * @param activity la actividad actual
     * @return el Bitmap escalado
     */
    public static Bitmap getScaledBitMap(String path, Activity activity){
        // Se obtiene el tamaño de pantalla actual del dispositivo
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        // Se llama al método getScaledBitmap anterior, pasando el tamaño de pantalla como el tamaño de destino
        return getScaledBitMap(path, size.x, size.y);
    }
}
