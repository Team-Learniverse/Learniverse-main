package learniverse.learniversemain.dto;

import learniverse.learniversemain.entity.MoonEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResMoonDTO {
    private LocalDate moonDate;
    private Integer moonScore;

    public ResMoonDTO(MoonEntity moonEntity){
        this.moonDate = moonEntity.getMoonDate();
        this.moonScore = moonEntity.getMoonScore();
    }
}
