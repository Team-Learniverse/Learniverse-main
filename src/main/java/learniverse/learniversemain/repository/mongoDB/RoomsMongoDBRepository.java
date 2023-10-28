package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.RoomsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomsMongoDBRepository extends MongoRepository<RoomsEntity, String> {
}
