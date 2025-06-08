package ododock.webserver.domain.profile;

import ododock.webserver.domain.StorageService;
import ododock.webserver.domain.account.Account;
import ododock.webserver.repository.jpa.AccountRepository;
import ododock.webserver.web.ResourceConflictException;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.v1alpha1.dto.account.ImageFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

public class SimpleProfileService implements ProfileService {

    private final StorageService storageService;
    private final AccountRepository accountRepository;

    public SimpleProfileService(StorageService storageService, AccountRepository accountRepository) {
        this.storageService = storageService;
        this.accountRepository = accountRepository;
    }

    @Override
    public Profile getProfile(Long accountId) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return foundAccount.getOwnProfile();
    }

    @Override
    @Transactional
    public Profile updateProfile(Long accountId, Profile request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));

        Optional<Account> existingAccountOpt = accountRepository.findByOwnProfile_Nickname(request.getNickname());
        if (existingAccountOpt.isPresent()) {
            if (!existingAccountOpt.get().equals(account)) {
                throw new ResourceConflictException(Profile.class, request.getNickname());
            }
        }

        account.getOwnProfile().updateNickname(request.getNickname());
        account.getOwnProfile().updateFullname(request.getFullname());
        return account.getOwnProfile();
    }

    @Override
    public Optional<ProfileImage> getProfileImage(Long accountId) {
        Account foundAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return Optional.ofNullable(foundAccount.getOwnProfile().getProfileImage());
    }

    @Override
    @Transactional
    public ProfileImage saveProfileImage(Long accountId, ImageFile imageFile) throws IOException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        String fileName = buildFileName(account, imageFile);
        String filePath = storageService.saveData(fileName, imageFile.data());
        account.getOwnProfile().updateProfileImage(filePath, imageFile.extension());
        return account.getOwnProfile().getProfileImage();
    }

    @Override
    public ProfileImage updateProfileImage(Long accountId, ImageFile imageFile) {
        return null;
    }

    @Override
    public void removeProfileImage(Long accountId) throws IOException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        String imagePath = account.getOwnProfile().getProfileImage().getSourcePath();
        if (imagePath != null && !imagePath.isBlank()) {
            storageService.deleteData(account.getOwnProfile().getProfileImage().getSourcePath());
        }
        account.getOwnProfile().deleteProfileImage();

    }

    private String buildFileName(Account account, ImageFile imageFile) {
        return String.format("%s-%s.%s", account.getId(), account.getCreatedAt().getEpochSecond(), imageFile.getFileExtension());
    }

}
