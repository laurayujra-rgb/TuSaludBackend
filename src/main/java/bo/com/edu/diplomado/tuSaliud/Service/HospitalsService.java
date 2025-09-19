package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.HospitalsEntity;
import bo.com.edu.diplomado.tuSaliud.Repository.HospitalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HospitalsService {

    private final HospitalsRepository hospitalsRepository;

    // ✅ Inyección por constructor (nada de @Autowired en campos)
    public HospitalsService(HospitalsRepository hospitalsRepository) {
        this.hospitalsRepository = hospitalsRepository;
    }

    @Transactional(readOnly = true)
    public List<HospitalsEntity> getAllHospitals(){
        return hospitalsRepository.findAllByStatus(); // activos
    }

    @Transactional(readOnly = true)
    public Optional<HospitalsEntity> getHospitalById(Long id){
        return hospitalsRepository.findByIdAndByStatus(id, 1); // Integer, no long
    }

    public Optional<HospitalsEntity> createHospital(HospitalsEntity hospitalsEntity){
        if (hospitalsEntity.getHospitalStatus() == null) hospitalsEntity.setHospitalStatus(1);
        return Optional.of(hospitalsRepository.save(hospitalsEntity));
    }

    public Optional<HospitalsEntity> updateHospital(Long id, HospitalsEntity changes){
        HospitalsEntity hospital = hospitalsRepository.findByIdAndByStatus(id, 1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital no encontrado"));
        hospital.setHospitalName(changes.getHospitalName());
        // setea aquí más campos editables si los hubiera...
        return Optional.of(hospitalsRepository.save(hospital));
    }

    public Optional<HospitalsEntity> softDeleteHospital(Long id){
        HospitalsEntity hospital = hospitalsRepository.findByIdAndByStatus(id, 1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hospital no encontrado"));
        hospital.setHospitalStatus(0);
        return Optional.of(hospitalsRepository.save(hospital));
    }
}