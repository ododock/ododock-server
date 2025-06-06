package ododock.webserver.controller;

import com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestWebFluxSecurityConfig;
import ododock.webserver.config.web.WebConfiguration;
import ododock.webserver.domain.article.*;
import ododock.webserver.domain.article.dto.V1alpha1BaseBlock;
import ododock.webserver.domain.article.dto.V1alpha1DefaultProps;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.V1alpha1ArticleController;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1Article;
import ododock.webserver.web.v1alpha1.dto.article.V1alpha1ArticleListOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.resourceDetails;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@WebFluxTest(controllers = V1alpha1ArticleController.class)
@Import({RestDocsConfig.class, TestWebFluxSecurityConfig.class, WebConfiguration.class})
@AutoConfigureRestDocs
public class V1alpha1ArticleControllerDocsTest {

    private static final String SCHEME_PREFIX = "https://oddk.life:8080";
    private static final String BASE_URL = ResourcePath.API + ResourcePath.API_VERSION;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ArticleQueryService articleQueryService;

    @Test
    @WithMockUser
    void get_article_with_not_exists_id_then_return_not_found_response_Docs() throws Exception {
        given(this.articleService.getArticle("article-id-1"))
                .willReturn(Mono.error(new ResourceNotFoundException(Article.class, "article-id-1")));

        webClient.get().uri(BASE_URL + ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}", "article-id-1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentationWrapper.document("article/get-article-not-found",
                                resourceDetails()
                                        .tag("Article").description("아티클 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 아티클의 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void get_article_with_id_Docs() throws Exception {
        V1alpha1BaseBlock block = new V1alpha1BaseBlock();
        block.setId("block-id-generated-from-blocknotejs");
        block.setChildren(List.of());
        block.setType("paragraph");
        block.setProps(new V1alpha1DefaultProps());
        block.setContent(List.of());

        Article mockArticle = mock(Article.class);
        when(mockArticle.getId()).thenReturn("f8c3a911-ad74-48b6-9f8e-2aedbf11f424");
        when(mockArticle.getTitle()).thenReturn("article-title");
        when(mockArticle.getOwnerAccountId()).thenReturn(1L);
        when(mockArticle.isVisibility()).thenReturn(true);
        when(mockArticle.getBody()).thenReturn(List.of(block));
        when(mockArticle.getExcerpt()).thenReturn("excerpt of the article");
        when(mockArticle.getCategoryId()).thenReturn("403202a5-1d33-4970-9fbc-e80394319098");
        when(mockArticle.getTags()).thenReturn(Set.of("tag1"));
        when(mockArticle.getCreatedDate()).thenReturn(Instant.now());
        when(mockArticle.getLastModifiedAt()).thenReturn(Instant.now());

        given(this.articleService.getArticle("article-id-1"))
                .willReturn(Mono.just(mockArticle));

        webClient.get().uri(BASE_URL + ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}", "article-id-1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentationWrapper.document("article/get-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 아티클의 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("조회된 아티클 ID"),
                                        fieldWithPath("title").description("조회된 아티클 제목"),
                                        fieldWithPath("ownerAccountId").description("아티클 작성자의 Account ID"),
                                        fieldWithPath("categoryId").description("아티클의 카테고리 ID").optional(),
                                        fieldWithPath("tags[]").description("태그").optional(),
                                        fieldWithPath("body").description("조회된 아티클 본문").optional(),
                                        fieldWithPath("excerpt").description("조회된 아티클의 서문").optional(),
                                        fieldWithPath("body[].id").description("아티클 블록 ID"),
                                        fieldWithPath("body[].type").description("아티클 블록 타입"),
                                        fieldWithPath("body[].props").description("아티클 블록 속성"),
                                        fieldWithPath("body[].props.backgroundColor").description("아티클 블록의 스타일 - 백그라운드 컬러"),
                                        fieldWithPath("body[].props.textColor").description("아티클 블록의 스타일 - 텍스트 색상"),
                                        fieldWithPath("body[].props.textAlignment").description("아티클 블록의 스타일 - 텍스트 정렬"),
                                        fieldWithPath("body[].content").description("아티클 블록의 컨텐츠").optional(),
                                        fieldWithPath("body[].children").description("아티클 블록의 하위 블록 리스트").optional(),
                                        fieldWithPath("visibility").description("카테고리 공개설정"),
                                        fieldWithPath("createdAt").description("카테고리 생성일"),
                                        fieldWithPath("updatedAt").description("카테고리 수정일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void list_articles_by_account_id_Docs() throws Exception {
        V1alpha1BaseBlock block = new V1alpha1BaseBlock();
        block.setId("block-id-generated-from-blocknotejs");
        block.setChildren(List.of());
        block.setType("paragraph");
        block.setProps(new V1alpha1DefaultProps());
        block.setContent(List.of());

        Article mockArticle = mock(Article.class);
        when(mockArticle.getId()).thenReturn("f8c3a911-ad74-48b6-9f8e-2aedbf11f424");
        when(mockArticle.getTitle()).thenReturn("article-title");
        when(mockArticle.getOwnerAccountId()).thenReturn(1L);
        when(mockArticle.isVisibility()).thenReturn(true);
        when(mockArticle.getBody()).thenReturn(List.of(block));
        when(mockArticle.getExcerpt()).thenReturn("excerpt of the article");
        when(mockArticle.getCategoryId()).thenReturn("403202a5-1d33-4970-9fbc-e80394319098");
        when(mockArticle.getTags()).thenReturn(Set.of("tag1"));
        when(mockArticle.getCreatedDate()).thenReturn(Instant.now());
        when(mockArticle.getLastModifiedAt()).thenReturn(Instant.now());

        ArticleListOptions listOptions = V1alpha1ArticleListOptions.builder()
                .authorName("John")
                .title("article-title")
                .categoryId("403202a5-1d33-4970-9fbc")
                .visibility(true)
                .tags(List.of("tag1", "tag2"))
                .keyword("keyword-to-search")
                .build().toDomainDto();

        given(this.articleQueryService.listArticles(any(), any()))
                .willReturn(Flux.just(mockArticle));

        webClient.get()
                .uri(BASE_URL + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ARTICLES + "?" +
                                "authorName={authorName}" +
                                "&title={title}" +
                                "&categoryId={categoryId}" +
                                "&visibility={visibility}" +
                                "&keyword={keyword}" +
                                "&tags={tags}",
                        1L,
                        listOptions.getAuthorName(),
                        listOptions.getTitle(),
                        listOptions.getCategoryId(),
                        listOptions.getVisibility(),
                        listOptions.getKeyword(),
                        listOptions.getTags() != null && !listOptions.getTags().isEmpty() ? listOptions.getTags().get(0) : null // 단일 태그만 예시
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentationWrapper.document("article/list-account-articles",
                                resourceDetails()
                                        .tag("Article").description("유저의 아티클 리스트 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 아티클 작성자 계정의 ID")
                                ),
                                queryParameters(
                                        parameterWithName("authorName").description("검색할 작성자 명").ignored(),
                                        parameterWithName("title").description("검색할 아티클 타이틀").optional(),
                                        parameterWithName("categoryId").description("검색할 아티클의 카테고리 ID").optional(),
                                        parameterWithName("visibility").description("공개여부에 따라 아티클 검색").optional(),
                                        parameterWithName("tags").description("검색할 아티클에 설정된 태그 값").optional(),
                                        parameterWithName("keyword").description("검색할 키워드(제목, 본문, 태그를 대상으로 함)").optional()
                                ),
                                responseFields(
                                        fieldWithPath("[].id").description("조회된 아티클 ID"),
                                        fieldWithPath("[].title").description("조회된 아티클 제목"),
                                        fieldWithPath("[].ownerAccountId").description("아티클 작성자의 Account ID"),
                                        fieldWithPath("[].categoryId").description("아티클의 카테고리 ID").optional(),
                                        fieldWithPath("[].tags[]").description("태그").optional(),
                                        fieldWithPath("[].body").description("조회된 아티클 본문").optional(),
                                        fieldWithPath("[].excerpt").description("조회된 아티클의 서문").optional(),
                                        fieldWithPath("[].body[].id").description("아티클 블록 ID"),
                                        fieldWithPath("[].body[].type").description("아티클 블록 타입"),
                                        fieldWithPath("[].body[].props").description("아티클 블록 속성"),
                                        fieldWithPath("[].body[].props.backgroundColor").description("아티클 블록의 스타일 - 백그라운드 컬러"),
                                        fieldWithPath("[].body[].props.textColor").description("아티클 블록의 스타일 - 텍스트 색상"),
                                        fieldWithPath("[].body[].props.textAlignment").description("아티클 블록의 스타일 - 텍스트 정렬"),
                                        fieldWithPath("[].body[].content").description("아티클 블록의 컨텐츠").optional(),
                                        fieldWithPath("[].body[].children").description("아티클 블록의 하위 블록 리스트").optional(),
                                        fieldWithPath("[].visibility").description("카테고리 공개설정"),
                                        fieldWithPath("[].createdAt").description("카테고리 생성일"),
                                        fieldWithPath("[].updatedAt").description("카테고리 수정일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void create_article_Docs() throws Exception {
        V1alpha1BaseBlock block = new V1alpha1BaseBlock();
        block.setId("block-id-generated-from-blocknotejs");
        block.setChildren(List.of());
        block.setProps(new V1alpha1DefaultProps());
        block.setType("paragraph");
        block.setContent(List.of());

        Article mockArticle = mock(Article.class);
        when(mockArticle.getId()).thenReturn("f8c3a911-ad74-48b6-9f8e-2aedbf11f424");
        when(mockArticle.getTitle()).thenReturn("article-title");
        when(mockArticle.getOwnerAccountId()).thenReturn(1L);
        when(mockArticle.isVisibility()).thenReturn(true);
        when(mockArticle.getBody()).thenReturn(List.of(block));
        when(mockArticle.getExcerpt()).thenReturn("excerpt of the article");
        when(mockArticle.getCategoryId()).thenReturn("403202a5-1d33-4970-9fbc-e80394319098");
        when(mockArticle.getTags()).thenReturn(Set.of("tag1"));
        when(mockArticle.getCreatedDate()).thenReturn(Instant.now());
        when(mockArticle.getLastModifiedAt()).thenReturn(Instant.now());

        V1alpha1Article controllerDto = V1alpha1Article.builder()
                .title("title")
                .ownerAccountId(1L)
                .categoryId("403202a5-1d33-4970-9fbc-e80394319098")
                .visibility(true)
                .body(List.of(block))
                .tags(Set.of("tag1"))
                .build();

        given(this.articleService.createArticle(any(Article.class)))
                .willReturn(Mono.just(mockArticle));

        webClient.post().uri(BASE_URL + ResourcePath.ARTICLES)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(controllerDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentationWrapper.document("article/create-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 엔드포인트"),
                                requestFields(
                                        fieldWithPath("title").description("작성한 아티클 제목"),
                                        fieldWithPath("ownerAccountId").description("아티클 작성자의 Account ID"),
                                        fieldWithPath("categoryId").description("아티클의 카테고리 ID").optional(),
                                        fieldWithPath("tags[]").description("태그").optional(),
                                        fieldWithPath("body").description("작성된 아티클 본문").optional(),
                                        fieldWithPath("body[].id").description("아티클 블록 ID"),
                                        fieldWithPath("body[].type").description("아티클 블록 타입"),
                                        fieldWithPath("body[].props").description("아티클 블록 속성"),
                                        fieldWithPath("body[].props.backgroundColor").description("아티클 블록의 스타일 - 백그라운드 컬러"),
                                        fieldWithPath("body[].props.textColor").description("아티클 블록의 스타일 - 텍스트 색상"),
                                        fieldWithPath("body[].props.textAlignment").description("아티클 블록의 스타일 - 텍스트 정렬"),
                                        fieldWithPath("body[].content").description("아티클 블록의 컨텐츠").optional(),
                                        fieldWithPath("body[].children").description("아티클 블록의 하위 블록 리스트").optional(),
                                        fieldWithPath("visibility").description("작성된 아티클 공개설정")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("조회된 아티클 ID").optional(),
                                        fieldWithPath("title").description("조회된 아티클 제목").optional(),
                                        fieldWithPath("ownerAccountId").description("아티클 작성자의 Account ID"),
                                        fieldWithPath("categoryId").description("아티클의 카테고리 ID").optional(),
                                        fieldWithPath("tags[]").description("태그").optional(),
                                        fieldWithPath("body").description("작성된 아티클 본문").optional(),
                                        fieldWithPath("excerpt").description("조회된 아티클의 서문").optional(),
                                        fieldWithPath("body[].id").description("아티클 블록 ID").optional(),
                                        fieldWithPath("body[].type").description("아티클 블록 타입").optional(),
                                        fieldWithPath("body[].props").description("아티클 블록 속성").optional(),
                                        fieldWithPath("body[].props.backgroundColor").description("아티클 블록의 스타일 - 백그라운드 컬러"),
                                        fieldWithPath("body[].props.textColor").description("아티클 블록의 스타일 - 텍스트 색상"),
                                        fieldWithPath("body[].props.textAlignment").description("아티클 블록의 스타일 - 텍스트 정렬"),
                                        fieldWithPath("body[].content").description("아티클 블록의 컨텐츠").optional(),
                                        fieldWithPath("body[].children").description("아티클 블록의 하위 블록 리스트").optional(),
                                        fieldWithPath("visibility").description("작성된 아티클 공개설정"),
                                        fieldWithPath("createdAt").description("작성된 아티클 생성일"),
                                        fieldWithPath("updatedAt").description("작성된 아티클 수정일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void update_article_Docs() throws Exception {
        V1alpha1BaseBlock block = new V1alpha1BaseBlock();
        block.setId("block-id-generated-from-blocknotejs");
        block.setChildren(List.of());
        block.setProps(new V1alpha1DefaultProps());
        block.setType("paragraph");
        block.setContent(List.of());

        Article mockArticle = mock(Article.class);
        when(mockArticle.getId()).thenReturn("f8c3a911-ad74-48b6-9f8e-2aedbf11f424");
        when(mockArticle.getTitle()).thenReturn("article-title");
        when(mockArticle.getOwnerAccountId()).thenReturn(1L);
        when(mockArticle.isVisibility()).thenReturn(true);
        when(mockArticle.getBody()).thenReturn(List.of(block));
        when(mockArticle.getExcerpt()).thenReturn("excerpt of the article");
        when(mockArticle.getCategoryId()).thenReturn("403202a5-1d33-4970-9fbc-e80394319098");
        when(mockArticle.getTags()).thenReturn(Set.of("tag1"));
        when(mockArticle.getCreatedDate()).thenReturn(Instant.now());
        when(mockArticle.getLastModifiedAt()).thenReturn(Instant.now());

        V1alpha1Article controllerDto = V1alpha1Article.builder()
                .title("title")
                .ownerAccountId(1L)
                .categoryId("403202a5-1d33-4970-9fbc-e80394319098")
                .visibility(true)
                .body(List.of(block))
                .tags(Set.of("tag1"))
                .build();


        given(this.articleService.updateArticle(any(String.class), any(Article.class))).willReturn(Mono.just(mockArticle));

        webClient.patch().uri(BASE_URL + ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(controllerDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentationWrapper.document("article/update-articles",
                                resourceDetails()
                                        .tag("Article").description("아티클 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("수정할 아티클 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("수정된 아티클 ID").optional(),
                                        fieldWithPath("title").description("수정된 아티클 제목").optional(),
                                        fieldWithPath("ownerAccountId").description("수정된 아티클 작성자의 Account ID"),
                                        fieldWithPath("categoryId").description("수정된 아티클의 카테고리 ID").optional(),
                                        fieldWithPath("tags[]").description("수정된 태그").optional(),
                                        fieldWithPath("body").description("수정된 아티클 본문").optional(),
                                        fieldWithPath("excerpt").description("조회된 아티클의 서문").optional(),
                                        fieldWithPath("body[].id").description("아티클 블록 ID").optional(),
                                        fieldWithPath("body[].type").description("아티클 블록 타입").optional(),
                                        fieldWithPath("body[].props").description("아티클 블록 속성").optional(),
                                        fieldWithPath("body[].props.backgroundColor").description("아티클 블록의 스타일 - 백그라운드 컬러"),
                                        fieldWithPath("body[].props.textColor").description("아티클 블록의 스타일 - 텍스트 색상"),
                                        fieldWithPath("body[].props.textAlignment").description("아티클 블록의 스타일 - 텍스트 정렬"),
                                        fieldWithPath("body[].content").description("아티클 블록의 컨텐츠").optional(),
                                        fieldWithPath("body[].children").description("아티클 블록의 하위 블록 리스트").optional(),
                                        fieldWithPath("visibility").description("수정된 아티클 공개설정"),
                                        fieldWithPath("createdAt").description("수정된 아티클 생성일"),
                                        fieldWithPath("updatedAt").description("수정된 아티클 수정일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void delete_article_Docs() throws Exception {
        Article article = Article.builder()
                .title("title")
                .ownerAccountId(1L)
                .body(null)
                .tags(Set.of("tag1"))
                .build();

        given(this.articleService.deleteArticle("article-id"))
                .willReturn(Mono.empty());

        webClient.delete().uri(BASE_URL + ResourcePath.ARTICLES + "/{" + ResourcePath.PATH_VAR_ID + "}", "article-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(WebTestClientRestDocumentationWrapper.document("article/delete-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("삭제할 아티클의 ID")
                                )
                        )
                );
    }

}
