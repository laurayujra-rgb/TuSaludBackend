package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.MedicinesEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.MedicinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicinesService {
    @Autowired
    public MedicinesRepository medicinesRepository;
    
    public List<MedicinesEntity> getAllMedicines(){
        return medicinesRepository.findAll();
    }
    public List<MedicinesEntity> getAllMedicinesByStatus(){
        return medicinesRepository.findAllByStatus();
    }
    public Optional<MedicinesEntity> getMedicineById(Long id){
        return Optional.of(medicinesRepository.findByIdAndByStatus(id,1L));
    }
    public Optional<MedicinesEntity>createMedicine(MedicinesEntity medicinesEntity){
        return Optional.of(medicinesRepository.save(medicinesEntity));
    }
    public Optional<MedicinesEntity> updateMedicnes(Long id, MedicinesEntity medicinesEntity){
        MedicinesEntity medicine = medicinesRepository.findByIdAndByStatus(id, 1L);
        medicine.setMedicineName(medicinesEntity.getMedicineName());
        medicine.setMedicineLaboratory(medicinesEntity.getMedicineLaboratory());

        return Optional.of(medicinesRepository.save(medicine));
    }
    public Optional<MedicinesEntity>deleteMedicine(Long id){
        Optional<MedicinesEntity> existingMedicine = medicinesRepository.findById(id);
        if(existingMedicine.isEmpty()){
            return Optional.empty();
        }
        MedicinesEntity medicine = existingMedicine.get();
        medicine.setMedicineStatus(0);
        return Optional.of(medicinesRepository.save(medicine));
    }
}
