package ododock.webserver.repository;

import ododock.webserver.domain.account.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

//    List<Role> findByUserId(Long userId);

}
