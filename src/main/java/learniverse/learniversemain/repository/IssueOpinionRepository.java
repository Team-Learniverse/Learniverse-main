package learniverse.learniversemain.repository;

import learniverse.learniversemain.entity.IssueOpinionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueOpinionRepository extends JpaRepository<IssueOpinionEntity, Long> {
    public List<IssueOpinionEntity> findByIssueId(Long issueId);


}
