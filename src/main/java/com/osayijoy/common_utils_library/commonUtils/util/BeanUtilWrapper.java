package com.osayijoy.common_utils_library.commonUtils.util;


import static java.util.Objects.isNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;

/**
 * @author Monsuru <br/>
 * @since Sep-09(Fri)-2022
 */
@UtilityClass
public class BeanUtilWrapper {

    public void copyNonNullProperties(Object source, Object target) {
        String[] nullFields = getNullFields(source);
        BeanUtils.copyProperties(source, target, nullFields);
    }

    private static String[] getNullFields(Object source) {
        Field[] declaredFields = source.getClass().getDeclaredFields();
        List<String> emptyFieldNames = Arrays.stream(declaredFields).filter(field -> {
            field.setAccessible(true);
            Object o;
            try {
                o = field.get(source);
            } catch (IllegalAccessException e) {
                throw new ZeusRuntimeException(e.getMessage(),e);
            }
            return isNull(o);
        }).map(Field::getName).toList();

        String[] emptyFieldsArray = new String[emptyFieldNames.size()];
        return emptyFieldNames.toArray(emptyFieldsArray);
    }
}
