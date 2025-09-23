package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.VitalSignsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.VitalSignsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VitalSignsService {
    @Autowired
    public VitalSignsRepository vitalSignsRepository;

    public List<VitalSignsEntity> getAllVitalSigns(){
        return vitalSignsRepository.findAll();
    }
    public List<VitalSignsEntity> getAllVitalSignsByStatus(){
        return vitalSignsRepository.findAllByStatus();
    }
    public Optional<VitalSignsEntity>getVitalSignById(Long id){
        return Optional.of(vitalSignsRepository.findByIdAndByStatus(id,1L));
    }
    public Optional<VitalSignsEntity> createVitalSigns(VitalSignsEntity vitalSignsEntity){
        return Optional.of(vitalSignsRepository.save(vitalSignsEntity));
    }
    public Optional<VitalSignsEntity> updateVitalSigns(Long id, VitalSignsEntity vitalSignsEntity){
        VitalSignsEntity vitalSigns = vitalSignsRepository.findByIdAndByStatus(id, 1L);
        vitalSigns.setVitalSignsDate(vitalSignsEntity.getVitalSignsDate());
        vitalSigns.setVitalSignsHour(vitalSignsEntity.getVitalSignsHour());
        vitalSigns.setVitalSignsTemperature(vitalSignsEntity.getVitalSignsTemperature());
        vitalSigns.setVitalSignsHeartRate(vitalSignsEntity.getVitalSignsHeartRate());
        vitalSigns.setVitalSignsHeartRate(vitalSignsEntity.getVitalSignsHeartRate());
        vitalSigns.setVitalSignsRespiratoryRate(vitalSignsEntity.getVitalSignsRespiratoryRate());
        vitalSigns.setVitalSignsBloodPressure(vitalSignsEntity.getVitalSignsBloodPressure());
        vitalSigns.setVitalSignsOxygenSaturation(vitalSignsEntity.getVitalSignsOxygenSaturation());
        return Optional.of(vitalSignsRepository.save(vitalSigns));
    }
    public Optional<VitalSignsEntity> deleteVitalSigns(Long id){
        Optional<VitalSignsEntity> existingSign = vitalSignsRepository.findById(id);
        if(existingSign.isEmpty()){
            return Optional.empty();
        }
        VitalSignsEntity sign = existingSign.get();
        sign.setVitalSignsStatus(0);
        return Optional.of(vitalSignsRepository.save(sign));
    }
}
