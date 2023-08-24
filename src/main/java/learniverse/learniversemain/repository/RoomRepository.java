package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    boolean existsByRoomId(long roomId);
}
