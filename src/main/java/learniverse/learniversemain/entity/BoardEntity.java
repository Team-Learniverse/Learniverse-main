package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.BoardDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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
    @Column(name = "title")
    private String title;
    //varchar(500)
    @Column(name = "content", length = 500)
    private String content;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public void update(BoardEntity newBoard) {
        LocalDateTime now = LocalDateTime.now();

        this.boardId = newBoard.getBoardId();
        this.roomId = newBoard.getRoomId();
        this.memberId = newBoard.getMemberId();
        this.title = newBoard.getTitle();
        this.content = newBoard.getContent();
        this.updatedDate = now;
    }
    public BoardEntity(BoardDTO boardDTO){
        LocalDateTime now = LocalDateTime.now();

        this.roomId = boardDTO.getRoomId();
        this.memberId = boardDTO.getMemberId();
        this.title = boardDTO.getTitle();
        this.content = boardDTO.getContent();
        this.createdDate = now;
        this.updatedDate = now;
    }

}
