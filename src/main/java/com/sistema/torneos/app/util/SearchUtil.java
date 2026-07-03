package com.sistema.torneos.app.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad para búsqueda genérica en listas de objetos.
 * Busca por coincidencia de texto en todas las propiedades del objeto.
 */
public class SearchUtil {

    /**
     * Busca objetos que coincidan con la query en cualquiera de sus propiedades.
     * 
     * @param items Lista de objetos a buscar
     * @param query Texto de búsqueda (case-insensitive)
     * @return Lista de objetos que coinciden con la búsqueda
     */
    public static <T> List<T> search(List<T> items, String query) {
        if (query == null || query.trim().isEmpty() || items == null) {
            return items != null ? items : new ArrayList<>();
        }

        String lowerQuery = query.toLowerCase().trim();
        List<T> results = new ArrayList<>();

        for (T item : items) {
            if (matchesQuery(item, lowerQuery)) {
                results.add(item);
            }
        }

        return results;
    }

    /**
     * Verifica si un objeto coincide con la query.
     */
    private static boolean matchesQuery(Object item, String lowerQuery) {
        if (item == null) {
            return false;
        }

        // Obtener todas las propiedades del objeto usando reflexión
        Field[] fields = item.getClass().getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(item);

                if (value != null) {
                    String stringValue = value.toString().toLowerCase();
                    if (stringValue.contains(lowerQuery)) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                // Continuar con el siguiente campo
            }
        }

        return false;
    }
}
