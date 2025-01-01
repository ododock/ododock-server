package ododock.webserver.domain.curation.dto;

import java.util.List;

public record LibraryBookList(
        LibraryRequestListOptions request,
        Integer resultNum, // 응답 결과 건수
        Integer numFound, // 전체 검색결과 건수
        List<PopularBookDoc> docs
) {

    public record PopularBookDoc(
            LibraryBook doc
    ) {

    }

}
