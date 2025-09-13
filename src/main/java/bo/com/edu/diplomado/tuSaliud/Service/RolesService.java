package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.RolesEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    public List<RolesEntity> getAllRoles(){
        return rolesRepository.findAll();
    }
    public List<RolesEntity> getAllRolesByStatus(){
        return rolesRepository.findAllByStatus();
    }
    public Optional<RolesEntity> getRolesById(Long id){
        return Optional.of(rolesRepository.findByIdAndByStatus(id,1L));
    }

    public Optional<RolesEntity> createRoles(RolesEntity rolesEntity){
        return Optional.of(rolesRepository.save(rolesEntity));
    }

    public Optional<RolesEntity> updateRoles(Long id, RolesEntity rolesEntity){
       RolesEntity role = rolesRepository.findByIdAndByStatus(id, 1L);
       role.setRoleName(rolesEntity.getRoleName());
       return Optional.of(rolesRepository.save(role));
    }

    public Optional<RolesEntity> deleteRoles(Long id){
        RolesEntity role = rolesRepository.findByIdAndByStatus(id, 1L);
        role.setRoleStatus(0);
        return Optional.of(rolesRepository.save(role));
    }
}
