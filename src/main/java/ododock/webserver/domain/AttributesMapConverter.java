package ododock.webserver.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
public class AttributesMapConverter implements AttributeConverter<Map<String, List<String>>, String> {

    @Override
    public String convertToDatabaseColumn(final Map<String, List<String>> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return null;
        }
        return attributes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey()+"="+entry.getValue().stream()
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining(";"));
    }

    @Override
    public Map<String, List<String>> convertToEntityAttribute(final String attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return null;
        }
        return Arrays.stream(attributes.split(";"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(
                        entry -> entry[0],
                        entry -> Arrays.stream(entry[1].split(","))
                                .collect(Collectors.toList())
                ));
    }

}
