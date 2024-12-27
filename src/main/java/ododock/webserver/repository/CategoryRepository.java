package ododock.webserver.repository;

import ododock.webserver.domain.account.Account;
import ododock.webserver.domain.article.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

    List<Category> findByOwnerAccount(Account ownerAccount);

    Optional<Category> findByOwnerAccountAndPosition(Account ownerAccount, Integer position);

}
