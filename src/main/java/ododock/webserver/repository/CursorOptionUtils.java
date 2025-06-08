package ododock.webserver.repository;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class CursorOptionUtils {

    private static final String DESC_PREFIX = "-";

    public static Map<String, Sort.Direction> parse(String sortParam) throws BadRequestException {
        Map<String, Sort.Direction> sortMap = new HashMap<>();
        StringUtils.commaDelimitedListToSet(sortParam).
                forEach(param -> {
                    if (param.startsWith(DESC_PREFIX)) {
                        String token = param.substring(1, param.length());
                        sortMap.put(token, Sort.Direction.DESC);
                    } else {
                        sortMap.put(param, Sort.Direction.ASC);
                    }
                });
        return sortMap;
    }

    public static Map<String, Sort.Direction> getDefaultSort() {
        Map<String, Sort.Direction> defaultSort = new HashMap<>();
        defaultSort.put("createdAt", Sort.Direction.DESC);
        return defaultSort;
    }

}
