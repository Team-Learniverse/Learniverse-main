package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.RoomsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoomsMongoDBRepository extends MongoRepository<RoomsEntity, String> {
    List<RoomsEntity> findByRoomId(Long roomId);
}
