package bo.com.edu.diplomado.tuSaliud.Controller;


import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.VitalSignsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.VitalSignsDto;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.KardexService;
import bo.com.edu.diplomado.tuSaliud.Service.VitalSignsService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vitalSigns")
public class VitalSignsController extends ApiController{

    @Autowired
    public VitalSignsService vitalSignsService;
    @Autowired
    public KardexService kardexService;

    @GetMapping("/all")
    public ApiResponse<List<VitalSignsDto>> getAllVitalSigns() {
        ApiResponse<List<VitalSignsDto>> response = new ApiResponse<>();
        List<VitalSignsDto> vitalSigns = vitalSignsService.toDtoList(vitalSignsService.getAllVitalSigns());
        response.setData(vitalSigns);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping
    public ApiResponse<List<VitalSignsEntity>> getAllVitalSignsByStatus(){
        ApiResponse<List<VitalSignsEntity>> response = new ApiResponse<>();
        List<VitalSignsEntity> vitalSigns = vitalSignsService.getAllVitalSignsByStatus();
        response.setData(vitalSigns);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<VitalSignsEntity> getVitalSignById(@PathVariable Long id){
        ApiResponse<VitalSignsEntity> response = new ApiResponse<>();
        try {
            Optional<VitalSignsEntity> optionalVitalSigns = vitalSignsService.getVitalSignById(id);
            if(optionalVitalSigns.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("el signo vital no fue encontrado");
                return logApiResponse(response);
            }
            VitalSignsEntity vitalSignsResponse = new VitalSignsEntity();
            vitalSignsResponse.setVitalSignsId(optionalVitalSigns.get().getVitalSignsId());
            vitalSignsResponse.setVitalSignsDate(optionalVitalSigns.get().getVitalSignsDate());
            vitalSignsResponse.setVitalSignsHour(optionalVitalSigns.get().getVitalSignsHour());
            vitalSignsResponse.setVitalSignsTemperature(optionalVitalSigns.get().getVitalSignsTemperature());
            vitalSignsResponse.setVitalSignsHeartRate(optionalVitalSigns.get().getVitalSignsHeartRate());
            vitalSignsResponse.setVitalSignsRespiratoryRate(optionalVitalSigns.get().getVitalSignsRespiratoryRate());
            vitalSignsResponse.setVitalSignsBloodPressure(optionalVitalSigns.get().getVitalSignsBloodPressure());
            vitalSignsResponse.setVitalSignsOxygenSaturation(optionalVitalSigns.get().getVitalSignsOxygenSaturation());
            vitalSignsResponse.setVitalSignsStatus(optionalVitalSigns.get().getVitalSignsStatus());
            vitalSignsResponse.setKardex(optionalVitalSigns.get().getKardex());
            response.setData(vitalSignsResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    @PostMapping("/create")

    public ApiResponse<VitalSignsDto> createVitalSigns(@RequestBody VitalSignsEntity vitalSignsEntity) {
        ApiResponse<VitalSignsDto> response = new ApiResponse<>();
        try {
            Optional<KardexEntity> kardex = kardexService.getKardexById(vitalSignsEntity.getKardex().getKardexId());
            if (kardex.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("El kardex no fue encontrado");
                return logApiResponse(response);
            }

            VitalSignsEntity vitalSigns = new VitalSignsEntity();
            vitalSigns.setVitalSignsDate(vitalSignsEntity.getVitalSignsDate());
            vitalSigns.setVitalSignsHour(vitalSignsEntity.getVitalSignsHour());
            vitalSigns.setVitalSignsTemperature(vitalSignsEntity.getVitalSignsTemperature());
            vitalSigns.setVitalSignsHeartRate(vitalSignsEntity.getVitalSignsHeartRate());
            vitalSigns.setVitalSignsRespiratoryRate(vitalSignsEntity.getVitalSignsRespiratoryRate());
            vitalSigns.setVitalSignsBloodPressure(vitalSignsEntity.getVitalSignsBloodPressure());
            vitalSigns.setVitalSignsOxygenSaturation(vitalSignsEntity.getVitalSignsOxygenSaturation());
            vitalSigns.setVitalSignsStatus(vitalSignsEntity.getVitalSignsStatus());
            vitalSigns.setKardex(kardex.get());

            Optional<VitalSignsEntity> createdVitalSigns = vitalSignsService.createVitalSigns(vitalSigns);

            if (createdVitalSigns.isPresent()) {
                response.setData(vitalSignsService.toDto(createdVitalSigns.get())); // 👈 convertir a DTO
                response.setStatus(HttpStatus.CREATED.value());
                response.setMessage(HttpStatus.CREATED.getReasonPhrase());
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    @GetMapping("/kardex/{kardexId}")
    public ApiResponse<List<VitalSignsDto>> getVitalSignsByKardex(@PathVariable Long kardexId) {
        ApiResponse<List<VitalSignsDto>> response = new ApiResponse<>();
        try {
            List<VitalSignsEntity> vitalSigns = vitalSignsService.getByKardexId(kardexId);
            if (vitalSigns.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron signos vitales para este kardex");
                return logApiResponse(response);
            }
            response.setData(vitalSignsService.toDtoList(vitalSigns));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }


    @PutMapping("/update/{id}")
    public ApiResponse<Optional<VitalSignsEntity>> updateVitalSigns(@PathVariable Long id, @RequestBody VitalSignsEntity vitalSignsEntity){
        ApiResponse<Optional<VitalSignsEntity>> response = new ApiResponse<>();
        try{
            Optional<VitalSignsEntity> existingVitalSigns = vitalSignsService.getVitalSignById(id);
            if(existingVitalSigns.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("El signo vital no fue encontrado");
                return logApiResponse(response);
            }

            Optional<KardexEntity> kardex = kardexService.getKardexById(vitalSignsEntity.getKardex().getKardexId());
            if(kardex.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("El kardex no fue encontrado");
                return logApiResponse(response);
            }
            VitalSignsEntity vitalSigns = new VitalSignsEntity();
            vitalSigns.setVitalSignsDate(vitalSignsEntity.getVitalSignsDate());
            vitalSigns.setVitalSignsHour(vitalSignsEntity.getVitalSignsHour());
            vitalSigns.setVitalSignsTemperature(vitalSignsEntity.getVitalSignsTemperature());
            vitalSigns.setVitalSignsHeartRate(vitalSignsEntity.getVitalSignsHeartRate());
            vitalSigns.setVitalSignsRespiratoryRate(vitalSignsEntity.getVitalSignsRespiratoryRate());
            vitalSigns.setVitalSignsBloodPressure(vitalSignsEntity.getVitalSignsBloodPressure());
            vitalSigns.setVitalSignsOxygenSaturation(vitalSignsEntity.getVitalSignsOxygenSaturation());
            vitalSigns.setVitalSignsStatus(vitalSignsEntity.getVitalSignsStatus());
            vitalSigns.setKardex(kardex.get());
            Optional<VitalSignsEntity> updatedVitalSigns = vitalSignsService.updateVitalSigns(id, vitalSigns);
            response.setData(updatedVitalSigns);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<VitalSignsEntity>> deleteVitalSigns(@PathVariable Long id){
        ApiResponse<Optional<VitalSignsEntity>> response = new ApiResponse<>();
        try{
            Optional<VitalSignsEntity> existingVitalSigns = vitalSignsService.getVitalSignById(id);
            if(existingVitalSigns.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("El signo vital no fue encontrado");
                return logApiResponse(response);
            }
            Optional<VitalSignsEntity> deletedVitalSigns = vitalSignsService.deleteVitalSigns(id);
            response.setData(deletedVitalSigns);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
}
