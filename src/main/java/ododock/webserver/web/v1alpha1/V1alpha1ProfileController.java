package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.profile.ProfileImage;
import ododock.webserver.domain.profile.ProfileService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.account.ImageFile;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Profile;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1ProfileImage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS + "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE)
@RequiredArgsConstructor
public class V1alpha1ProfileController {

    private final ProfileService profileService;

    @GetMapping(
            value = ""
    )
    public V1alpha1Profile getProfile(
            final @PathVariable Long id
    ) {
        return V1alpha1Profile.toControllerDto(profileService.getProfile(id));
    }

    @PatchMapping(
            value = ""
    )
    public ResponseEntity<Void> updateProfile(
            final @PathVariable Long id,
            final @Valid @RequestBody V1alpha1Profile profile
    ) {
        profileService.updateProfile(id, profile.toDomainDto());
        return ResponseEntity.ok().build();
    }

    @GetMapping(
            value = ResourcePath.PROFILE_SUBRESOURCE_IMAGE
    )
    public V1alpha1ProfileImage getProfileImage(
            final @PathVariable Long id
    ) {
        Optional<ProfileImage> imageDataOpt = profileService.getProfileImage(id);
        return imageDataOpt.map(V1alpha1ProfileImage::toControllerDto).orElseGet(() -> V1alpha1ProfileImage.builder().build());
    }

    @PostMapping(
            value = ResourcePath.PROFILE_SUBRESOURCE_IMAGE
    )
    public V1alpha1ProfileImage saveProfileImage(
            final @PathVariable Long id,
            final @RequestParam MultipartFile file
    ) throws IOException {
        return V1alpha1ProfileImage
                .toControllerDto(profileService.saveProfileImage(id, ImageFile.from(file)));
    }

    @PutMapping(
            value = ResourcePath.PROFILE_SUBRESOURCE_IMAGE
    )
    public V1alpha1ProfileImage updateProfileImage(
            final @PathVariable Long id,
            final @RequestParam MultipartFile file
    ) throws IOException {
        return V1alpha1ProfileImage
                .toControllerDto(profileService.saveProfileImage(id, ImageFile.from(file)));
    }

    @DeleteMapping(
            value = ResourcePath.PROFILE_SUBRESOURCE_IMAGE
    )
    public ResponseEntity<Void> deleteProfileImage(
            final @PathVariable Long id
    ) throws IOException {
        profileService.deleteProfileImage(id);
        return ResponseEntity.ok().build();
    }

}
