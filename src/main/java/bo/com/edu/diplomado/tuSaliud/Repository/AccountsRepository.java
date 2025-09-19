package bo.com.edu.diplomado.tuSaliud.Repository;

import bo.com.edu.diplomado.tuSaliud.Entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<AccountsEntity, Long> {
    boolean existsByAccountEmail(String email);
    java.util.Optional<AccountsEntity> findByAccountEmail(String email); // 👈
    boolean existsByPerson_PersonId(Long personId);
}