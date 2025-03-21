package ododock.webserver.domain.account;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import ododock.webserver.web.ResourceNotFoundException;
import ododock.webserver.web.v1alpha1.dto.account.AccountSocialConnectDetails;
import ododock.webserver.web.v1alpha1.dto.response.AccountDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static ododock.webserver.domain.account.QAccount.account;
import static ododock.webserver.domain.account.QSocialAccount.socialAccount;

@Service
@Transactional(readOnly = true)
public class AccountQueryService {

    private final JPAQueryFactory queryFactory;

    public AccountQueryService(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Account getAccountDetails(final Long accountId) {
        Account foundAccount = Optional.ofNullable(queryFactory.selectFrom(account)
                        .leftJoin(socialAccount).on(socialAccount.daoAccount.eq(account))
                        .where(account.id.eq(accountId))
                        .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));

        return foundAccount;
    }

    public AccountSocialConnectDetails getAccountSocialConnectDetails(final Long accountId) {
        Account foundAccount = Optional.ofNullable(queryFactory.selectFrom(account)
                        .leftJoin(socialAccount).fetchJoin()
                        .where(account.id.eq(accountId))
                        .fetchOne())
                .orElseThrow(() -> new ResourceNotFoundException(Account.class, accountId));
        return AccountSocialConnectDetails.of(foundAccount);
    }

    public Optional<Account> getAccountBySocialProviderId(final String providerId) {
        return Optional.ofNullable(queryFactory.selectFrom(account)
                .leftJoin(socialAccount).fetchJoin()
                .where(socialAccount.providerId.eq(providerId))
                .fetchOne());
    }

}
