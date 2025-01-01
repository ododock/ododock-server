package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.article.Category;
import ododock.webserver.domain.article.CategoryService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.CategoryController;
import ododock.webserver.web.v1alpha1.dto.request.CategoryCreate;
import ododock.webserver.web.v1alpha1.dto.request.CategoryPositionUpdate;
import ododock.webserver.web.v1alpha1.dto.request.CategoryUpdate;
import ododock.webserver.web.v1alpha1.dto.response.CategoryDetailsResponse;
import ododock.webserver.web.v1alpha1.dto.response.ListResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs()
public class CategoryControllerDocsTest {

    private static final String BASE_URL = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @WithMockUser
    void getCategoryByAccountId_Docs() throws Exception {
        //given
        final ListResponse<CategoryDetailsResponse> response = ListResponse.of(1L, 3, List.of(CategoryDetailsResponse.builder().categoryId(11L).name("essay").position(0).visibility(true).build(), CategoryDetailsResponse.builder().categoryId(12L).name("journal").position(1).visibility(true).build(), CategoryDetailsResponse.builder().categoryId(13L).name("transcribing").position(2).visibility(true).build()));
        given(this.categoryService.getCategoriesByAccountId(1L)).willReturn(response);

        // expected
        mockMvc.perform(get(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("category/get-categories",
                                resourceDetails()
                                        .tag("Category").description("카테고리 조회 엔드포인트")
                                , pathParameters(
                                        parameterWithName("id").description("조회할 카테고리의 계정 ID")
                                ),
                                responseFields(
                                        fieldWithPath("ownerId").description("카테고리 소유자 Account ID").optional(),
                                        fieldWithPath("totalSize").description("등록된 카테고리 갯수").optional(),
                                        subsectionWithPath("content").description("카테고리 목록"),
                                        fieldWithPath("content[].categoryId").description("조회된 카테고리 ID"),
                                        fieldWithPath("content[].name").description("조회된 카테고리 이름"),
                                        fieldWithPath("content[].position").description("조회된 카테고리 순서 index"),
                                        fieldWithPath("content[].visibility").description("카테고리 공개설정")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void createCategory_Docs() throws Exception {
        //given
        final CategoryCreate request = CategoryCreate.builder().name("new category").visibility(true).build();
        given(this.categoryService.createCategory(1L, request)).willReturn(20L);

        // expected
        mockMvc.perform(post(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("category/create-category",
                                resourceDetails()
                                        .tag("Category").description("카테고리 생성 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("생성할 카테고리의 소유자 Account ID")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("생성할 카테고리 이름"),
                                        fieldWithPath("visibility").description("생성할 카테고리의 공개 여부").optional()
                                ),
                                responseFields(
                                        fieldWithPath("type").description("생성된 자원 타입(카테고리)"),
                                        fieldWithPath("value").description("생성된 카테고리 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void updateCategory_Docs() throws Exception {
        // given
        final CategoryUpdate request = CategoryUpdate.builder()
                .name("updatedCategoryFoo")
                .position(3)
                .visibility(true)
                .build();

        Category mockCategory2 = Mockito.mock(Category.class);
        when(mockCategory2.getId()).thenReturn(12L);
        when(mockCategory2.getName()).thenReturn("previousOrdered3Category");
        when(mockCategory2.getPosition()).thenReturn(1);
        Category mockCategory3 = Mockito.mock(Category.class);
        when(mockCategory3.getId()).thenReturn(11L);
        when(mockCategory3.getName()).thenReturn("someCategory");
        when(mockCategory3.getPosition()).thenReturn(2);
        Category mockCategory1 = Mockito.mock(Category.class);
        when(mockCategory1.getId()).thenReturn(10L);
        when(mockCategory1.getName()).thenReturn("updatedCategoryFoo");
        when(mockCategory1.getPosition()).thenReturn(3);

        // expected
        given(this.categoryService.updateCategory(1L, 10L, request))
                .willReturn(List.of(mockCategory1, mockCategory2, mockCategory3));

        mockMvc.perform(
                        patch(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}",
                                1L, 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("category/update-category",
                                resourceDetails()
                                        .tag("Category").description("카테고리 단건 수정 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("업데이트할 카테고리의 소유자 Account ID"),
                                        parameterWithName("subId").description("업데이트할 카테고리 ID")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("수정할 카테고리 이름"),
                                        fieldWithPath("position").description("수정할 카테고리 순서 index"),
                                        fieldWithPath("visibility").description("수정할 카테고리의 공개 여부").optional()
                                ),
                                responseFields(
                                        fieldWithPath("ownerId").description("수정한 카테고리의 소유자 프로필 ID"),
                                        fieldWithPath("totalSize").description("수정된 카테고리의 전체 사이즈"),
                                        fieldWithPath("content").description("수정 이후 카테고리 목록"),
                                        fieldWithPath("content[].categoryId").description("카테고리 ID"),
                                        fieldWithPath("content[].name").description("카테고리 이름"),
                                        fieldWithPath("content[].position").description("카테고리 순서 인덱스"),
                                        fieldWithPath("content[].visibility").description("카테고리의 글에 대해 설정된 공개여부 기본 값")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void updateCategoryOrder_Docs() throws Exception {
        // given
        final CategoryPositionUpdate request = CategoryPositionUpdate.builder().newPosition(3).build();

        Category mockCategory2 = Mockito.mock(Category.class);
        when(mockCategory2.getId()).thenReturn(12L);
        when(mockCategory2.getName()).thenReturn("previousOrderWas3");
        when(mockCategory2.getPosition()).thenReturn(1);
        Category mockCategory3 = Mockito.mock(Category.class);
        when(mockCategory3.getId()).thenReturn(11L);
        when(mockCategory3.getName()).thenReturn("someCategory");
        when(mockCategory3.getPosition()).thenReturn(2);
        Category mockCategory1 = Mockito.mock(Category.class);
        when(mockCategory1.getId()).thenReturn(10L);
        when(mockCategory1.getName()).thenReturn("PreviousOrderWas1");
        when(mockCategory1.getPosition()).thenReturn(3);

        // expected
        given(this.categoryService
                .updateCategoryPosition(1L, 10L,
                        CategoryPositionUpdate.builder()
                                .newPosition(3)
                                .build())
        ).willReturn(
                List.of(mockCategory1, mockCategory2, mockCategory3)
        );

        mockMvc.perform(patch(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}/position",
                        1L, 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("category/update-category-position",
                                resourceDetails()
                                        .tag("Category").description("카테고리 순서 수정 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("업데이트할 카테고리의 소유자 Account ID"),
                                        parameterWithName("subId").description("업데이트할 카테고리 ID")
                                ),
                                requestFields(
                                        fieldWithPath("newPosition").description("업데이트할 카테고리의 순서")
                                ),
                                responseFields(
                                        fieldWithPath("ownerId").description("수정한 카테고리의 소유자 프로필 ID"),
                                        fieldWithPath("totalSize").description("수정된 카테고리의 전체 사이즈"),
                                        fieldWithPath("content[].categoryId").description("카테고리 ID"),
                                        fieldWithPath("content[].name").description("카테고리 이름"),
                                        fieldWithPath("content[].position").description("카테고리 순서 인덱스"),
                                        fieldWithPath("content[].visibility").description("카테고리의 글에 대해 설정된 공개여부 기본 값")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void deleteCategory_Docs() throws Exception {
        // expected
        mockMvc.perform(
                        delete(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_CATEGORIES + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}",
                                1L, 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("category/delete-category",
                                resourceDetails()
                                        .tag("Category").description("카테고리 삭제 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("삭제할 카테고리의 소유자 Account ID"),
                                        parameterWithName("subId").description("삭제할 카테고리 ID")
                                )
                        )
                );
    }

}
