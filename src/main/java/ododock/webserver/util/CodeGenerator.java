package ododock.webserver.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;

public class CodeGenerator {

    private static final Random RANDOM = new SecureRandom();

    public static String generateCode(final int length) {
        return RANDOM.ints(length, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

}
