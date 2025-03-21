package ododock.webserver.web;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class IllegalPropertyException extends IllegalArgumentException {

    private static final String MESSAGE_FORMAT = "objectClass [%s], propertyPath [%s], propertyErrorMessage [%s]";

    @NonNull
    private final Class<?> objectClass;
    @NonNull
    private final String propertyPath;
    @NonNull
    private final String propertyErrorMessage;

    public IllegalPropertyException(Class<?> objectClass, String propertyPath, String propertyErrorMessage) {
        super(String.format(MESSAGE_FORMAT, objectClass.getSimpleName(), propertyPath, propertyErrorMessage));
        this.objectClass = objectClass;
        this.propertyPath = propertyPath;
        this.propertyErrorMessage = propertyErrorMessage;
    }

}
