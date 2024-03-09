package ododock.webserver.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ododock.webserver.request.ProfileImageUpdate;
import ododock.webserver.request.ProfileUpdate;
import ododock.webserver.response.ProfileDetailsResponse;
import ododock.webserver.response.ValidateResponse;
import ododock.webserver.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/api/v1/profiles/{profileId}")
    public ProfileDetailsResponse getProfile(
            @PathVariable final Long profileId
    ) {
        return profileService.getProfile(profileId);
    }

    @GetMapping("/api/v1/profiles")
    public ValidateResponse validateNickname(
            @RequestParam("nickname") final  String nickname
    ) {
        return ValidateResponse.of(profileService.validateNickname(nickname));
    }

    @PatchMapping("/api/v1/profiles/{profileId}")
    public ResponseEntity<Void> updateProfile(
            final @PathVariable Long profileId,
            final @Valid @RequestBody ProfileUpdate request
            ) {
        profileService.updateProfile(profileId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/v1/profiles/{profileId}/profileImage")
    public ResponseEntity<Void> updateProfileImage(
            final @PathVariable Long profileId,
            final @Valid @RequestBody ProfileImageUpdate request
    ) {
        profileService.updateProfileImage(profileId, request);
        return ResponseEntity.ok().build();
    }

}
