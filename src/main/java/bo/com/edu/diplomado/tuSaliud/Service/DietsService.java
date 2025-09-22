package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.DietsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.DietsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DietsService {

    @Autowired
    private DietsRepository dietsRepository;

    public List<DietsEntity> getAllDiets(){
        return dietsRepository.findAll();
    }

    public List<DietsEntity> getAllDietsByStatus(){
        return dietsRepository.findAllByStatus();
    }
    public Optional<DietsEntity>getDietById(Long id){
        return Optional.of(dietsRepository.findByIdAndByStatus(id,1L));
    }
    public Optional<DietsEntity> createDiet(DietsEntity dietsEntity){
        return Optional.of(dietsRepository.save(dietsEntity));
    }
    public Optional<DietsEntity> updateDiet(Long id, DietsEntity dietsEntity){
       DietsEntity diet = dietsRepository.findByIdAndByStatus(id, 1L);
       diet.setDietName(dietsEntity.getDietName());
       return Optional.of(dietsRepository.save(diet));
    }
    public Optional<DietsEntity> deleteDiet(Long id){
        DietsEntity diet = dietsRepository.findByIdAndByStatus(id, 1L);
        diet.setDietStatus(0);
        return Optional.of(dietsRepository.save(diet));
    }
}
