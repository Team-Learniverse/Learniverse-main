package learniverse.learniversemain;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.entity.RoomSettingEntity;
import learniverse.learniversemain.repository.RoomSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
@Transactional
@RequiredArgsConstructor
public class LearniverseDataInitializer implements CommandLineRunner {
    private final RoomSettingRepository roomSettingRepository;

    @Override
    public void run(String... args) throws Exception {
        // 테이블이 존재하지 않으면 테이블 생성 및 초기 데이터 삽입
        if (tableEmpty("room_settings")) {
            insertInitialData();
        }
    }

    private boolean tableEmpty(String tableName) {
        long count = roomSettingRepository.count();
        return count == 0;
    }


    @Transactional
    private void insertInitialData() throws IOException {
        // 초기 데이터 삽입 쿼리 실행 (예: jdbcTemplate.update 등)
        File file = new File("src/main/resources/room_settings.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        String type = "";
        while ((line = bufferedReader.readLine()) != null) {
            if ("type".equals(line)) {
                type = bufferedReader.readLine();
            } else {
                //entityManager
                //        .createNativeQuery("INSERT INTO room_settings" +
                //                " (name, type) VALUES ('" + line + "','" + type + "')").executeUpdate();
                RoomSettingEntity roomSettingEntity = new RoomSettingEntity(line, type);
                roomSettingRepository.save(roomSettingEntity);
            }
        }
    }
}
