package ododock.webserver.web;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class InvalidParameterException extends IllegalArgumentException {

    @NonNull
    private final String parameterName;
    @Nullable
    private final String errorMessage;

    public InvalidParameterException(String parameterName, @Nullable String errorMessage) {
        super(String.format(parameterName, errorMessage));
        this.parameterName = parameterName;
        this.errorMessage = errorMessage;
    }

}
