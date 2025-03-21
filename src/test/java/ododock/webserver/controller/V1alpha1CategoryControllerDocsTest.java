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

import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
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

        given(this.categoryService.listCategoriesByOwnerAccountId(1L))
                .willReturn(Flux.just(category1));

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
        Category category = Category.builder()
                .name("Journal")
                .ownerAccountId(1L)
                .visibility(true)
                .position(0)
                .build();
        given(this.categoryService.createCategory(category))
                .willReturn(Mono.just(category));

        webClient.get().uri(BASE_URL + "", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/create-category",
                                resourceDetails()
                                        .tag("Category").description("카테고리 생성 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("생성할 카테고리의 계정 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[]").description("빈 카테고리 목록").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void update_category_Docs() throws Exception {
        Category category = Category.builder()
                .name("Journal")
                .ownerAccountId(1L)
                .visibility(true)
                .position(0)
                .build();

        given(this.categoryService.updateCategory(category))
                .willReturn(Mono.just(category));

        webClient.patch().uri(BASE_URL + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}", 1L, "update-category-id")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(V1alpha1Category.toControllerDto(category))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("category/update-category",
                                resourceDetails()
                                        .tag("Category").description("카테고리 수정 엔드포인트"),
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
