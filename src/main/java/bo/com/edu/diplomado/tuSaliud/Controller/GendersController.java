package bo.com.edu.diplomado.tuSaliud.Controller;
import bo.com.edu.diplomado.tuSaliud.Entity.GendersEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.GendersService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/gender")
public class GendersController extends ApiController {
    @Autowired
    private GendersService gendersService;
    @GetMapping("/all")
    public ApiResponse<List<GendersEntity>> getAllGenders(){
        ApiResponse<List<GendersEntity>> response = new ApiResponse<>();
        List<GendersEntity> genders = gendersService.getAllGenders();
        response.setData(genders);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<GendersEntity> getGenerById(@PathVariable Long id){
        ApiResponse<GendersEntity> response = new ApiResponse<>();
        try{
            Optional<GendersEntity> gender = gendersService.getGenderById(id);
            if(gender.isPresent()){
                response.setData(gender.get());
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
    public ApiResponse<Optional<GendersEntity>> createGender(@RequestBody GendersEntity gendersEntity){
        ApiResponse<Optional<GendersEntity>> response = new ApiResponse<>();
        try{
            Optional<GendersEntity> gender = gendersService.createGender(gendersEntity);
            response.setData(gender);
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
    public ApiResponse<Optional<GendersEntity>> updateGender(@PathVariable Long id, @RequestBody GendersEntity gendersEntity){
        ApiResponse<Optional<GendersEntity>> response = new ApiResponse<>();
        try{
            Optional<GendersEntity> gender = gendersService.updateGender(id, gendersEntity);
            response.setData(gender);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<GendersEntity>> deleteGender(@PathVariable Long id){
        ApiResponse<Optional<GendersEntity>> response = new ApiResponse<>();
        try{
            Optional<GendersEntity> gender = gendersService.deleteGender(id);
            response.setData(gender);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}
