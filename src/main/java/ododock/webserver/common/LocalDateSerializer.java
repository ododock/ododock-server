package ododock.webserver.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonComponent
public class LocalDateSerializer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static class Serializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(
                final LocalDate value,
                final JsonGenerator gen,
                final SerializerProvider pvd) throws IOException {
            gen.writeString(value.format(FORMATTER));
        }
    }

    public static class Deserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(
                final JsonParser p,
                final DeserializationContext ctx
        ) throws IOException {
            return LocalDate.parse(p.getValueAsString(), FORMATTER);
        }
    }
}
