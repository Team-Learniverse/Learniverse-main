package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.CoreTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoreTimeRepository extends JpaRepository<CoreTimeEntity, Long> {
}
