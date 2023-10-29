package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.LangEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LangMongoDBRepository extends MongoRepository<LangEntity, String> {
}
