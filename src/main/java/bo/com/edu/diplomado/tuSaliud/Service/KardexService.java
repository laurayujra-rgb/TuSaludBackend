package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KardexService {

    @Autowired
    private KardexRepository kardexRepository;

    public List<KardexEntity> getAllKardex(){
        return kardexRepository.findAll();
    }

    public List<KardexEntity> getAllKardexByStatus(){
        return kardexRepository.findAllByStatus();
    }

    public Optional<KardexEntity> getKardexById(Long id){
        return Optional.of(kardexRepository.findByIdAndByStatus(id,1L));
    }

    public Optional<KardexEntity> createKardex(KardexEntity kardexEntity){
        return Optional.of(kardexRepository.save(kardexEntity));
    }

    public Optional<KardexEntity> updateKardex(Long id, KardexEntity kardexEntity){
        Optional<KardexEntity> existingKardex = kardexRepository.findById(id);
        if(existingKardex.isEmpty()){
            return Optional.empty();
        }
        KardexEntity kardex = kardexRepository.findByIdAndByStatus(id, 1L);
        kardex.setKardexNumber(kardexEntity.getKardexNumber());
        kardex.setKardexDiagnosis(kardexEntity.getKardexDiagnosis());
        kardex.setKardexDate(kardexEntity.getKardexDate());
        kardex.setKardexHour(kardexEntity.getKardexHour());
        kardex.setDiets(kardexEntity.getDiets());
        return Optional.of(kardexRepository.save(kardex));
    }
    public Optional<KardexEntity> deleteKardex(Long id){
        Optional<KardexEntity> kardexEntity = kardexRepository.findById(id);
        if(kardexEntity.isEmpty()){
            return Optional.empty();
        }
        KardexEntity kardex = kardexEntity.get();
        kardex.setKardexStatus(0);
        return Optional.of(kardexRepository.save(kardex));

    }
}
