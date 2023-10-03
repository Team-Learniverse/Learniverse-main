package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.dto.mongoDB.DefaultRoomsDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DefaultMongoDBRepository extends MongoRepository<DefaultRoomsDTO, String> {
}
