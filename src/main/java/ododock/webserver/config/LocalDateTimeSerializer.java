package ododock.webserver.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class LocalDateTimeSerializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static class Serializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(
                final LocalDateTime value,
                final JsonGenerator gen,
                final SerializerProvider pvd
        ) throws IOException {
            gen.writeString(value.format(FORMATTER));
        }
    }

    public static class Desrializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(
                final JsonParser p,
                final DeserializationContext ctx
        ) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), FORMATTER);
        }
    }
}
