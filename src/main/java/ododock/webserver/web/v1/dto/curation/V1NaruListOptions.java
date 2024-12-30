package ododock.webserver.web.v1.dto.curation;

import lombok.Data;

@Data
public class V1NaruListOptions {

    private Long pageNo;
    private Long pageSize;
    private String format; // xml, json

}
