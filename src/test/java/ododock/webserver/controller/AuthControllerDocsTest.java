package ododock.webserver.controller;

import ododock.webserver.common.RestDocsConfig;
import ododock.webserver.common.TestMvcSecurityConfig;
import ododock.webserver.web.v1alpha1.V1alpha1AuthController;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@WebMvcTest(controllers = V1alpha1AuthController.class)
@Import({RestDocsConfig.class, TestMvcSecurityConfig.class})
@AutoConfigureRestDocs
public class AuthControllerDocsTest {
}
