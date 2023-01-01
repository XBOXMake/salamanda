package moe.salamanda.salamanda.repositories.general;

import moe.salamanda.salamanda.models.general.AuthenticationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationCodeRepository extends JpaRepository<AuthenticationCode,Long> {
    @Query(value = "Select * from authcode where authcode= :code",nativeQuery = true)
    AuthenticationCode findByAuthCode(@Param("code") String authCode);

}
