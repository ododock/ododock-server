package ododock.webserver.web.v1alpha1.dto.curation;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class V1alpha1ListOptions {

    @Positive
    private Integer pageNo;
    @Positive
    private Integer pageSize;

    public V1alpha1ListOptions() {
        this.pageNo = 1;
        this.pageSize = 100;
    }

}
