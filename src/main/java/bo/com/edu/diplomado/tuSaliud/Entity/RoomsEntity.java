package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "rooms")
@Entity
@Table(name = "Rooms")
public class RoomsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomId;
    private String roomName;
    private Integer roomStatus;
    @PrePersist
    public void prePersist() {
        this.roomStatus = 1;
    }
}
