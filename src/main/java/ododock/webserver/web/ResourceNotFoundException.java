package ododock.webserver.web;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "resourceType [%s], resourceProperty [%s]";

    private final String resourceType;
    private final String resourceProperty;

    public ResourceNotFoundException(Class<?> resourceClass, String resourceProperty) {
        super(String.format(MESSAGE_FORMAT, resourceClass.getSimpleName(), resourceProperty));
        this.resourceType = resourceClass.getSimpleName();  // 클래스 이름을 문자열로 저장
        this.resourceProperty = resourceProperty;
    }

    public ResourceNotFoundException(Class<?> resourceClass, Long resourceProperty) {
        super(String.format(MESSAGE_FORMAT, resourceClass.getSimpleName(), resourceProperty));
        this.resourceType = resourceClass.getSimpleName();  // 클래스 이름을 문자열로 저장
        this.resourceProperty = String.valueOf(resourceProperty);
    }

}
