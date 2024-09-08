package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.request.ProfileImageUpdate;
import ododock.webserver.request.ProfileUpdate;
import ododock.webserver.response.CategoryDetailsResponse;
import ododock.webserver.response.ProfileDetailsResponse;
import ododock.webserver.service.ProfileService;
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
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
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

@WebMvcTest(controllers = ProfileController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs()
public class ProfileControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @Test
    @WithMockUser
    void validateNickname_Docs() throws Exception {
        // given
        given(profileService.isAvailableNickname("admin")).willReturn(true);

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
                        resourceDetails().tag("Profile").description("프로필 nickname 중복여부 검증 엔드포인트"),
                        queryParameters(
                                parameterWithName("nickname").description("검증할 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("availability").description("닉네임 존재여부")
                        )
                ));
    }

    @Test
    @WithMockUser
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
                .imageSource("http://oddk.xyz/img.jpg")
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
                        resourceDetails().tag("Profile").description("계정 프로필 정보 조회 엔드포인트"),
                        pathParameters(
                                parameterWithName("profileId").description("조회할 프로필 ID")
                        ),
                        responseFields(
                                fieldWithPath("ownerAccountId").description("조회한 프로필의 소유자 DB 계정 ID"),
                                fieldWithPath("profileId").description("조회한 프로필 ID"),
                                fieldWithPath("nickname").description("조회한 프로필의 닉네임"),
                                fieldWithPath("imageSource").description("조회한 프로필의 프로필 사진 리소스 주소"),
                                fieldWithPath("fileType").description("조회한 프로필의 프로필 사진 파일 포맷"),
                                fieldWithPath("categories").description("조회한 프로필의 카테고리 목록"),
                                fieldWithPath("categories[].categoryId").description("카테고리 ID"),
                                fieldWithPath("categories[].order").description("카테고리의 순서"),
                                fieldWithPath("categories[].name").description("카테고리 이름"),
                                fieldWithPath("categories[].visibility").description("카테고리 공개여부"),
                                fieldWithPath("createdDate").description("조회한 프로필의 최초 생성일"),
                                fieldWithPath("lastModifiedDate").description("조회한 프로필의 최근 수정일")
                        )
                ));
    }

    @Test
    @WithMockUser
    void updateProfile_Docs() throws Exception {
        // given
        final ProfileUpdate request = ProfileUpdate.builder()
                .nickname("newNickname")
                .imageSource("http://oddk.xyz/newPhoto.png")
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
                .imageSource("http://oddk.xyz/img.jpg")
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
                        resourceDetails().tag("Profile").description("계정 프로필 수정 엔드포인트"),
                        pathParameters(
                                parameterWithName("profileId").description("업데이트할 프로필의 ID")
                        ),
                        requestFields(
                                fieldWithPath("nickname").description("업데이트할 프로필의 닉네임"),
                                fieldWithPath("imageSource").description("업데이트할 프로필의 프로필 사진 리소스 주소"),
                                fieldWithPath("fileType").description("업데이트할 프로필의 프로필 사진 파일 포맷")
                        )
                ));
    }

    @Test
    @WithMockUser
    void updateProfileImage_Docs() throws Exception {
        // given
        final ProfileImageUpdate request = ProfileImageUpdate.builder()
                .imageSource("http://oddk.xyz/newPhoto.png")
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
                        resourceDetails().tag("Profile").description("계정 프로필 이미지 수정 엔드포인트"),
                        pathParameters(
                                parameterWithName("profileId").description("업데이트할 프로필의 ID")
                        ),
                        requestFields(
                                fieldWithPath("imageSource").description("업데이트할 프로필의 프로필 사진 리소스 주소"),
                                fieldWithPath("fileType").description("업데이트할 프로필의 프로필 사진 파일 포맷")
                        )
                ));
    }

}
