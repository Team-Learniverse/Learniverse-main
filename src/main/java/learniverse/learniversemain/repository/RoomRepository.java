package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    boolean existsByRoomId(long roomId);
    Page<RoomEntity> findByRoomNameContainingOrRoomIntroContaining(String str, String str2, Pageable pageable);
    //List<RoomEntity> findByRoomNameContainingOrRoomIntroContaining(String str, String str2);
    Page<RoomEntity> findAll(Pageable pageable);

    Page<RoomEntity> findByRoomCategoryAndRoomNameContainingOrRoomIntroContaining(int category, String str, String str1, Pageable pageable);

    Page<RoomEntity> findByRoomCategoryAndRoomNameContainingOrRoomCategoryAndRoomIntroContaining(int category, String str, int category1, String str1, Pageable pageable);

    @Query("SELECT DISTINCT r FROM rooms r LEFT JOIN r.hashtags h WHERE h.hashtag LIKE %:hashtag%")
    Page<RoomEntity> findByHashtagsHashtagContaining(@Param("hashtag") String hashtag, Pageable pageable);
}
