package learniverse.learniversemain.repository.mongoDB;

import learniverse.learniversemain.entity.mongoDB.JoinsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JoinsMongoDBRepository extends MongoRepository<JoinsEntity, String> {
    List<JoinsEntity> findByMemberIdAndRoomId(Long memberId, Long roomId);
}
