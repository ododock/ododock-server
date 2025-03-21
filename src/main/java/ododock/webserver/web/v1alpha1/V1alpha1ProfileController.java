package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.ProfileService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.request.ProfileUpdate;
import ododock.webserver.web.v1alpha1.dto.response.ProfileDetailsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS)
@RequiredArgsConstructor
public class V1alpha1ProfileController {

    private final ProfileService profileService;

    @GetMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE
    )
    public ProfileDetailsResponse getProfile(
            final @PathVariable Long id
    ) {
        return profileService.getProfile(id);
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE
    )
    public ResponseEntity<Void> updateProfile(
            final @PathVariable Long id,
            final @Valid @RequestBody ProfileUpdate request
    ) {
        profileService.updateProfile(id, request);
        return ResponseEntity.ok().build();
    }

}
