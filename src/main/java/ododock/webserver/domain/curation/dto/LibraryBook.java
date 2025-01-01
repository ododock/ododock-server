package ododock.webserver.domain.curation.dto;

public record LibraryBook(
        Integer no, // 순번
        String ranking, // 순위
        String bookname, // 도서명
        String authors, // 저자
        String publisher, // 출판사
        String publication_year, // 출판년도
        String isbn13, // ISBN13
        String addition_symbol, // ISBN 부가기호
        String vol, // 권
        String class_no, // 주제분류
        String class_nm, // 주제분류명
        String loan_count, // 대출횟수
        String bookImageUrl, // 도서이미지 URL
        String bookDtlUrl // 도서상세 URL
) {

}
