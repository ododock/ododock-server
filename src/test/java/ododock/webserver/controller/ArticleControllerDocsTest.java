package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.request.ArticleCreate;
import ododock.webserver.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs()
public class ArticleControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @Test
    void docs() throws Exception {
        // given
        final ArticleCreate request = ArticleCreate.builder()
                .profileId(10L)
                .title("title")
                .body("body")
                .tags(Set.of("tag"))
                .categoryId(1L)
                .visibility(true)
                .build();
        given(articleService.createArticle(request)).willReturn(20L);

        // expected
        mockMvc.perform(
                        post("/api/v1/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("article/create-article",
                        requestFields(
                                fieldWithPath("profileId").description("글 작성자 프로필 ID"),
                                fieldWithPath("title").description("글 제목"),
                                fieldWithPath("body").description("글 본문"),
                                fieldWithPath("tags").description("글 태그").optional(),
                                fieldWithPath("categoryId").description("글 카테고리 ID").optional(),
                                fieldWithPath("visibility").description("글 공개여부")
                        ),
                        responseFields(
                                fieldWithPath("type").description("생성된 자원 타입"),
                                fieldWithPath("value").description("생성된 자원 ID")
                        )
                ));
    }

}
