package ododock.webserver.repository;

import ododock.webserver.domain.profile.Category;
import ododock.webserver.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    List<Category> findByOwnerProfile(Profile profile);

    Optional<Category> findByOwnerProfileAndOrder(Profile ownerProfile, Integer order);

}
