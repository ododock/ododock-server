package ododock.webserver;

import ododock.webserver.domain.AttributesMapConverter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TempTest {

    AttributesMapConverter converter = new AttributesMapConverter();

    @Test
    void test() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("key1", List.of("val1","val1","val1"));
        map.put("key2", List.of("val2"));
        System.out.println("map disp");
        System.out.println(map);
        System.out.println("converter result");
        String result = converter.convertToDatabaseColumn(map);
        System.out.println(result);

        System.out.println(converter.convertToEntityAttribute(result));
    }

    @Test
    void test1() {
//        String gen = UUID.randomUUID().toString();
//        System.out.println(gen);
//        System.out.println(gen.split("-")[0]);
//        System.out.println(gen.split("-",12)[0]);
        String url = "http://test.io/userimage.png";
        String format = url.substring(url.lastIndexOf('.')+1);
        System.out.println(format);

    }
}
