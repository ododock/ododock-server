package ododock.webserver.web.exceptionhandler.response;

import com.mongodb.lang.Nullable;

public record Status(String type, @Nullable Object detail) {
}
