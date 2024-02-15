package ododock.webserver.repository;

import ododock.webserver.domain.account.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authority, Long> {
    public List<Authority> findByUserId(Long userId);
}
