package learniverse.learniversemain;

import learniverse.learniversemain.dto.RoomCardDTO;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageTest {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void getRooms() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "roomId"));
        Page<RoomEntity> roomEntities = roomRepository.findAll(pageable);
        for(RoomEntity roomEntity : roomEntities){
            System.out.println(roomEntity.getRoomId());
        }

        pageable = PageRequest.of(1, 10, Sort.by(Sort.Direction.DESC, "roomId"));
        Page<RoomEntity> roomEntities2 = roomRepository.findAll(pageable);
        for(RoomEntity roomEntity : roomEntities2){
            System.out.println(roomEntity.getRoomId());
        }
    }
}
