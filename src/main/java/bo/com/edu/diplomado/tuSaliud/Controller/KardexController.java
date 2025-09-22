package bo.com.edu.diplomado.tuSaliud.Controller;


import bo.com.edu.diplomado.tuSaliud.Entity.DietsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.DietsService;
import bo.com.edu.diplomado.tuSaliud.Service.KardexService;
import bo.com.edu.diplomado.tuSaliud.Service.RolesService;
import org.apache.naming.EjbRef;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kardex")
public class KardexController extends ApiController{
    
    @Autowired
    public KardexService kardexService;
    @Autowired
    public DietsService dietsService;
    @Autowired
    private RolesService rolesService;

    @GetMapping("/all")
    public ApiResponse<List<KardexEntity>> getAllKardex(){
        ApiResponse<List<KardexEntity>> response = new ApiResponse<>();
        List<KardexEntity> kardex = kardexService.getAllKardex();
        response.setData(kardex);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping
    public ApiResponse<List<KardexEntity>> getAllKardexByStatus(){
        ApiResponse<List<KardexEntity>> response = new ApiResponse<>();
        List<KardexEntity> kardex = kardexService.getAllKardexByStatus();
        response.setData(kardex);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<KardexEntity> getKardexById(@PathVariable Long id){
        ApiResponse<KardexEntity> response = new ApiResponse<>();
        try{
            Optional<KardexEntity> optionalKardex = kardexService.getKardexById(id);
            if(optionalKardex.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Kardex no encontrado");
                return logApiResponse(response);
            }
            KardexEntity kardexResponse = new KardexEntity();
            kardexResponse.setKardexId(optionalKardex.get().getKardexId());
            kardexResponse.setKardexNumber(optionalKardex.get().getKardexNumber());
            kardexResponse.setKardexDiagnosis(optionalKardex.get().getKardexDiagnosis());
            kardexResponse.setKardexDate(optionalKardex.get().getKardexDate());
            kardexResponse.setKardexHour(optionalKardex.get().getKardexHour());
            kardexResponse.setKardexStatus(optionalKardex.get().getKardexStatus());
            kardexResponse.setDiets(optionalKardex.get().getDiets());
            response.setData(kardexResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @PostMapping("/create")
    public ApiResponse<Optional<KardexEntity>> createKardex(@RequestBody KardexEntity kardexEntity){
        ApiResponse<Optional<KardexEntity>> response = new ApiResponse<>();
        try{
            Optional<DietsEntity> diet = dietsService.getDietById(kardexEntity.getDiets().getDietId());
            if(diet.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("la dieta no fue encontrada");
                return logApiResponse(response);
            }
            KardexEntity newKardex = new KardexEntity();
            newKardex.setKardexNumber(kardexEntity.getKardexNumber());
            newKardex.setKardexDiagnosis(kardexEntity.getKardexDiagnosis());
            newKardex.setKardexDate(kardexEntity.getKardexDate());
            newKardex.setKardexHour(kardexEntity.getKardexHour());
            newKardex.setKardexStatus(kardexEntity.getKardexStatus());
            newKardex.setDiets(diet.get());
            newKardex.setNursingActions(kardexEntity.getNursingActions());
            Optional<KardexEntity> createdKardex = kardexService.createKardex(newKardex);
            response.setData(createdKardex);
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
    public ApiResponse<Optional<KardexEntity>> updateKardex(@PathVariable Long id, @RequestBody KardexEntity kardexEntity){
        ApiResponse<Optional<KardexEntity>> response = new ApiResponse<>();
        try{
            Optional<DietsEntity> diet = dietsService.getDietById(kardexEntity.getDiets().getDietId());
            if(diet.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("la dieta no fue encontrada");
                return logApiResponse(response);
            }
            KardexEntity updatedKardex = new KardexEntity();
            updatedKardex.setKardexNumber(kardexEntity.getKardexNumber());
            updatedKardex.setKardexDate(kardexEntity.getKardexDate());
            updatedKardex.setKardexHour(kardexEntity.getKardexHour());
            updatedKardex.setKardexStatus(kardexEntity.getKardexStatus());
            updatedKardex.setDiets(diet.get());
            updatedKardex.setKardexDiagnosis(kardexEntity.getKardexDiagnosis());
            updatedKardex.setNursingActions(kardexEntity.getNursingActions());
            Optional<KardexEntity> kardex = kardexService.updateKardex(id, updatedKardex);
            response.setData(kardex);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());

        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<KardexEntity>> deleteKardex(@PathVariable Long id){
        ApiResponse<Optional<KardexEntity>> response = new ApiResponse<>();
        try{
            Optional<KardexEntity> kardex = kardexService.deleteKardex(id);
            response.setData(kardex);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}
