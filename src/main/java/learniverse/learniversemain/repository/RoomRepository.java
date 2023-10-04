package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    boolean existsByRoomId(long roomId);
    //Page<RoomEntity> findByRoomNameContainingOrRoomIntroContaining(String str, String str2, Pageable pageable);
    List<RoomEntity> findByRoomNameContainingOrRoomIntroContaining(String str, String str2);
}
