package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.config.web.WebConfiguration;
import ododock.webserver.domain.curation.LibraryBookListOptionsUtil;
import ododock.webserver.domain.curation.LibraryCurationService;
import ododock.webserver.domain.curation.dto.LibraryBook;
import ododock.webserver.domain.curation.dto.LibraryBookListOptions;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.V1alpha1CurationController;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1Book;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1BookCurationConverter;
import ododock.webserver.web.v1alpha1.dto.curation.V1alpha1BookListOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

@WebFluxTest(controllers = V1alpha1CurationController.class)
@Import({RestDocsConfig.class, WebConfiguration.class})
@AutoConfigureRestDocs
@AutoConfigureWebTestClient
public class V1alpha1CurationControllerDocsTest {

    private static final String MOCK_API_KEY = "mock-api-key";
    private static final String BASE_PATH = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.CURATIONS;
    private final V1alpha1BookCurationConverter curationConverter;
    private final LibraryBookListOptionsUtil listOptionsUtil;
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private LibraryCurationService curationService;

    public V1alpha1CurationControllerDocsTest() {
        this.curationConverter = new V1alpha1BookCurationConverter();
        this.listOptionsUtil = new LibraryBookListOptionsUtil(MOCK_API_KEY);
    }

    @Test
    @WithMockUser
    void listPopularBooks_Docs() throws Exception {
        // given
        V1alpha1BookListOptions listOptionsControllerDto = new V1alpha1BookListOptions();
        listOptionsControllerDto.setStartDate(LocalDate.of(2024, 12, 1));
        listOptionsControllerDto.setEndDate(LocalDate.of(2024, 12, 31));
        listOptionsControllerDto.setPageOffset(1);
        listOptionsControllerDto.setPageSize(3);

        LibraryBookListOptions listOptionsDomainDto = this.curationConverter.toDomainDto(listOptionsControllerDto);
        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        this.listOptionsUtil.applyPopularBookListOptions(queryParams, listOptionsDomainDto);

        List<LibraryBook> books = List.of(
                new LibraryBook(
                        1,
                        "1",
                        "불편한 편의점 :김호연 장편소설 ",
                        "지은이: 김호연",
                        "나무옆의자",
                        "2021",
                        "9791161571188",
                        "03810",
                        "",  // vol
                        "813.7",
                        "문학 > 한국문학 > 소설",
                        "47873",
                        null, // bookImageUrl
                        "https://data4library.kr/bookV?seq=5962217"
                ),
                new LibraryBook(
                        2,
                        "2",
                        "아버지의 해방일지 :정지아 장편소설 ",
                        "지은이: 정지아",
                        "창비",
                        "2022",
                        "9788936438838",
                        "03810",
                        "",
                        "813.62",
                        "문학 > 한국문학 > 소설",
                        "31080",
                        null,
                        "https://data4library.kr/bookV?seq=6551957"
                ),
                new LibraryBook(
                        3,
                        "3",
                        "흔한남매",
                        "원작: 흔한남매 ;그림: 유난희",
                        "미래엔",
                        "2019",
                        "9791164134267",
                        "77810",
                        "3",
                        "810",
                        "문학 > 한국문학 > 한국문학",
                        "29555",
                        null,
                        "https://data4library.kr/bookV?seq=5569013"
                )
        );

        LibraryBookListOptions listOpt = this.curationConverter.toDomainDto(listOptionsControllerDto);
        given(this.curationService.listPopularBook(listOpt))
                .willReturn(Flux.fromIterable(books));

        RestDocumentationRequestBuilders.get(BASE_PATH + ResourcePath.CURATIONS_SUBRESOURCE_POPULAR_BOOKS)
                .queryParam("pageOffset", "1")
                .queryParam("pageSize", "3")
                .queryParam("startDate", "2024-12-01")
                .queryParam("endDate", "2024-12-31");
        // expected
        webTestClient
                .get()
                .uri(
                        UriComponentsBuilder
                                .fromPath(BASE_PATH + ResourcePath.CURATIONS_SUBRESOURCE_POPULAR_BOOKS)
                                .queryParam("pageOffset", 1)
                                .queryParam("pageSize", 3)
                                .queryParam("startDate", "2024-12-01")
                                .queryParam("endDate", "2024-12-31")
                                .toUriString()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(V1alpha1Book.class)
                .consumeWith(
                        document("sample",
                                resourceDetails()
                                        .tag("Curation").description("인기도서 목록 조회 엔드포인트"),
                                queryParameters(
                                        parameterWithName("startDate").description("조회 시작일").optional(),
                                        parameterWithName("endDate").description("조회 종료일").optional(),
                                        parameterWithName("pageOffset").description("페이지 오프셋 - 기본값: 1").optional(),
                                        parameterWithName("pageSize").description("페이지 사이즈 - 기본값: 100").optional()
                                ),
                                responseFields(
                                        fieldWithPath("[].no").description("순번"),
                                        fieldWithPath("[].ranking").description("순위"),
                                        fieldWithPath("[].bookName").description("도서명"),
                                        fieldWithPath("[].authors").description("저자"),
                                        fieldWithPath("[].publisher").description("출판사"),
                                        fieldWithPath("[].publicationYear").description("출판년도"),
                                        fieldWithPath("[].isbn").description("ISBN13"),
                                        fieldWithPath("[].additionalISBNSymbol").description("ISBN 부가기호"),
                                        fieldWithPath("[].vol").description("권"),
                                        fieldWithPath("[].classNumber").description("주제분류"),
                                        fieldWithPath("[].className").description("주제분류명"),
                                        fieldWithPath("[].loanCount").description("대출횟수"),
                                        fieldWithPath("[].bookImageUrl").description("도서이미지 URL").optional(),
                                        fieldWithPath("[].bookDetailUrl").description("도서상세 URL")
                                )
                        )
                );
    }

}
