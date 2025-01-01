package ododock.webserver.domain.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ListOptions {

    @Positive
    private Integer pageOffset;
    @Positive
    private Integer pageSize;

    public ListOptions() {
        this.pageOffset = 1;
        this.pageSize = 100;
    }
}
