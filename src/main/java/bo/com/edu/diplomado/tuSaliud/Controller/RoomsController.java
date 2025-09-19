package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.RolesService;
import bo.com.edu.diplomado.tuSaliud.Service.RoomsService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomsController extends ApiController {

    @Autowired
    public RoomsService roomsService;

    @GetMapping("/all")
    public ApiResponse<List<RoomsEntity>> getAllRooms(){
        ApiResponse<List<RoomsEntity>> response = new ApiResponse<>();
        List<RoomsEntity> rooms = roomsService.getAllRooms();
        response.setData(rooms);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("OK");
        return logApiResponse(response);
    }
    @GetMapping
    public ApiResponse<List<RoomsEntity>> getAllRoomsByStatus(){
        ApiResponse<List<RoomsEntity>> response = new ApiResponse<>();
        List<RoomsEntity> rooms = roomsService.getAllRoomsByStatus();
        response.setData(rooms);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("OK");
        return logApiResponse(response);
    }
    @GetMapping("/{id}")
    public ApiResponse<RoomsEntity> getRoomById(@PathVariable Long id){
        ApiResponse<RoomsEntity> response = new ApiResponse<>();
        try{
            Optional<RoomsEntity> optionalRoom = roomsService.getRoomById(id);
            if(optionalRoom.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("La sala no fue encontrada");
                return logApiResponse(response);
            }
            RoomsEntity roomResponse = new RoomsEntity();
            roomResponse.setRoomName(optionalRoom.get().getRoomName());
            response.setData(roomResponse);
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
    public ApiResponse<Optional<RoomsEntity>> createRoom(@RequestBody RoomsEntity roomsEntity){
        ApiResponse<Optional<RoomsEntity>> response = new ApiResponse<>();
        try{
            Optional<RoomsEntity> optionalRoom = roomsService.createRoom(roomsEntity);
            response.setData(optionalRoom);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());
            response.setMessage("Sala creada");
        }catch (ConstraintViolationException e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
    @PutMapping("/update/{id}")
    public ApiResponse<Optional<RoomsEntity>> updateRoom(@PathVariable Long id, @RequestBody RoomsEntity roomsEntity){
        ApiResponse<Optional<RoomsEntity>> response = new ApiResponse<>();
        try{
            Optional<RoomsEntity> optionalRoom = roomsService.updateRoom(id, roomsEntity);
            if(optionalRoom.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("La sala no fue encontrada");
                return logApiResponse(response);
            }
            response.setData(optionalRoom);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setMessage("Sala actualizada");
        }catch (ConstraintViolationException e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<RoomsEntity>> deleteRoom(@PathVariable Long id){
        ApiResponse<Optional<RoomsEntity>> response = new ApiResponse<>();
        try{
            Optional<RoomsEntity> optionalRoom = roomsService.deleteRoom(id);
            if(optionalRoom.isEmpty()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("La sala no fue encontrada");
                return logApiResponse(response);
            }
            response.setData(optionalRoom);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
            response.setMessage("Sala eliminada");
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
}
