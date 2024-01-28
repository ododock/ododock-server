package swim.webserver.repository;

import swim.webserver.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authority, Long> {
    public List<Authority> findByUsername(String username);
}
