package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.account.Role;
import ododock.webserver.request.account.AccountCreate;
import ododock.webserver.request.account.AccountPasswordReset;
import ododock.webserver.request.account.AccountPasswordUpdate;
import ododock.webserver.request.account.CompleteDaoAccountVerification;
import ododock.webserver.request.account.CompleteSocialAccountRegister;
import ododock.webserver.request.account.OAuthAccountConnect;
import ododock.webserver.response.ValidateResponse;
import ododock.webserver.response.account.AccountCreateResponse;
import ododock.webserver.response.account.AccountVerified;
import ododock.webserver.service.AccountService;
import ododock.webserver.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs
public class AccountControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private MailService mailService;

    @Test
    void validateEmail_Docs() throws Exception {
        // given
        final ValidateResponse response = ValidateResponse.of(true);
        given(accountService.isAvailableEmail("tester@oddk.xyz")).willReturn(true);

        // expected
        mockMvc.perform(
                        get("/api/v1/accounts")
                                .queryParam("email", "tester@oddk.xyz")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(
                        document("account/validate-email",
                                resourceDetails().tag("Account").description("email 중복여부 검증 엔드포인트"),
                                queryParameters(
                                        parameterWithName("email").description("중복여부 검증할 email")
                                ),
                                responseFields(
                                        fieldWithPath("availability").description("주어진 email 사용가능 여부")
                                )
                        ));
    }

    @Test
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

        given(accountService.createDaoAccount(requset)).willReturn(response);

        // expected
        mockMvc.perform(
                        post("/api/v1/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/create-dao-account",
                        resourceDetails().tag("Account").description("DB 계정 생성 엔드포인트"),
                        requestFields(
                                fieldWithPath("email").description("생성할 계정 이메일"),
                                fieldWithPath("password").description("생성할 계정 비밀번호"),
                                fieldWithPath("fullname").description("생성할 계정의 유저 이름").optional(),
                                fieldWithPath("birthDate").description("생성할 계정 유저 생년월일").optional(),
                                fieldWithPath("nickname").description("생성할 계정의 프로필 닉네임").optional(),
                                fieldWithPath("attributes").description("생성할 계정 추가 속성필드").optional()
                        ),
                        responseFields(
                                fieldWithPath("sub").description("생성된 계정 ID"),
                                fieldWithPath("profileId").description("생성된 프로필 ID")
                        )
                ));
    }

    @Test
    void connectSocialAccount_Docs() throws Exception {
        // given
        final OAuthAccountConnect requset = OAuthAccountConnect.builder()
                .targetAccountId(2L)
                .oauthProvider("naver")
                .build();

        // expected
        mockMvc.perform(
                        post("/api/v1/accounts/{accountId}/social-accounts", 1L)
                                .with(user("tester@oddk.xyz").password("password").roles(Role.USER.toString()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/connect-social-account",
                        resourceDetails().tag("Account").description("기존의 DB 계정에 소셜 계정 연동 엔드포인트"),
                        pathParameters(
                                parameterWithName("accountId").description("연동을 요청한 DB 계정 ID")
                        ),
                        requestFields(
                                fieldWithPath("oauthProvider").description("연동할 소셜 계정 제공사"),
                                fieldWithPath("targetAccountId").description("연동할 소셜 계정의 ID")
                        )
                ));
    }

    @Test
    @WithMockUser
    void sendVerificationCode_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(
                        put("/api/v1/accounts/{accountId}/verification-code", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("email", "testuser@oddk.xyz")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/send-verification-code",
                        resourceDetails().tag("Account").description("DB 계정의 이메일 인증 코드 요청"),
                        queryParameters(
                                parameterWithName("email").description("회원가입한 DB계정의 등록 이메일(인증코드 발급 주소)")
                        ),
                        pathParameters(
                                parameterWithName("accountId").description("인증 코드를 발송한 DB 계정 ID")
                        )
                ));
    }


    @Test
    @WithMockUser
    void verifyDaoAccountEmail_Docs() throws Exception {
        // given
        final CompleteDaoAccountVerification request = CompleteDaoAccountVerification.builder()
                .email("testuser@oddk.xyz")
                .code("5252")
                .build();
        final AccountVerified response = AccountVerified.builder()
                .code(UUID.randomUUID().toString())
                .expiredAt(LocalDateTime.now().plusMinutes(5L))
                .build();

        given(accountService.verifyDaoAccountEmail(1L, request)).willReturn(response);


        // expected
        mockMvc.perform(
                        post("/api/v1/accounts/{accountId}/verification-code", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/complete-dao-account-register",
                        resourceDetails().tag("Account").description("DB 회원가입 계정의 이메일 인증 완료 엔드포인트"),
                        pathParameters(
                                parameterWithName("accountId").description("회원가입 완료할 DB 계정 ID")
                        ),
                        requestFields(
                                fieldWithPath("email").description("회원가입 완료할 계정의 이메일"),
                                fieldWithPath("code").description("회원가입 완료할 계정의 이메일로 발급된 이메일 검증 코드")
                        ),
                        responseFields(
                                fieldWithPath("code").description("비밀번호 재설정 시 사용되는 코드"),
                                fieldWithPath("expiredAt").description("비밀번호 재설정 코드 만료 시간")
                        )
                ));
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
                        put("/api/v1/accounts/{accountId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/complete-social-account-register",
                        resourceDetails().tag("Account").description("최초 소셜 가입 계정의 회원가입 완료 처리 엔드포인트"),
                        pathParameters(
                                parameterWithName("accountId").description("회원가입 완료할 DB 계정 ID")
                        ),
                        requestFields(
                                fieldWithPath("fullname").description("회원가입 완료할 계정의 유저 이름"),
                                fieldWithPath("nickname").description("회원가입 완료할 계정의 프로필 닉네임"),
                                fieldWithPath("password").description("회원가입 완료할 계정의 비밀번호")
                        )
                ));
    }

    @Test
    void updateAccountPassword_Docs() throws Exception {
        // given
        final AccountPasswordUpdate requset = AccountPasswordUpdate.builder()
                .password("qwer1234")
                .build();

        // expected
        mockMvc.perform(
                        patch("/api/v1/accounts/{accountId}/password", 1L)
                                .with(user("tester@ododock.io").password("password").roles(Role.USER.toString()))
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/update-account-password",
                        resourceDetails().tag("Account").description("DB 계정의 비밀번호 업데이트"),
                        pathParameters(
                                parameterWithName("accountId").description("비밀번호를 변경할 계정 ID")
                        ),
                        requestFields(
                                fieldWithPath("password").description("변경할 비밀번호")
                        )
                ));
    }

    @Test
    @WithMockUser
    void resetAccountPassword_Docs() throws Exception {
        // given
        final AccountPasswordReset request = AccountPasswordReset.builder()
                .code(UUID.randomUUID().toString())
                .newPassword("123456")
                .build();

        // expected
        mockMvc.perform(
                        put("/api/v1/accounts/{accountId}/password", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/reset-account-password",
                        resourceDetails().tag("Account").description("DB 회원가입 계정의 비밀번호 재설정 엔드포인트"),
                        pathParameters(
                                parameterWithName("accountId").description("회원가입 완료할 DB 계정 ID")
                        ),
                        requestFields(
                                fieldWithPath("code").description("비밀번호를 재설정할 계정에 발급된 인증 코드"),
                                fieldWithPath("newPassword").description("비밀번호를 재설정할 계정의 새 비밀번호")
                        )
                ));
    }

    @Test
    void deleteAccount_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(
                        delete("/api/v1/accounts/{accountId}", 1L)
                                .with(user("tester@oddk.xyz").password("password").roles(Role.USER.toString()))
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/delete-account",
                        resourceDetails().tag("Account").description("DB 계정 삭제 엔드포인트"),
                        pathParameters(
                                parameterWithName("accountId").description("삭제할 Account ID")
                        )
                ));
    }

    @Test
    void deleteSocialAccount_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(
                        delete("/api/v1/accounts/{accountId}/social-accounts/{socialAccountId}", 1L, 2L)
                                .with(user("tester@ododock.io").password("password").roles(Role.USER.toString()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/delete-social-account",
                        resourceDetails().tag("Account").description("연동된 소셜계정 삭제 엔드포인트"),
                        pathParameters(
                                parameterWithName("accountId").description("삭제할 Account ID"),
                                parameterWithName("socialAccountId").description("삭제할 Social Account ID")
                        )
                ));
    }


}
