package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.MembersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MembersMongoDBRepository extends MongoRepository<MembersEntity, String> {
}
