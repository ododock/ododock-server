package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.account.ProfileImage;
import ododock.webserver.domain.account.ProfileService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1.ProfileController;
import ododock.webserver.web.v1.dto.ProfileUpdate;
import ododock.webserver.web.v1.dto.response.ProfileDetailsResponse;
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

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs()
public class ProfileControllerDocsTest {

    private static final String BASE_PATH = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @Test
    @WithMockUser
    void getProfile_Docs() throws Exception {
        // given
        final ProfileDetailsResponse response = ProfileDetailsResponse.builder()
                .accountId(1L)
                .nickname("user1")
                .fullname("John Doe")
                .profileImage(ProfileImage.builder()
                        .imageSource("http://oddx.xyz/img.jpg")
                        .fileType("jpeg")
                        .build())
                .build();
        given(profileService.getProfile(1L)).willReturn(response);

        // expected
        mockMvc.perform(
                        get(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("profile/get-profile",
                                resourceDetails()
                                        .tag("Profile").description("계정 프로필 정보 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 프로필 ID")
                                ),
                                responseFields(
                                        fieldWithPath("accountId").description("조회한 프로필의 소유자 Account ID"),
                                        fieldWithPath("nickname").description("조회한 프로필의 닉네임"),
                                        fieldWithPath("fullname").description("조회한 프로필의 닉네임"),
                                        fieldWithPath("birthDate").description("조회한 프로필의 생년월일"),
                                        fieldWithPath("profileImage.imageSource").description("조회한 프로필의 프로필 사진 리소스 주소"),
                                        fieldWithPath("profileImage.fileType").description("조회한 프로필의 프로필 사진 리소스 타입")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void updateProfile_Docs() throws Exception {
        // given
        final ProfileUpdate request = ProfileUpdate.builder()
                .nickname("newNickname")
                .profileImageSource("http://oddk.xyz/newPhoto.png")
                .profileImageFileType("png")
                .build();

        final ProfileDetailsResponse response = ProfileDetailsResponse.builder()
                .accountId(1L)
                .nickname("user1")
                .fullname("John Doe")
                .profileImage(ProfileImage.builder()
                        .imageSource("http://oddx.xyz/img.jpg")
                        .fileType("jpeg")
                        .build())
                .build();

        // expected
        mockMvc.perform(
                        patch(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("profile/update-profile",
                                resourceDetails()
                                        .tag("Profile").description("계정 프로필 수정 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("업데이트할 프로필의 Account ID")
                                ),
                                requestFields(
                                        fieldWithPath("nickname").description("업데이트할 프로필의 닉네임"),
                                        fieldWithPath("profileImageSource").description("업데이트할 프로필의 프로필 사진 리소스 주소"),
                                        fieldWithPath("profileImageFileType").description("업데이트할 프로필의 프로필 사진 파일 포맷")
                                )
                        )
                );
    }

}
