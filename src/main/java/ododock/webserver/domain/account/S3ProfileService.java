package ododock.webserver.domain.account;

import lombok.RequiredArgsConstructor;
import ododock.webserver.domain.S3StorageService;
import ododock.webserver.domain.StorageService;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceConflictException;
import ododock.webserver.web.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import java.util.Optional;

@Service
public class S3ProfileService {

    private final AccountRepository accountRepository;
    private final StorageService storageService;

    public S3ProfileService(AccountRepository accountRepository, S3Client s3Client) {
        this.accountRepository = accountRepository;
        this.storageService = new S3StorageService("oddk-profile", s3Client);
    }

    @Transactional(readOnly = true)
    public boolean isAvailableNickname(final String nickname) {
        return !accountRepository.existsByOwnProfile_Nickname(nickname);
    }

    @Transactional(readOnly = true)
    public Account getProfile(final Long accountId) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return foundAccount;
    }

    @Transactional(readOnly = true)
    public Optional<byte[]> getProfileImage(final Long accountId) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        if (foundAccount.getOwnProfile().getProfileImage() != null) {
            return Optional.ofNullable(storageService.getData(String.valueOf(accountId), foundAccount.getOwnProfile().getProfileImage().getImageSource()));
        }
        return Optional.empty();
    }

    @Transactional
    public void updateProfile(final Long accountId, final Profile request) {
        if (!isAvailableNickname(request.getNickname())) {
            throw new ResourceConflictException(Profile.class, request.getNickname());
        }
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        ownerAccount.getOwnProfile().updateNickname(request.getNickname());
        if (request.getProfileImage().getImageSource() != null && request.getProfileImage().getFileType() != null) {
            ownerAccount.getOwnProfile()
                    .updateProfileImage(
                            request.getProfileImage().getImageSource(),
                            request.getProfileImage().getFileType())
            ;
        }
    }

    @Transactional
    public void saveProfileImage(final Long accountId, final MultipartFile file) {
        Account ownerAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));

        storageService.saveData(String.valueOf(accountId), ownerAccount.getId(), file);

        ownerAccount.getOwnProfile().updateNickname(request.getNickname());
        if (request.getProfileImage().getImageSource() != null && request.getProfileImage().getFileType() != null) {
            ownerAccount.getOwnProfile()
                    .updateProfileImage(
                            request.getProfileImage().getImageSource(),
                            request.getProfileImage().getFileType())
            ;
        }
    }

}
