package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.DietsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.DietsService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diets")
public class DietsController extends ApiController{
    @Autowired
    public DietsService dietsService;

    @GetMapping("/all")
    public ApiResponse<List<DietsEntity>> getAllDiets(){
        ApiResponse<List<DietsEntity>> response = new ApiResponse<>();
        List<DietsEntity> dits = dietsService.getAllDiets();
        response.setData(dits);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping
    public ApiResponse<List<DietsEntity>> getAllDietsByStatus(){
        ApiResponse<List<DietsEntity>> response = new ApiResponse<>();
        List<DietsEntity> dits = dietsService.getAllDietsByStatus();
        response.setData(dits);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<DietsEntity> getDietById(@PathVariable Long id){
        ApiResponse<DietsEntity> response= new ApiResponse<>();
        try{
            Optional<DietsEntity> optionalDiet = dietsService.getDietById(id);
            if(optionalDiet.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Dieta no encontrada");
                return logApiResponse(response);
            }
            DietsEntity dietResponse = new DietsEntity();
            dietResponse.setDietId(optionalDiet.get().getDietId());
            dietResponse.setDietName(optionalDiet.get().getDietName());
            dietResponse.setDietStatus(optionalDiet.get().getDietStatus());
            response.setData(dietResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @PostMapping("/create")
    public ApiResponse<Optional<DietsEntity>> createDiet(@RequestBody DietsEntity dietsEntity){
        ApiResponse<Optional<DietsEntity>> response = new ApiResponse<>();
        try{
            Optional<DietsEntity> diet = dietsService.createDiet(dietsEntity);
            response.setData(diet);
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
    public ApiResponse<Optional<DietsEntity>> updateDiet(@PathVariable Long id, @RequestBody DietsEntity dietsEntity){
        ApiResponse<Optional<DietsEntity>> response = new ApiResponse<>();
        try{
            Optional<DietsEntity> diet = dietsService.updateDiet(id, dietsEntity);
            response.setData(diet);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<DietsEntity>> deleteDiet(@PathVariable Long id){
        ApiResponse<Optional<DietsEntity>> response = new ApiResponse<>();
        try{
            Optional<DietsEntity> diet = dietsService.deleteDiet(id);
            response.setData(diet);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}
