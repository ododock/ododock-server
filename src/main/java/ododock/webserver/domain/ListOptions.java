package ododock.webserver.domain;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ListOptions {

    @Positive
    private Integer pageOffset;
    @Positive
    private Integer pageSize;
    @Positive
    private Integer size;
    private String sort;
    private String sortKeys;

    public ListOptions() {
        this.pageOffset = 1;
        this.pageSize = 100;
        this.size = 100;
    }

}
