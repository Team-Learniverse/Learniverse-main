package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.HistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryMongoDBRepository extends MongoRepository<HistoryEntity, String> {
}
