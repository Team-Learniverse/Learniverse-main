package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.GitcodeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GitCodeMongoDBRepository extends MongoRepository<GitcodeEntity, String> {
    GitcodeEntity findByIssueId(long issueId);
}
