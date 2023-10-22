package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<IssueEntity, Long> {
    //public List<IssueEntity> findByRoomId(Long roomId);

    List<IssueEntity> findByRoomIdOrderByCreatedDateDesc(Long roomId);

}
