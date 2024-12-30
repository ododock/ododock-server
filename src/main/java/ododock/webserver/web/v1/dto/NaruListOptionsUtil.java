package ododock.webserver.web.v1.dto;

import lombok.RequiredArgsConstructor;
import ododock.webserver.web.v1.dto.curation.V1PopularBookNaruListOptions;
import org.springframework.util.MultiValueMap;

import java.util.List;

@RequiredArgsConstructor
public class NaruListOptionsUtil {

    private final String authKey;

    public void applyPopularBookListOptions(MultiValueMap<String, List<String>> params, V1PopularBookNaruListOptions listOptions) {
        params.add("authKey", List.of(authKey));
        if (listOptions.getStartDt() != null) {
            params.add("startDt", ListValueUtils.getStringValues(listOptions.getStartDt()));
        }
        if (listOptions.getEndDt() != null) {
            params.add("endDt", ListValueUtils.getStringValues(listOptions.getEndDt()));
        }
        if (listOptions.getGender() != null) {
            params.add("gender", List.of(listOptions.getGender().toString()));
        }
        if (listOptions.getFrom_age() != null) {
            params.add("from_age", List.of(listOptions.getFrom_age().toString()));
        }
        if (listOptions.getTo_age() != null) {
            params.add("to_age", List.of(listOptions.getTo_age().toString()));
        }
        if (listOptions.getAge() != null) {
            params.add("age", ListValueUtils.getStringValues(listOptions.getAge()));
        }
        if (listOptions.getRegion() != null) {
            params.add("region", ListValueUtils.getStringValues(listOptions.getRegion()));
        }
        if (listOptions.getDtl_region() != null) {
            params.add("dtl_region", ListValueUtils.getStringValues(listOptions.getDtl_region()));
        }
        if (listOptions.getBook_dvsn() != null) {
            params.add("book_dvsn", List.of(listOptions.getBook_dvsn()));
        }
        if (listOptions.getAddCode() != null) {
            params.add("addCode", List.of(listOptions.getAddCode().toString()));
        }
        if (listOptions.getKdc() != null) {
            params.add("kdc", List.of(listOptions.getKdc().toString()));
        }
        if (listOptions.getDtl_kdc() != null) {
            params.add("dtl_kdc", List.of(listOptions.getDtl_kdc().toString()));
        }
        if (listOptions.getPageNo() != null) {
            params.add("pageNo", List.of(listOptions.getPageNo().toString()));
        }
        if (listOptions.getPageSize() != null) {
            params.add("pageSize", List.of(listOptions.getPageSize().toString()));
        }
        if (listOptions.getFormat() != null) {
            params.add("format", List.of(listOptions.getFormat()));
        }
    }

}
