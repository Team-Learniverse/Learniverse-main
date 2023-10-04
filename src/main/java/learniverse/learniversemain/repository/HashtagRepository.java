package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository<HashtagEntity, Long> {
    boolean existsByRoomId(long roomId);
    List<HashtagEntity> findByRoomId(Long roomId);

    //List<HashtagEntity> findByHashtagContaining(String str);
    Page<HashtagEntity> findByHashtagContaining(String str, Pageable pageable);
}
