package ododock.webserver.repository;

import ododock.webserver.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByNickname(String nickname);

    boolean existsByNickname(String nickname);

}
