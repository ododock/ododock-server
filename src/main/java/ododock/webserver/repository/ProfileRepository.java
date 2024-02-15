package ododock.webserver.repository;

import ododock.webserver.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    public Profile findByNickname(String nickname);

}
