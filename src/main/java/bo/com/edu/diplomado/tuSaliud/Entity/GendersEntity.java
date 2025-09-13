package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Genders")
public class GendersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long genderId;

    private String genderName;


}
