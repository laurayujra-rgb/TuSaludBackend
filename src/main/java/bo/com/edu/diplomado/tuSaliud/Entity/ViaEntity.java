package bo.com.edu.diplomado.tuSaliud.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "via")
@Entity
@Table(name = "vias")
public class ViaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long viaId;
    private String viaName;
    private Integer viaStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "via", fetch = FetchType.LAZY)
    private List<MedicinesEntity> medicine;
    @PrePersist
    public void prePersist() {
        this.viaStatus = 1;
    }
}
