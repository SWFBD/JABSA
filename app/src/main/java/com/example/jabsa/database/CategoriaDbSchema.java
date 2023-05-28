/*
* En este código, se define la estructura de la tabla "categorias"
* en la base de datos. La clase interna Cols enumera los nombres
* de las columnas UUID y NOMBRE utilizadas en la tabla.
* */
package com.example.jabsa.database;

public class CategoriaDbSchema {
    public static class CategoriaTable{

        public static final String NAME = "categorias";

        public static final class Cols{
            public static final String UUID = "uuid"; // Nombre de la columna UUID
            public static final String NOMBRE = "nombre"; // Nombre de la columna NOMBRE
        }
    }
}
