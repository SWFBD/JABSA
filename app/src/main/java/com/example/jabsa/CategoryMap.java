/*
* En este código, se define la clase CategoryMap que crea un mapa
*  de correspondencia entre códigos de categoría y nombres de categoría.
*  El constructor toma dos listas, una para los códigos de categoría
* y otra para los nombres de categoría, y los agrega al mapa
* codeToNameMap. Los métodos getCategoryName y getCategoryCode
* permiten obtener el nombre de la categoría en base a su código
* y viceversa, respectivamente.
*
* */
package com.example.jabsa;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryMap {

    private Map<String, String> codeToNameMap;

    public CategoryMap(List<String> codes, List<String> names){
        codeToNameMap = new HashMap<String, String>();
        //  Iterar las dos listas y agregar los elementos al mapa
        for(int i = 0; i < codes.size(); i++){
            codeToNameMap.put(codes.get(i), names.get(i));
        }
    }

    public String getCategoryName(String categoryCode){
        // Obtener el nombre de la categoria en base a su codigo
        return codeToNameMap.get(categoryCode);
    }

    public String getCategoryCode(String categoryName){
        String categoryCode = "";
        // Iterar sobre las entradas del mapa y buscar el nombre de la categoría
        for(Map.Entry<String, String> entry:codeToNameMap.entrySet()){
            if(entry.getValue().equals(categoryName)){
                categoryCode = entry.getKey();
                break;
            }
        }
        return categoryCode;
    }
}
