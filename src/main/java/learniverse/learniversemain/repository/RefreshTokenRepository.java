package learniverse.learniversemain.repository;

import learniverse.learniversemain.jwt.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<Refresh, Long> {
    Refresh findByToken(String token);

}
