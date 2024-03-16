package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.request.AccountCreate;
import ododock.webserver.request.AccountPasswordUpdate;
import ododock.webserver.response.AccountCreateResponse;
import ododock.webserver.response.AccountDetailsResponse;
import ododock.webserver.response.ValidateResponse;
import ododock.webserver.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs()
public class AccountControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void getAccount_Docs() throws Exception {
        // given
        final AccountDetailsResponse response = AccountDetailsResponse.builder()
                .id(1l)
                .email("tester@ododock.io")
                .fullname("John doe")
                .birthDate(LocalDate.of(1999, 05, 23))
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .credentialNonExpired(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .build();
        given(accountService.getAccount(1L)).willReturn(response);

        // expected
        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/get-account",
                        pathParameters(
                                parameterWithName("accountId").description("조회할 Account ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("조회한 Account ID"),
                                fieldWithPath("email").description("조회한 Account 이메일"),
                                fieldWithPath("fullname").description("조회한 Account 이름"),
                                fieldWithPath("birthDate").description("조회한 Account 생년월일"),
                                fieldWithPath("createdDate").description("조회한 Account 최초 생성일"),
                                fieldWithPath("lastModifiedDate").description("조회한 Account 최근 수정일"),
                                fieldWithPath("credentialNonExpired").description("조회한 Account 비밀번호 만료 여부"),
                                fieldWithPath("accountNonExpired").description("조회한 Account 만료 여부"),
                                fieldWithPath("accountNonLocked").description("조회한 Account 잠금 여부"),
                                fieldWithPath("enabled").description("조회한 Account 활성화 여부")
                        )
                ));
    }

    @Test
    void validateEmail_Docs() throws Exception {
        // given
        final ValidateResponse response = ValidateResponse.of(true);
        given(accountService.isAvailableEmail("tester@ododock.io")).willReturn(true);

        // expected
        mockMvc.perform(
                        get("/api/v1/accounts")
                                .param("email", "tester@ododock.io")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/validate-email",
                        responseFields(
                                fieldWithPath("availability").description("주어진 email 사용가능 여부")
                        )
                ));
    }

    @Test
    void createAccount_Docs() throws Exception {
        // given
        final AccountCreate requset = AccountCreate.builder()
                .email("tester@ododock.io")
                .password("1q2w3e4r")
                .fullname("John doe")
                .birthDate(LocalDate.of(1999, 05, 23))
                .nickname("john.doe")
                .imageSource("sample.png")
                .fileType("png")
                .build();
        final AccountCreateResponse response = AccountCreateResponse.builder()
                .accountId(1L)
                .profileId(10L)
                .build();

        given(accountService.createAccount(requset)).willReturn(response);

        // expected
        mockMvc.perform(
                        post("/api/v1/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/create-account",
                        requestFields(
                                fieldWithPath("email").description("생성할 Account 이메일"),
                                fieldWithPath("password").description("생성할 Account 비밀번호"),
                                fieldWithPath("fullname").description("생성할 Account 유저 이름"),
                                fieldWithPath("birthDate").description("생성할 Account 유저 생년월일"),
                                fieldWithPath("nickname").description("생성할 Account의 Profile 닉네임"),
                                fieldWithPath("imageSource").description("생성할 Account의 Profile 이미지 소스"),
                                fieldWithPath("fileType").description("생성할 Account의 Profile 이미지 포맷")
                        ),
                        responseFields(
                                fieldWithPath("accountId").description("생성된 Account ID"),
                                fieldWithPath("profileId").description("생성된 Profile ID")
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
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requset))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/update-account-password",
                        requestFields(
                                fieldWithPath("password").description("변경할 비밀번호")
                        )
                ));
    }

    @Test
    void deleteAccount_Docs() throws Exception {
        // given

        // expected
        mockMvc.perform(
                        delete("/api/v1/accounts/{accountId}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/delete-account",
                        pathParameters(
                                parameterWithName("accountId").description("삭제할 Account ID")
                        )
                ));
    }


}
