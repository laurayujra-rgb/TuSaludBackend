package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.BedsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.BedsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BedsService {
    @Autowired
    public BedsRepository bedsRepository;

    public List<BedsEntity> getAllBeds(){
        return bedsRepository.findAll();
    }
    public List<BedsEntity> getAllBedsByStatus(){
        return bedsRepository.findAllByStatus();
    }
    public Optional<BedsEntity>getBedById(Long id){
        return Optional.of(bedsRepository.findByIdAndByStatus(id,1L));
    }
    public Optional<BedsEntity> createBed(BedsEntity bedsEntity){
        return Optional.of(bedsRepository.save(bedsEntity));
    }
    public Optional<BedsEntity> updateBed(Long id, BedsEntity bedsEntity){
        BedsEntity bed = bedsRepository.findByIdAndByStatus(id, 1L);
        bed.setBedName(bedsEntity.getBedName());
        return Optional.of(bedsRepository.save(bed));
    }
    public Optional<BedsEntity> deleteBed(Long id){
        Optional<BedsEntity> existingBed = bedsRepository.findById(id);
        if(existingBed.isEmpty()){
            return Optional.empty();
        }
        BedsEntity bed = existingBed.get();
        bed.setBedStatus(0);
        return Optional.of(bedsRepository.save(bed));
    }

}
