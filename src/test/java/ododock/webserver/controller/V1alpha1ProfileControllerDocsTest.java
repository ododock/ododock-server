package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestMvcSecurityConfig;
import ododock.webserver.config.web.WebConfiguration;
import ododock.webserver.domain.profile.Profile;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.domain.profile.ProfileService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.V1alpha1ProfileController;
import ododock.webserver.web.v1alpha1.dto.account.ImageFile;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Account;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Profile;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1ProfileImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = V1alpha1ProfileController.class)
@Import({RestDocsConfig.class, TestMvcSecurityConfig.class, WebConfiguration.class})
@AutoConfigureRestDocs()
public class V1alpha1ProfileControllerDocsTest {

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
        Profile profile = mock(Profile.class);
        given(profile.getNickname()).willReturn("user1");
        given(profile.getFullname()).willReturn("John Doe");
        given(profile.getBirthDate()).willReturn(LocalDate.of(1990, 1, 1));
        given(profile.getProfileImage()).willReturn(ododock.webserver.domain.profile.ProfileImage.builder()
                .sourcePath("/img.jpg")
                .fileType("jpeg")
                .build());

        given(profileService.getProfile(1L)).willReturn(profile);

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
                                        fieldWithPath("nickname").description("조회한 프로필의 닉네임"),
                                        fieldWithPath("fullname").description("조회한 프로필의 닉네임"),
                                        fieldWithPath("birthDate").description("조회한 프로필의 생년월일"),
                                        fieldWithPath("profileImage.sourcePath").description("조회한 프로필의 프로필 사진 리소스 주소"),
                                        fieldWithPath("profileImage.fileType").description("조회한 프로필의 프로필 사진 리소스 타입")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void updateProfile_Docs() throws Exception {
        // given
        final V1alpha1Profile request = V1alpha1Profile.builder()
                .nickname("newNickname")
                .profileImage(V1alpha1ProfileImage.builder()
                        .sourcePath("newPhoto.png")
                        .fileType("png")
                        .build())
                .build();

        final V1alpha1Account response = V1alpha1Account.builder()
                .id(1L)
                .nickname("user1")
                .fullname("John Doe")
                .profileImageSource("http://oddx.xyz/img.jpg")
                .profileImageFileType("jpeg")
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
                                        fieldWithPath("profileImage.sourcePath").description("업데이트할 프로필의 프로필 사진 리소스 주소"),
                                        fieldWithPath("profileImage.fileType").description("업데이트할 프로필의 프로필 사진 파일 포맷")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void getProfileImage_Docs() throws Exception {
        // given
        ProfileImage profileImage = mock(ProfileImage.class);
        given(profileImage.getSourcePath()).willReturn("/img.jpg");
        given(profileImage.getFileType()).willReturn("jpeg");

        given(profileService.getProfileImage(1L)).willReturn(Optional.of(profileImage));

        // expected
        mockMvc.perform(
                        get(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE + ResourcePath.PROFILE_SUBRESOURCE_IMAGE, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("profile/get-profile-image",
                                resourceDetails()
                                        .tag("Profile").description("계정 프로필 이미지 조회 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("조회할 프로필 ID")
                                ),
                                responseFields(
                                        fieldWithPath("sourcePath").description("조회한 프로필의 프로필 사진 리소스 주소"),
                                        fieldWithPath("fileType").description("조회한 프로필의 프로필 사진 리소스 타입")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void saveProfile_Docs() throws Exception {
        // given
        final V1alpha1Profile request = V1alpha1Profile.builder()
                .nickname("newNickname")
                .profileImage(V1alpha1ProfileImage.builder()
                        .sourcePath("newPhoto.png")
                        .fileType("png")
                        .build())
                .build();

        final V1alpha1Account response = V1alpha1Account.builder()
                .id(1L)
                .nickname("user1")
                .fullname("John Doe")
                .profileImageSource("http://oddx.xyz/img.jpg")
                .profileImageFileType("jpeg")
                .build();

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png",
                "image/png", "Spring Framework".getBytes());

        ProfileImage profileImage = mock(ProfileImage.class);
        given(profileImage.getSourcePath()).willReturn("/img.jpg");
        given(profileImage.getFileType()).willReturn("jpeg");

        given(profileService.saveProfileImage(1L, ImageFile.from(multipartFile))).willReturn(profileImage);

        // expected
        mockMvc.perform(
                        multipart(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE + ResourcePath.PROFILE_SUBRESOURCE_IMAGE, 1L)
                                .file(multipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("profile/save-profile-image",
                                resourceDetails()
                                        .tag("Profile").description("계정 프로필 이미지 저장 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("저장할 프로필 이미지의 Account ID")
                                ),
                                requestParts(
                                        partWithName("file").description("업로드 할 프로필 이미지 파일")
                                )
                        )
                );
    }

}
