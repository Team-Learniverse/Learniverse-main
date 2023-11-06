package learniverse.learniversemain.repository;

import learniverse.learniversemain.jwt.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Refresh, Long> {
    Optional<Refresh> findByToken(String token);

    Optional<Refresh> findByMemberId(Long memberId);

    //List<Refresh> findByMemberId(Long memberId);

}
