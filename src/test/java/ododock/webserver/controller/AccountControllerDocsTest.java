package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.account.AccountManageService;
import ododock.webserver.domain.account.AccountService;
import ododock.webserver.domain.account.Role;
import ododock.webserver.domain.account.SocialAccountService;
import ododock.webserver.domain.notification.MailService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.AccountController;
import ododock.webserver.web.v1alpha1.dto.account.*;
import ododock.webserver.web.v1alpha1.dto.response.ValidateResponse;
import ododock.webserver.web.v1alpha1.dto.response.account.AccountCreateResponse;
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
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs
public class AccountControllerDocsTest {

    private static final String BASE_PATH = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountManageService accountManageService;

    @MockBean
    private SocialAccountService socialAccountService;

    @MockBean
    private MailService mailService;

    @Test
    @WithMockUser
    void validateEmail_Docs() throws Exception {
        // given
        final ValidateResponse response = ValidateResponse.of(true);
        given(accountService.isAvailableEmail("tester@oddk.xyz")).willReturn(true);

        // expected
        mockMvc.perform(
                        get(BASE_PATH)
                                .queryParam("email", "tester@oddk.xyz"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/validate-email",
                                resourceDetails()
                                        .tag("Account").description("email 중복여부 검증 엔드포인트"),
                                queryParameters(
                                        parameterWithName("email").description("중복여부 검증할 email")
                                ),
                                responseFields(
                                        fieldWithPath("availability").description("주어진 email 사용가능 여부")
                                )
                        )
                );
    }


    @Test
    @WithMockUser
    void validateNickname_Docs() throws Exception {
        // given
        given(accountService.isAvailableNickname("admin")).willReturn(true);

        // expected
        mockMvc.perform(
                        get(BASE_PATH, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .queryParam("nickname", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/validate-nickname",
                                resourceDetails()
                                        .tag("Account").description("계정 nickname 중복여부 검증 엔드포인트"),
                                queryParameters(
                                        parameterWithName("nickname").description("검증할 닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("availability").description("닉네임 존재여부")
                                )
                        )
                );
    }


    @Test
    @WithMockUser
    void createDaoAccount_Docs() throws Exception {
        // given
        final AccountCreate requset = AccountCreate.builder()
                .email("tester@oddk.xyz")
                .password("1q2w3e4r")
                .fullname("John doe")
                .birthDate(LocalDate.of(1999, 05, 23))
                .nickname("john.doe")
                .attributes(Map.of())
                .build();
        final AccountCreateResponse response = AccountCreateResponse.builder()
                .sub(1L)
                .profileId(10L)
                .build();

        // expected
        mockMvc.perform(
                        post(BASE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/create-dao-account",
                                resourceDetails()
                                        .tag("Account").description("DB 계정 생성 엔드포인트"),
                                requestFields(
                                        fieldWithPath("email").description("생성할 계정 이메일"),
                                        fieldWithPath("password").description("생성할 계정 비밀번호"),
                                        fieldWithPath("fullname").description("생성할 계정의 유저 이름").optional(),
                                        fieldWithPath("birthDate").description("생성할 계정 유저 생년월일").optional(),
                                        fieldWithPath("nickname").description("생성할 계정의 프로필 닉네임").optional(),
                                        fieldWithPath("attributes").description("생성할 계정 추가 속성필드").optional(),
                                        fieldWithPath("profileImageSource").description("생성할 계정의 프로필 이미지 소스").optional(),
                                        fieldWithPath("profileImageFileType").description("생성할 계정의 프로필 이미지 파일 타입").optional()
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void mergeSocialAccount_Docs() throws Exception {
        // given
        final OAuthAccountMerge requset = OAuthAccountMerge.builder()
                .targetAccountId(2L)
                .oauthProvider("naver")
                .build();

        // expected
        mockMvc.perform(
                        post(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_SOCIAL_ACCOUNTS, 1L)
                                .with(user("tester@oddk.xyz").password("password").roles(Role.USER.toString()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/merge-social-account",
                                resourceDetails()
                                        .tag("Account").description("기존의 DB 계정에 소셜 계정 연동 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("연동을 요청한 DB 계정 ID")
                                ),
                                requestFields(
                                        fieldWithPath("oauthProvider").description("연동할 소셜 계정 제공사"),
                                        fieldWithPath("targetAccountId").description("연동할 소셜 계정의 ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void verifyDaoAccountEmail_Docs() throws Exception {
        // given
        final CompleteDaoAccountVerification request = CompleteDaoAccountVerification.builder()
                .email("testuser@oddk.xyz")
                .verificationCode("5252")
                .build();

        // expected
        mockMvc.perform(
                        post(BASE_PATH + ResourcePath.VERIFICATION)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/complete-dao-account-register",
                                resourceDetails()
                                        .tag("Account").description("DB 회원가입 계정의 이메일 인증 완료 엔드포인트"),
                                requestFields(
                                        fieldWithPath("email").description("회원가입 완료할 계정의 이메일"),
                                        fieldWithPath("verificationCode").description("회원가입 완료할 계정의 이메일로 발급된 이메일 검증 코드")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void completeSocialAccountRegister_Docs() throws Exception {
        // given
        final CompleteSocialAccountRegister request = CompleteSocialAccountRegister.builder()
                .fullname("테스트유저")
                .nickname("testuser")
                .password("password")
                .build();

        // expected
        mockMvc.perform(
                        put(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/complete-social-account-register",
                                resourceDetails()
                                        .tag("Account").description("최초 소셜 가입 계정의 회원가입 완료 처리 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("회원가입 완료할 DB 계정 ID")
                                ),
                                requestFields(
                                        fieldWithPath("fullname").description("회원가입 완료할 계정의 유저 이름"),
                                        fieldWithPath("nickname").description("회원가입 완료할 계정의 프로필 닉네임"),
                                        fieldWithPath("password").description("회원가입 완료할 계정의 비밀번호")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void updateAccountPassword_Docs() throws Exception {
        // given
        final AccountPasswordUpdate requset = AccountPasswordUpdate.builder()
                .password("qwer1234")
                .build();

        // expected
        mockMvc.perform(
                        patch(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PASSWORD, 1L)
                                .with(user("tester@ododock.io").password("password").roles(Role.USER.toString()))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/update-account-password",
                                resourceDetails()
                                        .tag("Account").description("DB 계정의 비밀번호 업데이트"),
                                pathParameters(
                                        parameterWithName("id").description("비밀번호를 변경할 계정 ID")
                                ),
                                requestFields(
                                        fieldWithPath("password").description("변경할 비밀번호")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void resetAccountPassword_Docs() throws Exception {
        // given
        String email = "test-user@oddk.xyz";
        final AccountPasswordReset request = AccountPasswordReset.builder()
                .verificationCode(UUID.randomUUID().toString())
                .newPassword("123456")
                .build();

        // expected
        mockMvc.perform(
                        put(BASE_PATH + "/{" + ResourcePath.PATH_VAR_NAME + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PASSWORD, email)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/reset-account-password",
                                resourceDetails()
                                        .tag("Account").description("DB 회원가입 계정의 비밀번호 재설정 엔드포인트"),
                                pathParameters(
                                        parameterWithName("name").description("회원가입 완료할 DB 계정 email")
                                ),
                                requestFields(
                                        fieldWithPath("verificationCode").description("비밀번호를 재설정할 계정에 발급된 인증 코드"),
                                        fieldWithPath("newPassword").description("비밀번호를 재설정할 계정의 새 비밀번호")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void deleteAccount_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(
                        delete(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}", 1L)
                                .with(user("tester@oddk.xyz").password("password").roles(Role.USER.toString()))
                                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/delete-account",
                                resourceDetails()
                                        .tag("Account").description("DB 계정 삭제 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("삭제할 Account ID")
                                )
                        )
                );
    }

    @Test
    @WithMockUser
    void deleteSocialAccount_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(
                        delete(BASE_PATH + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_SOCIAL_ACCOUNTS + "/{" + ResourcePath.PATH_VAR_SUB_ID + "}", 1L, 2L)
                                .with(user("tester@ododock.io").password("password").roles(Role.USER.toString())))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/delete-social-account",
                                resourceDetails()
                                        .tag("Account").description("연동된 소셜계정 삭제 엔드포인트"),
                                pathParameters(
                                        parameterWithName("id").description("삭제할 Account ID"),
                                        parameterWithName("subId").description("삭제할 Social Account ID")
                                )
                        )
                );
    }

}
