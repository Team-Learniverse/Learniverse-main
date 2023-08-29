package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.MoonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MoonRepository extends JpaRepository<MoonEntity, Long> {
    MoonEntity findOneByMemberIdAndMoonDate(Long memberId, LocalDate moonDate);

    List<MoonEntity> findByMemberIdAndMoonDateGreaterThan(long memberId, LocalDate localDate);
}
