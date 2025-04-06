package ododock.webserver.web.v1alpha1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.account.S3ProfileService;
import ododock.webserver.web.ResourcePath;
import ododock.webserver.web.v1alpha1.dto.account.V1alpha1Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(ResourcePath.API + ResourcePath.API_VERSION + ResourcePath.ACCOUNTS)
@RequiredArgsConstructor
public class V1alpha1ProfileController {

    private final S3ProfileService profileService;

    @GetMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE
    )
    public V1alpha1Profile getProfile(
            final @PathVariable Long id
    ) {
        return V1alpha1Profile.toControllerDto(profileService.getProfile(id));
    }

    @GetMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE + "/" + ResourcePath.PROFILE_SUBRESOURCE_IMAGE
    )
    public ResponseEntity<byte[]> getProfileImage(
            final @PathVariable Long id
    ) {
        Optional<byte[]> imageDataOpt = profileService.getProfileImage(id);
        if (imageDataOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(imageDataOpt.get().length);
        return new ResponseEntity<>(imageDataOpt.get(), headers, HttpStatus.OK);
    }

    @PostMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE + "/" + ResourcePath.PROFILE_SUBRESOURCE_IMAGE
    )
    public ResponseEntity<Void> getProfileImage(
            final @PathVariable Long id,
            final @RequestParam MultipartFile file
            ) {
//        Optional<byte[]> imageDataOpt = profileService.getProfileImage(id);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_JPEG);
//        headers.setContentLength(imageDataOpt.get().length);
//        return new ResponseEntity<>(imageDataOpt.get(), headers, HttpStatus.OK);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(
            value = "/{" + ResourcePath.PATH_VAR_ID + "}" + ResourcePath.ACCOUNTS_SUBRESOURCE_PROFILE
    )
    public ResponseEntity<Void> updateProfile(
            final @PathVariable Long id,
            final @Valid @RequestBody V1alpha1Profile profile
    ) {
//        profileService.updateProfile(id, profile.toDomainDto());
        return ResponseEntity.ok().build();
    }

}
