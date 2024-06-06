package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestSecurityConfig;
import ododock.webserver.domain.account.Role;
import ododock.webserver.response.account.AccountDetailsResponse;
import ododock.webserver.service.AccountQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountQueryController.class)
@Import({RestDocsConfig.class, TestSecurityConfig.class})
@AutoConfigureRestDocs
public class AccountQueryControllerDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountQueryService accountQueryService;


    @Test
    void getAccount_Docs() throws Exception {
        // given
        final AccountDetailsResponse response = AccountDetailsResponse.builder()
                .sub(1l)
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
        given(accountQueryService.getAccountDetails(1L)).willReturn(response);

        // expected
        mockMvc.perform(
                        get("/api/v1/accounts/{accountId}", 1L)
                                .with(user("tester@ododock.io").password("password").roles(Role.USER.toString()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("account/get-account-details",
                        pathParameters(
                                parameterWithName("accountId").description("조회할 Account ID")
                        ),
                        responseFields(
                                fieldWithPath("sub").description("조회한 Account ID"),
                                fieldWithPath("profileId").description("조회한 Profile ID"),
                                fieldWithPath("email").description("조회한 Account 이메일"),
                                fieldWithPath("fullname").description("조회한 Account 이름"),
                                fieldWithPath("birthDate").description("조회한 Account 생년월일"),
                                fieldWithPath("createdDate").description("조회한 Account 최초 생성일"),
                                fieldWithPath("lastModifiedDate").description("조회한 Account 최근 수정일"),
                                fieldWithPath("credentialNonExpired").description("조회한 Account 비밀번호 만료 여부"),
                                fieldWithPath("accountNonExpired").description("조회한 Account 만료 여부"),
                                fieldWithPath("accountNonLocked").description("조회한 Account 잠금 여부"),
                                fieldWithPath("enabled").description("조회한 Account 활성화 여부"),
                                fieldWithPath("isDaoSignedUp").description("조회한 Account의 DB 설정 여부"),
                                fieldWithPath("nickname").description("조회한 Account의 Profile 닉네임"),
                                fieldWithPath("providers").description("조회한 Account의 연동된 Social Accounts"),
                                fieldWithPath("profileImage").description("조회한 Account의 프로필 이미지")
                        )
                ));
    }


}
