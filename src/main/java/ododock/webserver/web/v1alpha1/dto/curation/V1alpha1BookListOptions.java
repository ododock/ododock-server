package ododock.webserver.web.v1alpha1.dto.curation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class V1alpha1BookListOptions extends V1alpha1ListOptions {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer gender;
    private Integer fromAge;
    private Integer toAge;
    private List<Integer> age;
    private List<Integer> region;
    private List<Integer> detailedRegion;
    private String bookDivision; // big 큰글씨, oversea 국외
    private Integer addedCode;
    private Integer category; // 대주제
    private Integer detailedCategory;

}
