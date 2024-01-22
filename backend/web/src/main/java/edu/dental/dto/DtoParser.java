package edu.dental.dto;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class DtoParser {

    public static <ENTITY, DTO> ENTITY revertDto(ENTITY entity, DTO dto) throws ReflectiveOperationException {
        //get field of dto object to concatenate setters and getters by them
        Field[] fields = dto.getClass().getFields();
        Method getter;
        Method setter;

        for (Field field : fields) {

            getter = dto.getClass().getMethod(concatenateMethod(field, "get"));

            if (field.getType().getName().endsWith("Dto")) {

                //get field name of dto object and cut its name tail for getting field name of entity
                String fieldName = field.getName().substring(0, field.getName().length() - "Dto".length());
                Field entityField = entity.getClass().getField(fieldName);
                //if field value type of dto object is dto self, necessary
                // to get filed value type of entity, because its type is different.
                setter = entity.getClass().getMethod(concatenateMethod(field, "set"), entityField.getType());

                if (field.getType().isArray()) {

                    Object[] value = (Object[]) getter.invoke(dto);
                    setter.invoke(entity, (Object[]) revertDtoArray(field.getType(), value));

                } else {

                    setter.invoke(entity, revertDto(createInstance(field.getType()), getter.invoke(dto)));
                }

            } else {

                setter = entity.getClass().getMethod(concatenateMethod(field, "set"), field.getType());
                setter.invoke(entity, getter.invoke(dto));
            }
        }
        return entity;
    }
    //TODO!!!!

    private static <ENTITY, DTO> ENTITY[] revertDtoArray(Class<ENTITY> clas, DTO[] dtoArray) throws ReflectiveOperationException {
        ArrayList<ENTITY> entities = new ArrayList<>();
        for (DTO dto : dtoArray) {
            entities.add(revertDto(createInstance(clas), dto));
        }
        @SuppressWarnings("unchecked")
        ENTITY[] entitiesArray = (ENTITY[]) new Object[]{};
        return entities.toArray(entitiesArray);
    }

    private static <ENTITY> ENTITY createInstance(Class<ENTITY> clas) throws ReflectiveOperationException {
        Constructor<ENTITY> constructor = clas.getConstructor();
        return constructor.newInstance();
    }

    private static String concatenateMethod(Field field, String method) {
        String fieldName = field.getName();
        char firstLetter = (char) (fieldName.charAt(0) - 32);
        StringBuilder str = new StringBuilder(fieldName);
        str.setCharAt(0, firstLetter);
        str.insert(0, method, 0, method.length());
        return str.toString();
    }
}
