package bo.com.edu.diplomado.tuSaliud.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Beds")
public class BedsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bedId;

    private String bedName;
    private Integer bedStatus;      // 1 = activo, 0 = eliminado
    private Boolean bedOccupied;    // true = ocupada, false = libre

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomsEntity room;

    @PrePersist
    public void prePersist() {
        this.bedStatus = 1;
        this.bedOccupied = false;
    }
}
