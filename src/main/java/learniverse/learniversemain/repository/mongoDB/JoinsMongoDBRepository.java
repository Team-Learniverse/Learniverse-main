package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.JoinsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JoinsMongoDBRepository extends MongoRepository<JoinsEntity, String> {
}
