package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.BedsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.BedsService;
import bo.com.edu.diplomado.tuSaliud.Service.RoomsService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bed")
public class BedsController extends ApiController{
    @Autowired
    private BedsService bedsService;
    @Autowired
    private RoomsService roomsService;
    
    @GetMapping("/all")
    public ApiResponse<List<BedsEntity>> getAllBeds(){
        ApiResponse<List<BedsEntity>> response = new ApiResponse<>();
        List<BedsEntity> beds = bedsService.getAllBeds();
        response.setData(beds);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<BedsEntity> getBedById(@PathVariable  Long id){
        ApiResponse<BedsEntity> response = new ApiResponse<>();
        try{
            Optional<BedsEntity> bed = bedsService.getBedById(id);
            if(bed.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Cama no encotrada");
                return logApiResponse(response);
            }
            BedsEntity bedResponse = new BedsEntity();
            bedResponse.setBedId(bed.get().getBedId());
            bedResponse.setBedName(bed.get().getBedName());
            bedResponse.setBedStatus(bed.get().getBedStatus());
            bedResponse.setRoom(bed.get().getRoom());
            response.setData(bedResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @PostMapping("/create")
    public ApiResponse<Optional<BedsEntity>> createBed(@RequestBody  BedsEntity bedsEntity){
        ApiResponse<Optional<BedsEntity>> response = new ApiResponse<>();
        try {
            Optional<RoomsEntity> room = roomsService.getRoomById(bedsEntity.getRoom().getRoomId());
            if (room.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("la habitacion no fue encontrada");
                return logApiResponse(response);
            }
            BedsEntity newBed = new BedsEntity();
            newBed.setBedName(bedsEntity.getBedName());
            newBed.setBedStatus(bedsEntity.getBedStatus());
            newBed.setRoom(room.get());
            Optional<BedsEntity> createdBed = bedsService.createBed(newBed);
            response.setData(createdBed);
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
    public ApiResponse<Optional<BedsEntity>> updateBed(@PathVariable Long id, @RequestBody BedsEntity bedsEntity){
        ApiResponse<Optional<BedsEntity>> response = new ApiResponse<>();
        try{
            Optional<RoomsEntity> room = roomsService.getRoomById(bedsEntity.getRoom().getRoomId());
            if(room.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("Cama no encontrada");
                return logApiResponse(response);
            }
            BedsEntity updatedBed = new BedsEntity();
            updatedBed.setBedName(bedsEntity.getBedName());
            updatedBed.setRoom(room.get());
            Optional<BedsEntity> bed = bedsService.updateBed(id, updatedBed);
            response.setData(bed);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<BedsEntity>> deleteBed(@PathVariable Long id){
        ApiResponse<Optional<BedsEntity>> response = new ApiResponse<>();
        try{
            Optional<BedsEntity> existingBed = bedsService.deleteBed(id);

                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
                response.setMessage("Cama eliminada con exito");

        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

}
