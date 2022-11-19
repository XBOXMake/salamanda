package moe.salamanda.salamanda.repositories;

import moe.salamanda.salamanda.models.AuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthenticationCodeRepository extends JpaRepository<AuthenticationCode,Long> {
    @Query(value = "Select * from authcode where authcode= :code",nativeQuery = true)
    AuthenticationCode findByAuthCode(@Param("code") String authCode);
}
