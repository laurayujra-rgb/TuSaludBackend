package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "beds")
@Entity
@Table(name = "Rooms")
public class RoomsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomId;

    private String roomName;
    private Integer roomStatus;     // 1 = activo, 0 = eliminado
    private Boolean roomOccupied;   // true = ocupada, false = libre

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<BedsEntity> beds;

    @PrePersist
    public void prePersist() {
        this.roomStatus = 1;
        this.roomOccupied = false;
    }
}
