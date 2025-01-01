package ododock.webserver.web.v1alpha1.dto.curation;

import lombok.Data;

@Data
public class V1alpha1Book {

    private Integer no; // 순번
    private Integer ranking; // 순위
    private String bookName; // 도서명
    private String authors; // 저자
    private String publisher; // 출판사
    private String publicationYear; // 출판년도
    private String isbn; // ISBN13
    private String additionalISBNSymbol; // ISBN 부가기호
    private String vol; // 권
    private String classNumber; // 주제분류
    private String className; // 주제분류명
    private String loanCount; // 대출횟수
    private String bookImageUrl; // 도서이미지 URL
    private String bookDetailUrl; // 도서상세 URL

}
