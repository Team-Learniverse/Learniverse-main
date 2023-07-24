package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.HashtagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
}
