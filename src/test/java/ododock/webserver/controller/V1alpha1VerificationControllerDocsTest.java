package ododock.webserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestMvcSecurityConfig;
import ododock.webserver.config.web.WebConfiguration;
import ododock.webserver.domain.verification.VerificationService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.V1alpha1VerificationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = V1alpha1VerificationController.class)
@Import({RestDocsConfig.class, TestMvcSecurityConfig.class, WebConfiguration.class})
@AutoConfigureRestDocs
public class V1alpha1VerificationControllerDocsTest {

    private static final String BASE_PATH = ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.VERIFICATION;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VerificationService verificationService;

    @Test
    @WithMockUser
    void sendVerificationCode_Docs() throws Exception {
        // given
        String email = "test-user@oddk.xyz";
        ObjectNode request = objectMapper.createObjectNode();
        request.put("email", email);

        // expected
        mockMvc.perform(
                        post(BASE_PATH + ResourcePath.ACCOUNTS)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("verifications/verify-account",
                                resourceDetails()
                                        .tag("Verifications").description("계정 이메일 인증을 위한 코드발급 엔드포인트"),
                                requestFields(
                                        fieldWithPath("email").description("회원가입한 DB계정의 등록 이메일(인증코드 발급 주소)")
                                )
                        )
                );
    }

}
