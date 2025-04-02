package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.profile.ProfileService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Profile;
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
    public V1alpha1Profile getProfile(
            final @PathVariable Long id
    ) {
        return V1alpha1Profile.toControllerDto(profileService.getProfile(id));
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE
    )
    public ResponseEntity<Void> updateProfile(
            final @PathVariable Long id,
            final @Valid @RequestBody V1alpha1Profile profile
    ) {
        profileService.updateProfile(id, profile.toDomainDto());
        return ResponseEntity.ok().build();
    }

}
