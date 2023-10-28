package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.GitcodeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GitCodeMongoDBRepository extends MongoRepository<GitcodeEntity, String> {
    GitcodeEntity findByIssueId(Long issueId);

    List<GitcodeEntity> findByRoomIdOrderByCreatedDateDesc(Long roomId);
}
