package bo.com.edu.diplomado.tuSaliud.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Accounts", uniqueConstraints = {
        @UniqueConstraint(name = "uk_accounts_email", columnNames = "accountEmail"),
        @UniqueConstraint(name = "uk_accounts_person", columnNames = "person_id")
})
public class AccountsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long accountId;

    private String accountEmail;
    private String accountPassword;
    private Integer accountStatus;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_accounts_person"))
    @ToString.Exclude
    private PersonsEntity person;

    @PrePersist
    public void prePersist() {
        if (this.accountStatus == null) this.accountStatus = 1;
    }

}
