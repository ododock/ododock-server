package ododock.webserver.repository;

import ododock.webserver.domain.profile.Category;
import ododock.webserver.domain.profile.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    List<Category> findByOwnerProfile(Profile profile);

}
