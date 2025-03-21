package ododock.webserver.web.v1alpha1;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import ododock.webserver.web.ResourcePath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.AUTH)
@RequiredArgsConstructor
public class V1alpha1AuthController {

    @PostMapping(
            value = ResourcePath.LOGOUT
    )
    public ResponseEntity<?> logout(final HttpServletRequest request) {
        return ResponseEntity.ok().build();
    }

}
