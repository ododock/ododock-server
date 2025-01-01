package ododock.webserver.web.v1alpha1.dto.curation;

import ododock.webserver.domain.curation.dto.LibraryBook;
import ododock.webserver.domain.curation.dto.LibraryBookListOptions;

import java.util.List;

public class V1alpha1BookCurationConverter {

    public LibraryBookListOptions toDomainDto(V1alpha1BookListOptions controllerDto) {
        LibraryBookListOptions domainDto = new LibraryBookListOptions();
        domainDto.setPageOffset(controllerDto.getPageNo());
        domainDto.setPageSize(controllerDto.getPageSize());
        domainDto.setStartDt(controllerDto.getStartDate());
        domainDto.setEndDt(controllerDto.getEndDate());

        return domainDto;
    }

    public V1alpha1Book toControllerDto(LibraryBook domainDto) {
        V1alpha1Book controllerDto = new V1alpha1Book();
        controllerDto.setNo(domainDto.no());
        controllerDto.setRanking(Integer.valueOf(domainDto.ranking()));
        controllerDto.setBookName(domainDto.bookname());
        controllerDto.setAuthors(domainDto.authors());
        controllerDto.setPublisher(domainDto.publisher());
        controllerDto.setPublicationYear(domainDto.publication_year());
        controllerDto.setIsbn(domainDto.isbn13());
        controllerDto.setAdditionalISBNSymbol(domainDto.addition_symbol());
        controllerDto.setVol(domainDto.vol());
        controllerDto.setClassNumber(domainDto.class_no());
        controllerDto.setClassName(domainDto.class_nm());
        controllerDto.setLoanCount(domainDto.loan_count());
        controllerDto.setBookImageUrl(domainDto.bookImageUrl());
        controllerDto.setBookDetailUrl(domainDto.bookDtlUrl());

        return controllerDto;
    }

    public List<V1alpha1Book> toControllerDto(List<LibraryBook> domtainDto) {
        return domtainDto.stream()
                .map(this::toControllerDto)
                .toList();
    }

}
