package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    public List<BoardEntity> findByRoomId(Long roomId);
}
