package com.midas.app.repositories;

import com.midas.app.models.Account;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

  Account findByEmail(String email);

  Account findById(UUID id);
}
