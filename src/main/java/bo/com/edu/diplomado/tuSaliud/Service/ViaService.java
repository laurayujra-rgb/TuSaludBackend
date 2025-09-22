package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.ViaEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.ViaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ViaService {

    @Autowired
    private ViaRepository viaRepository;

    public List<ViaEntity> getAllVia(){
        return viaRepository.findAll();
    }

    public List<ViaEntity> getAllViaByStatus(){
        return viaRepository.findAllByStatus();
    }

    public Optional<ViaEntity> getViaById(Long id){
        return Optional.of(viaRepository.findByIdAndByStatus(id,1L));
    }

    public Optional<ViaEntity> createVia(ViaEntity viaEntity){
        return Optional.of(viaRepository.save(viaEntity));
    }

    public Optional<ViaEntity> updateVia(Long id, ViaEntity viaEntity){
        Optional<ViaEntity> existingVia = viaRepository.findById(id);
        if(existingVia.isEmpty()){
            return Optional.empty();
        }
        ViaEntity via = viaRepository.findByIdAndByStatus(id, 1L);
        via.setViaName(viaEntity.getViaName());
        return Optional.of(viaRepository.save(via));
    }
    public Optional<ViaEntity> deleteVia(Long id){
        Optional<ViaEntity> viaEntity = viaRepository.findById(id);
        if(viaEntity.isEmpty()){
            return Optional.empty();
        }
        ViaEntity via = viaEntity.get();
        via.setViaStatus(0);
        return Optional.of(viaRepository.save(via));
    }
}
