package bo.com.edu.diplomado.tuSaliud.Auth.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.AccountsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    private final AccountsRepository accountsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountsEntity person = accountsRepository.findByAccountEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String role = "" + person.getPerson().getRole().getRoleName().toUpperCase();

        return org.springframework.security.core.userdetails.User.builder()
                .username(person.getAccountEmail())
                .password(person.getAccountPassword())
                .roles(role)
                .build();
    }



    // Método adicional para obtener la entidad completa
    public AccountsEntity getPersonByEmail(String email) {
        return accountsRepository.findByAccountEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
