package com.sssupply.customerportal.util;
import java.util.Arrays;
import java.util.List;
public class EnumMetaUtil {
    public static List<String> names(Class<? extends Enum<?>> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toList();
    }
}
