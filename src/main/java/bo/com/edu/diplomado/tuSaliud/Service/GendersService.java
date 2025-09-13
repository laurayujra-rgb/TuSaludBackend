package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.GendersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GendersService {
    @Autowired
    private GendersRepository gendersRepository;

    public List<GendersEntity> getAllGenders(){
        return gendersRepository.findAll();
    }
    public List<GendersEntity> getAllGendersByStatus(){
        return gendersRepository.findAllByStatus();
    }
    public Optional<GendersEntity> getGenderById(Long id){
        return Optional.of(gendersRepository.findByIdAndByStatus(id,1L));
    }
    public Optional<GendersEntity> createGender(GendersEntity gendersEntity){
        return Optional.of(gendersRepository.save(gendersEntity));
    }
    
    public Optional<GendersEntity> updateGender(Long id, GendersEntity gendersEntity){
       GendersEntity gender = gendersRepository.findByIdAndByStatus(id, 1L);
       gender.setGenderName(gendersEntity.getGenderName());
       return Optional.of(gendersRepository.save(gender));
    }

    public Optional<GendersEntity> deleteGender(Long id){
        GendersEntity gender = gendersRepository.findByIdAndByStatus(id, 1L);
        gender.setGenderStatus(0);
        return Optional.of(gendersRepository.save(gender));
    }
}
