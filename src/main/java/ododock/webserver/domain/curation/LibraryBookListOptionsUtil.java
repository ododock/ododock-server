package ododock.webserver.domain.curation;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.curation.dto.LibraryBookListOptions;
import org.springframework.util.MultiValueMap;

@RequiredArgsConstructor
public class LibraryBookListOptionsUtil {

    private final static String API_KEY_LABEL = "authKey";
    private final String apiKey;

    public void applyPopularBookListOptions(MultiValueMap<String, String> params, LibraryBookListOptions listOptions) {
        params.add(API_KEY_LABEL, apiKey);
        params.add("format", "json");
        if (listOptions.getStartDt() != null) {
            params.add("startDt", getStringValue(listOptions.getStartDt()));
        }
        if (listOptions.getEndDt() != null) {
            params.add("endDt", getStringValue(listOptions.getEndDt()));
        }
        if (listOptions.getGender() != null) {
            params.add("gender", getStringValue(listOptions.getGender()));
        }
        if (listOptions.getFrom_age() != null) {
            params.add("from_age", getStringValue(listOptions.getFrom_age()));
        }
        if (listOptions.getTo_age() != null) {
            params.add("to_age", getStringValue(listOptions.getTo_age()));
        }
        if (listOptions.getAge() != null) {
            listOptions.getAge()
                    .forEach(i -> {
                        params.add("age", getStringValue(i));
                    });
        }
        if (listOptions.getRegion() != null) {
            listOptions.getRegion()
                    .forEach(i -> {
                        params.add("region", getStringValue(i));
                    });
        }
        if (listOptions.getDtl_region() != null) {
            listOptions.getDtl_region()
                    .forEach(i -> {
                        params.add("dtl_region", getStringValue(i));
                    });
        }
        if (listOptions.getBook_dvsn() != null) {
            params.add("book_dvsn", getStringValue(listOptions.getBook_dvsn()));
        }
        if (listOptions.getAddCode() != null) {
            params.add("addCode", getStringValue(listOptions.getAddCode()));
        }
        if (listOptions.getKdc() != null) {
            params.add("kdc", getStringValue(listOptions.getKdc()));
        }
        if (listOptions.getDtl_kdc() != null) {
            params.add("dtl_kdc", getStringValue(listOptions.getDtl_kdc()));
        }
        if (listOptions.getPageOffset() != null) {
            params.add("pageNo", getStringValue(listOptions.getPageOffset()));
        }
        if (listOptions.getPageSize() != null) {
            params.add("pageSize", getStringValue(listOptions.getPageSize()));
        }
    }

    private String getStringValue(Object value) {
        return value == null ? null : value.toString();
    }

}
