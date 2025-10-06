package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.RoomsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/rooms")
public class RoomsController extends ApiController {

    @Autowired
    public RoomsService roomsService;

    @GetMapping("/all")
    public ApiResponse<List<RoomsEntity>> getAllRooms() {
        ApiResponse<List<RoomsEntity>> response = new ApiResponse<>();
        List<RoomsEntity> rooms = roomsService.getAllRoomsByStatus();
        response.setData(rooms);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("OK");
        return logApiResponse(response);
    }

    @GetMapping("/available")
    public ApiResponse<List<RoomsEntity>> getAvailableRooms() {
        ApiResponse<List<RoomsEntity>> response = new ApiResponse<>();
        response.setData(roomsService.getAvailableRooms());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Salas disponibles");
        return logApiResponse(response);
    }

    @GetMapping("/occupied")
    public ApiResponse<List<RoomsEntity>> getOccupiedRooms() {
        ApiResponse<List<RoomsEntity>> response = new ApiResponse<>();
        response.setData(roomsService.getOccupiedRooms());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Salas ocupadas");
        return logApiResponse(response);
    }

    @PostMapping("/create")
    public ApiResponse<RoomsEntity> createRoom(@RequestBody RoomsEntity room) {
        ApiResponse<RoomsEntity> response = new ApiResponse<>();
        Optional<RoomsEntity> created = roomsService.createRoom(room);
        response.setData(created.get());
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Sala creada correctamente");
        return logApiResponse(response);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<RoomsEntity> updateRoom(@PathVariable Long id, @RequestBody RoomsEntity room) {
        ApiResponse<RoomsEntity> response = new ApiResponse<>();
        Optional<RoomsEntity> updated = roomsService.updateRoom(id, room);
        response.setData(updated.get());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Sala actualizada correctamente");
        return logApiResponse(response);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<RoomsEntity> deleteRoom(@PathVariable Long id) {
        ApiResponse<RoomsEntity> response = new ApiResponse<>();
        Optional<RoomsEntity> deleted = roomsService.deleteRoom(id);
        response.setData(deleted.get());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Sala eliminada correctamente");
        return logApiResponse(response);
    }
}
