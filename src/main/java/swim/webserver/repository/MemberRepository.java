package swim.webserver.repository;

import swim.webserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByUsername(String username);

    public Member findByEmail(String email);

}
