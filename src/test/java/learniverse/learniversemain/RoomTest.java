package learniverse.learniversemain;

import learniverse.learniversemain.controller.RoomController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomTest {
    @Autowired
    private RoomController roomController;

    @Test
    void getMembers(){

    }
}
