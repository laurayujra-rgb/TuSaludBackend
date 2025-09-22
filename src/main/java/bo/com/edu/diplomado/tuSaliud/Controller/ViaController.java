package bo.com.edu.diplomado.tuSaliud.Controller;


import bo.com.edu.diplomado.tuSaliud.Entity.ViaEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.BedsService;
import bo.com.edu.diplomado.tuSaliud.Service.ViaService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/via")
public class ViaController extends  ApiController{

    @Autowired
    private ViaService viaService;
    @Autowired
    private BedsService bedsService;

    @GetMapping("/all")
    public ApiResponse<List<ViaEntity>> getAllVia(){
        ApiResponse<List<ViaEntity>> response = new ApiResponse<>();
        List<ViaEntity> vias = viaService.getAllVia();
        response.setData(vias);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<ViaEntity> getViaById(@PathVariable Long id){
        ApiResponse<ViaEntity> response = new ApiResponse<>();
        try {
            Optional<ViaEntity> via = viaService.getViaById(id);
            if(via.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Via no encontrada");
                return logApiResponse(response);
            }
            ViaEntity viaResponse = new ViaEntity();
            viaResponse.setViaId(via.get().getViaId());
            viaResponse.setViaName(via.get().getViaName());
            viaResponse.setViaStatus(via.get().getViaStatus());
            response.setData(viaResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @PostMapping("/create")
    public ApiResponse<Optional<ViaEntity>> createVia(@RequestBody ViaEntity viaEntity){
        ApiResponse<Optional<ViaEntity>> response = new ApiResponse<>();
        try {
            Optional<ViaEntity> via = viaService.createVia(viaEntity);
            response.setData(via);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());
            response.setMessage("Via creada exitosamente");
        }catch (ConstraintViolationException e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage("Error de constraint violation: " + e.getMessage());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Optional<ViaEntity>> updateVia(@PathVariable Long id, @RequestBody ViaEntity viaEntity){
        ApiResponse<Optional<ViaEntity>> response = new ApiResponse<>();
        try{
            Optional<ViaEntity> existingVia = viaService.getViaById(id);
            if(existingVia.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Via no encontrada");
                return logApiResponse(response);
            }
            ViaEntity updatedVia = new ViaEntity();
            updatedVia.setViaName(viaEntity.getViaName());
            Optional<ViaEntity> via = viaService.updateVia(id, updatedVia);
            response.setData(via);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setMessage("Via actualizada exitosamente");

        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<ViaEntity>>deleteVia(@PathVariable Long id){
        ApiResponse<Optional<ViaEntity>> response = new ApiResponse<>();
        try{
            Optional<ViaEntity> existingVia = viaService.deleteVia(id);
            if(existingVia.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Via no encontrada");
                return logApiResponse(response);
            }
            response.setData(existingVia);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setMessage("Via eliminada exitosamente");

        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

}
