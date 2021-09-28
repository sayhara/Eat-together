package com.jpaproject.repository;

import com.jpaproject.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true) // 성능향상
public interface AccountRepository extends JpaRepository<Account,Long>, QuerydslPredicateExecutor<Account> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Account findByNickname(String nickname);

}
