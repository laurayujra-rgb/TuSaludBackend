package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table (name = "Roles")
public class RolesEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long roleId;
    private String roleName;
}
