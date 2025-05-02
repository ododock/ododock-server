package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestWebFluxSecurityConfig;
import ododock.webserver.config.web.WebConfiguration;
import ododock.webserver.domain.article.Category;
import ododock.webserver.domain.article.CategoryService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.V1alpha1CategoryController;
import ododock.webserver.web.v1alpha1.dto.category.V1alpha1Category;
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

import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@WebFluxTest(controllers = V1alpha1CategoryController.class)
@Import({RestDocsConfig.class, TestWebFluxSecurityConfig.class, WebConfiguration.class})
@AutoConfigureRestDocs
public class V1alpha1CategoryControllerDocsTest {

    private static final String BASE_URL = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @WithMockUser
    void list_category_by_account_id_Docs() throws Exception {
        Category category1 = Category.builder()
                .name("Journal")
                .ownerAccountId(1L)
                .visibility(true)
                .position(0)
                .build();

        Category category = mock(Category.class);
        when(category.getId()).thenReturn("category-id");
        when(category.getOwnerAccountId()).thenReturn(1L);
        when(category.getName()).thenReturn("Journal");
        when(category.getPosition()).thenReturn(0);
        when(category.isVisibility()).thenReturn(true);
        when(category.getCreatedDate()).thenReturn(Instant.now());
        when(category.getLastModifiedAt()).thenReturn(Instant.now());

        given(this.categoryService.listCategoriesByOwnerAccountId(1L))
                .willReturn(Flux.just(category));

        webClient.get().uri(BASE_URL, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/list-categories-by-account-id",
                                resourceDetails()
                                        .tag("Category").description("계정으로 카테고리 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 카테고리의 계정 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").description("조회된 카테고리 ID").optional(),
                                        fieldWithPath("[].ownerAccountId").description("카테고리 소유자 Account ID").optional(),
                                        fieldWithPath("[].name").description("조회된 카테고리 이름").optional(),
                                        fieldWithPath("[].position").description("조회된 카테고리 순서 index").optional(),
                                        fieldWithPath("[].visibility").description("카테고리 공개설정").optional(),
                                        fieldWithPath("[].createdAt").description("카테고리 생성일").optional(),
                                        fieldWithPath("[].updatedAt").description("카테고리 수정일").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void list_category_by_account_id_with_empty_response_Docs() throws Exception {
        given(this.categoryService.listCategoriesByOwnerAccountId(1L))
                .willReturn(Flux.empty());

        webClient.get().uri(BASE_URL, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/list-categories-empty-response",
                                resourceDetails()
                                        .tag("Category").description("카테고리 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 카테고리의 계정 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[]").description("빈 카테고리 목록").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void create_category_Docs() throws Exception {
        V1alpha1Category request = V1alpha1Category.builder()
                .name("Journal")
                .ownerAccountId(1L)
                .visibility(true)
                .build();

        Category mock = mock(Category.class);
        when(mock.getId()).thenReturn("68142fb23ce8964a97f9767f");
        when(mock.getOwnerAccountId()).thenReturn(1L);
        when(mock.getName()).thenReturn("Journal");
        when(mock.getPosition()).thenReturn(0);
        when(mock.isVisibility()).thenReturn(true);
        when(mock.getCreatedDate()).thenReturn(Instant.now());
        when(mock.getLastModifiedAt()).thenReturn(Instant.now());

        given(this.categoryService.createCategory(request.toDomainDto()))
                .willReturn(Mono.just(mock));

        webClient.post().uri(BASE_URL, 1L)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/create-category",
                                resourceDetails()
                                        .tag("Category").summary("카테고리 생성 엔드포인트")
                                        .description("""
                                                카테고리 생성 시 가장 마지막 위치에 기본적으로 추가됨. 
                                                총 카테고리의 갯수가 5개인 상태에서 새로운 카테고리를 생성하면 해당 카테고리는 position이 6으로 됨.
                                                """),
                                pathParameters(
                                        parameterWithName("id").description("생성할 카테고리의 계정 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("카테고리의 ID"),
                                        fieldWithPath("name").description("카테고리의 이름"),
                                        fieldWithPath("position").description("카테고리의 위치"),
                                        fieldWithPath("visibility").description("카테고리의 공개여부"),
                                        fieldWithPath("ownerAccountId").description("카테고리의 소유자 ID"),
                                        fieldWithPath("createdAt").description("카테고리 생성일"),
                                        fieldWithPath("updatedAt").description("카테고리 수정일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void update_category_Docs() throws Exception {
        V1alpha1Category request = V1alpha1Category.builder()
                .name("Journal")
                .ownerAccountId(1L)
                .visibility(true)
                .build();

        Category response = mock(Category.class);
        when(response.getId()).thenReturn("category-id");
        when(response.getOwnerAccountId()).thenReturn(1L);
        when(response.getName()).thenReturn("Journal");
        when(response.isVisibility()).thenReturn(true);
        when(response.getCreatedDate()).thenReturn(Instant.now());
        when(response.getLastModifiedAt()).thenReturn(Instant.now());

        given(this.categoryService.updateCategory(1L, request.toDomainDto()))
                .willReturn(Mono.just(response));

        webClient.patch().uri(BASE_URL + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}", 1L, "category-id")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/update-category",
                                resourceDetails()
                                        .tag("Category")
                                        .summary("카테고리 수정 엔드포인트")
                                        .description("""
                                                카테고리의 위치(position)이 변경되는 경우, 해당 위치에 이미 존재하는 category와 순서가 변경됨.
                                                예를 들어, 0번 위치에 있는 카테고리를 1번 위치로 이동하면, 1번 위치에 있는 카테고리는 0번으로 이동함.
                                                현재 존재하는 카테고리의 갯수보다 많은 갯수를 입력하는 경우, 현재 존재하는 가장 마지막 위치로 설정됨.
                                                총 카테고리 갯수가 5개인 상황에서 카테고리(position 1)의 위치를 10으로 하게되면 해당 카테고리는 5로 설정되며, 기존에 5에 있던 카테고리는 position 1로 됨.
                                                카테고리의 위치는 1-based index으로 표현됨."""),
                                pathParameters(
                                        parameterWithName("id").description("수정할 카테고리의 계정 ID"),
                                        parameterWithName("subId").description("수정할 카테고리 ID")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("수정된 카테고리 ID").optional(),
                                        fieldWithPath("ownerAccountId").description("카테고리 소유자 Account ID").optional(),
                                        fieldWithPath("name").description("수정된 카테고리 이름").optional(),
                                        fieldWithPath("position").description("수정된 카테고리 순서 index").optional(),
                                        fieldWithPath("visibility").description("수정된 카테고리 공개설정").optional(),
                                        fieldWithPath("createdAt").description("카테고리 생성일").optional(),
                                        fieldWithPath("updatedAt").description("카테고리 수정일").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void delete_category_Docs() throws Exception {
        given(this.categoryService.deleteCategory("delete-category-id"))
                .willReturn(Flux.empty());

        webClient.delete().uri(BASE_URL + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}", 1L, "delete-category-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/delete-category",
                                resourceDetails()
                                        .tag("Category").description("카테고리 삭제 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("삭제할 카테고리의 계정 ID"),
                                        parameterWithName("subId").description("삭제할 카테고리의 계정 ID")
                                )
                        )
                );
    }

}
