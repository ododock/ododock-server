package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.article.ArticleService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.ArticleController;
import ododock.webserver.web.v1alpha1.dto.request.ArticleCreate;
import ododock.webserver.web.v1alpha1.dto.request.ArticleUpdate;
import ododock.webserver.web.v1alpha1.dto.response.ArticleDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs()
public class ArticleControllerDocsTest {

    private static final String BASE_URL = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ARTICLES;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @Test
    @WithMockUser
    void getArticle_Docs() throws Exception {
        // given
        final ArticleDetailsResponse response = ArticleDetailsResponse.builder()
                .articleId(1L)
                .title("title")
                .body("body")
                .tags(Set.of("tag1", "tag2"))
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .categoryName("category")
                .categoryId(10L)
                .visibility(true)
                .build();
        given(articleService.getArticle(1L)).willReturn(response);

        // expected
        mockMvc.perform(
                        get(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("article/get-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 글 ID")
                                ),
                                responseFields(
                                        fieldWithPath("articleId").description("조회한 글 ID"),
                                        fieldWithPath("title").description("조회한 글 제목"),
                                        fieldWithPath("body").description("조회한 글 본문"),
                                        fieldWithPath("tags").description("조회한 글 태그").optional(),
                                        fieldWithPath("categoryId").description("조회한 카테고리 ID").optional(),
                                        fieldWithPath("categoryName").description("조회한 카테고리 이름").optional(),
                                        fieldWithPath("visibility").description("조회한 글 공개여부"),
                                        fieldWithPath("createdDate").description("조회한 글 생성일"),
                                        fieldWithPath("lastModifiedDate").description("조회한 글 최근 수정일")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void createArticle_Docs() throws Exception {
        // given
        final ArticleCreate request = ArticleCreate.builder()
                .accountId(10L)
                .title("title")
                .body("body")
                .tags(Set.of("tag"))
                .categoryId(1L)
                .visibility(true)
                .build();
        given(articleService.createArticle(request)).willReturn(20L);

        // expected
        mockMvc.perform(
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("article/create-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 생성 엔드포인트"),
                                requestFields(
                                        fieldWithPath("accountId").description("글 작성자 프로필 ID"),
                                        fieldWithPath("title").description("글 제목"),
                                        fieldWithPath("body").description("글 본문"),
                                        fieldWithPath("tags").description("글 태그").optional(),
                                        fieldWithPath("categoryId").description("글 카테고리 ID").optional(),
                                        fieldWithPath("visibility").description("글 공개여부").optional()
                                ),
                                responseFields(
                                        fieldWithPath("type").description("생성된 자원 타입"),
                                        fieldWithPath("value").description("생성된 자원 ID")
                                )
                        )
                );
    }


    @Test
    @WithMockUser
    void updateArticle_Docs() throws Exception {
        // given
        final ArticleUpdate request = ArticleUpdate.builder()
                .title("updatedTitle")
                .body("updatedBody")
                .tags(Set.of("updatedTag"))
                .categoryId(10L)
                .visibility(true)
                .build();

        // expected
        mockMvc.perform(
                        patch(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("article/update-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 수정 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("업데이트할 글 ID")
                                ),
                                requestFields(
                                        fieldWithPath("title").description("업데이트된 글 제목"),
                                        fieldWithPath("body").description("업데이트된 글 본문"),
                                        fieldWithPath("tags").description("업데이트된 글 태그").optional(),
                                        fieldWithPath("categoryId").description("업데이트된 카테고리 ID").optional(),
                                        fieldWithPath("visibility").description("글 공개여부").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void deleteArticle_Docs() throws Exception {
        // expected
        mockMvc.perform(
                        delete(BASE_URL + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("article/delete-article",
                                resourceDetails()
                                        .tag("Article").description("아티클 삭제 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("삭제할 글 ID")
                                )
                        )
                );
    }

}
