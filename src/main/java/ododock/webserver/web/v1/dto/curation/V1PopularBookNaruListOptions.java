package ododock.webserver.web.v1.dto.curation;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class V1PopularBookNaruListOptions extends V1NaruListOptions {

    private LocalDate startDt;
    private LocalDate endDt;
    private Long gender;
    private Long from_age;
    private Long to_age;
    private List<Long> age;
    private List<Long> region;
    private List<Long> dtl_region;
    private String book_dvsn; // big 큰글씨, oversea 국외
    private Long addCode;
    private Long kdc; // 대주제
    private Long dtl_kdc;

}
