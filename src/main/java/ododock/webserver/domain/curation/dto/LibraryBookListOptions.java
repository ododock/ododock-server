package ododock.webserver.domain.curation.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ododock.webserver.domain.dto.ListOptions;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LibraryBookListOptions extends ListOptions {

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
