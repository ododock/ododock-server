package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.request.ProfileImageUpdate;
import ododock.webserver.request.ProfileUpdate;
import ododock.webserver.response.CategoryDetailsResponse;
import ododock.webserver.response.ProfileDetailsResponse;
import ododock.webserver.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs()
public class ProfileControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @Test
    void getProfile_Docs() throws Exception {
        // given
        final ProfileDetailsResponse response = ProfileDetailsResponse.builder()
                .profileId(1L)
                .categories(List.of(CategoryDetailsResponse.builder()
                        .categoryId(10L)
                        .visibility(true)
                        .name("category1")
                        .build()))
                .nickname("user1")
                .imageSource("http://example.com/img.jpg")
                .fileType("jpeg")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
        given(profileService.getProfile(1L)).willReturn(response);

        // expected
        mockMvc.perform(
                        get("/api/v1/profiles/{profileId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("profile/get-profile",
                        pathParameters(
                                parameterWithName("profileId").description("조회할 프로필 ID")
                        ),
                        responseFields(
                                fieldWithPath("profileId").description("조회한 프로필 ID"),
                                fieldWithPath("nickname").description("조회한 프로필의 닉네임"),
                                fieldWithPath("imageSource").description("조회한 프로필의 프로필 사진 리소스 주소"),
                                fieldWithPath("fileType").description("조회한 프로필의 프로필 사진 파일 포맷"),
                                fieldWithPath("categories").description("조회한 프로필의 카테고리 목록"),
                                fieldWithPath("categories[].categoryId").description("카테고리 ID"),
                                fieldWithPath("categories[].name").description("카테고리 이름"),
                                fieldWithPath("categories[].visibility").description("카테고리 공개여부"),
                                fieldWithPath("createdDate").description("조회한 프로필의 최초 생성일"),
                                fieldWithPath("lastModifiedDate").description("조회한 프로필의 최근 수정일")
                        )
                ));
    }

    @Test
    void validateNickname_Docs() throws Exception {
        // given
        given(profileService.validateNickname("admin")).willReturn(true);

        // expected
        mockMvc.perform(
                        get("/api/v1/profiles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .param("nickname", "admin")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("profile/validate-nickname",
                        queryParameters(
                                parameterWithName("nickname").description("검증할 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("availability").description("닉네임 존재여부")
                        )
                ));
    }

    @Test
    void updateProfile_Docs() throws Exception {
        // given
        final ProfileUpdate request = ProfileUpdate.builder()
                .nickname("newNickname")
                .imageSource("http://example.com/newPhoto.png")
                .fileType("png")
                .build();

        final ProfileDetailsResponse response = ProfileDetailsResponse.builder()
                .profileId(1L)
                .categories(List.of(CategoryDetailsResponse.builder()
                        .categoryId(10L)
                        .visibility(true)
                        .name("category1")
                        .build()))
                .nickname("user1")
                .imageSource("http://example.com/img.jpg")
                .fileType("jpeg")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        // expected
        mockMvc.perform(
                        patch("/api/v1/profiles/{profileId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("profile/update-profile",
                        pathParameters(
                                parameterWithName("profileId").description("업데이트할 프로필 ID")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("업데이트할 프로필의 닉네임"),
                                fieldWithPath("imageSource").description("업데이트할 프로필의 프로필 사진 리소스 주소"),
                                fieldWithPath("fileType").description("업데이트할 프로필의 프로필 사진 파일 포맷")
                        )
                ));
    }

    @Test
    void updateProfileImage_Docs() throws Exception {
        // given
        final ProfileImageUpdate request = ProfileImageUpdate.builder()
                .imageSource("http://example.com/newPhoto.png")
                .fileType("png")
                .build();

        // expected
        mockMvc.perform(
                        patch("/api/v1/profiles/{profileId}/profileImage", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("profile/update-profile-image",
                        pathParameters(
                                parameterWithName("profileId").description("업데이트할 프로필 ID")
                        ),
                        requestFields(
                                fieldWithPath("imageSource").description("업데이트할 프로필의 프로필 사진 리소스 주소"),
                                fieldWithPath("fileType").description("업데이트할 프로필의 프로필 사진 파일 포맷")
                        )
                ));
    }

}
