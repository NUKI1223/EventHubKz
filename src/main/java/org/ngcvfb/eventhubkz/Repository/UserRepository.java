package org.ngcvfb.eventhubkz.Repository;


import org.ngcvfb.eventhubkz.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByVerificationCode(String verificationCode);

}
