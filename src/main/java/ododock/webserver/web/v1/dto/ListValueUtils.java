package ododock.webserver.web.v1.dto;

import java.time.LocalDate;
import java.util.List;

public abstract class ListValueUtils {

    public static List<String> getStringValues(LocalDate date) {
        return List.of(date.toString());
    }

    public static List<String> getStringValues(List<Long> values) {
        return values.stream().map(String::valueOf).toList();
    }

}
