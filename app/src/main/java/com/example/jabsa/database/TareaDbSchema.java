package com.example.jabsa.database;

public class TareaDbSchema {

    // Definición de la tabla TareaTable
    public static class TareaTable{

        // Nombre de la tabla
        public static final String NAME = "tareas";

        // Columnas de la tabla TareaTable
        public static final class Cols{
            // Identificador único de la tarea
            public static final String UUID = "uuid";

            // Título de la tarea
            public static final String TITULO = "titulo";

            // Descripción de la tarea
            public static final String DESCRIPCION = "descripcion";

            // Fecha de la tarea
            public static final String FECHA = "fecha";

            // Hora de la tarea
            public static final String HORA = "hora";

            // Indica si la tarea está completada o no
            public static final String COMPLETADO = "completado";

            // Indica si se ha activado la llamada para la tarea
            public static final String LLAMADA_ACTIVADA = "llamada_activada";

            // Nombre del contacto asociado a la tarea
            public static final String CONTACTO = "contacto";

            // Número de teléfono del contacto asociado a la tarea
            public static final String NUMERO = "numero";

            //Categoría de la tarea
            public static final String IDCATEGORIA = "idcategoria";

            //Alarma activada
            public static final String ALARMA_ACTIVADA = "alarma_activada";


        }
    }
}
