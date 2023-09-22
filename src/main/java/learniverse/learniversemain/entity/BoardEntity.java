package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "board")
public class BoardEntity {
//    고유 id, 스터디룸 id, 생성자 id, 내용, 생성시간
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private long boardId;
    @Column(name = "room_id")
    private long roomId;
    @Column(name = "member_id")
    private long memberId;
    @Column(name = "content")
    private String content;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public void update(BoardEntity newBoard) {
        this.boardId = newBoard.getBoardId();
        this.roomId = newBoard.getRoomId();
        this.memberId = newBoard.getMemberId();
        this.content = newBoard.getContent();
        this.updatedDate = newBoard.getUpdatedDate();
    }
}
