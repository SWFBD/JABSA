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
        for(Map.Entry<String, String> entry:codeToNameMap.entrySet()){
            if(entry.getValue().equals(categoryName)){
                categoryCode = entry.getKey();
                break;
            }
        }
        return categoryCode;
    }
}
