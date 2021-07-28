package com.eattogether.repository;

import com.eattogether.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true) // 성능향상
public interface AccountRepository extends JpaRepository<Account,Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Account findByNickname(String nickname);

}
