package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.RolesEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.RolesService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolesController extends  ApiController{

    @Autowired
    private RolesService rolesService;

    @GetMapping("/all")
    public ApiResponse<List<RolesEntity>> getAllRoles(){
        ApiResponse<List<RolesEntity>> response = new ApiResponse<>();
        List<RolesEntity> roles = rolesService.getAllRoles();
        response.setData(roles);
//        response.setStatus(200);
        response.setStatus(HttpStatus.OK.value());
//        response.setMessage("OK");
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<RolesEntity> getRoleById(@PathVariable Long id){
        ApiResponse<RolesEntity> response = new ApiResponse<>();
        try{
            Optional<RolesEntity> role = rolesService.getRolesById(id);
            if(role.isPresent()){
                response.setData(role.get());
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
    public ApiResponse<Optional<RolesEntity>> createRole(@RequestBody RolesEntity rolesEntity){
        ApiResponse<Optional<RolesEntity>> response = new ApiResponse<>();
        try{
            Optional<RolesEntity> role = rolesService.createRoles(rolesEntity);
            response.setData(role);
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
    public ApiResponse<Optional<RolesEntity>> updateRole(@PathVariable Long id, @RequestBody RolesEntity rolesEntity){
        ApiResponse<Optional<RolesEntity>> response = new ApiResponse<>();
        try{
            Optional<RolesEntity> role = rolesService.updateRoles(id, rolesEntity);
            response.setData(role);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
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
    public ApiResponse<Optional<RolesEntity>> deleteRole(@PathVariable Long id){
        ApiResponse<Optional<RolesEntity>> response = new ApiResponse<>();
        try{
            Optional<RolesEntity> role = rolesService.deleteRoles(id);
            response.setData(role);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

}
