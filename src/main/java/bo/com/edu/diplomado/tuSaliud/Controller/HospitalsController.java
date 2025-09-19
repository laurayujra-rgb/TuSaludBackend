package bo.com.edu.diplomado.tuSaliud.Controller;
import bo.com.edu.diplomado.tuSaliud.Entity.HospitalsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.HospitalsService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hospital")
public class HospitalsController extends ApiController {

    private final HospitalsService hospitalsService;

    // ✅ Inyección por constructor (recomendado)
    public HospitalsController(HospitalsService hospitalsService) {
        this.hospitalsService = hospitalsService;
    }

    @GetMapping("/all")
    public ApiResponse<List<HospitalsEntity>> getAllHopspital(){
        ApiResponse<List<HospitalsEntity>> response = new ApiResponse<>();
        List<HospitalsEntity> hospital = hospitalsService.getAllHospitals();
        response.setData(hospital);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<HospitalsEntity> getHospitalById(@PathVariable Long id){
        ApiResponse<HospitalsEntity> response = new ApiResponse<>();
        try{
            Optional<HospitalsEntity> hospitals = hospitalsService.getHospitalById(id);
            if(hospitals.isPresent()){
                response.setData(hospitals.get());
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
            }else{
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @PostMapping("/create")
    public ApiResponse<Optional<HospitalsEntity>> createHospital(@RequestBody HospitalsEntity hospitalsEntity){
        ApiResponse<Optional<HospitalsEntity>> response = new ApiResponse<>();
        try{
            Optional<HospitalsEntity> hospital = hospitalsService.createHospital(hospitalsEntity);
            response.setData(hospital);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());
        }catch (ConstraintViolationException e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Optional<HospitalsEntity>> updateHospital(@PathVariable Long id, @RequestBody HospitalsEntity hospitalsEntity){
        ApiResponse<Optional<HospitalsEntity>> response = new ApiResponse<>();
        try{
            Optional<HospitalsEntity> hospital = hospitalsService.updateHospital(id, hospitalsEntity);
            if(hospital.isPresent()){
                response.setData(hospital);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
            }else{
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
        }catch (ConstraintViolationException e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<HospitalsEntity>> deleteHospital(@PathVariable Long id){
        ApiResponse<Optional<HospitalsEntity>> response = new ApiResponse<>();
        try{
            Optional<HospitalsEntity> hospital = hospitalsService.softDeleteHospital(id); // 👈 renombrado
            if(hospital.isPresent()){
                response.setData(hospital);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
            }else{
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
            }
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}