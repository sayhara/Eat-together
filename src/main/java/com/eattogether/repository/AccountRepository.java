package com.eattogether.repository;

import com.eattogether.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Account findByNickname(String nickname);
}
