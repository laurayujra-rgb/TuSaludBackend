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
import java.util.*;

@RestController
@RequestMapping("/api/bed")
public class BedsController extends ApiController {

    @Autowired
    private BedsService bedsService;

    @Autowired
    private RoomsService roomsService;

    // 🔹 Todas las camas
    @GetMapping("/all")
    public ApiResponse<List<BedsEntity>> getAllBeds() {
        ApiResponse<List<BedsEntity>> response = new ApiResponse<>();
        List<BedsEntity> beds = bedsService.getAllBedsByStatus();
        response.setData(beds);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("OK");
        return logApiResponse(response);
    }

    // 🔹 Camas disponibles
    @GetMapping("/available")
    public ApiResponse<List<BedsEntity>> getAvailableBeds() {
        ApiResponse<List<BedsEntity>> response = new ApiResponse<>();
        List<BedsEntity> beds = bedsService.getAvailableBeds();
        response.setData(beds);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Camas disponibles");
        return logApiResponse(response);
    }

    // 🔹 Camas ocupadas
    @GetMapping("/occupied")
    public ApiResponse<List<BedsEntity>> getOccupiedBeds() {
        ApiResponse<List<BedsEntity>> response = new ApiResponse<>();
        List<BedsEntity> beds = bedsService.getOccupiedBeds();
        response.setData(beds);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Camas ocupadas");
        return logApiResponse(response);
    }

    // 🔹 Camas por habitación
    @GetMapping("/room/{roomId}")
    public ApiResponse<List<BedsEntity>> getBedsByRoomId(@PathVariable Long roomId) {
        ApiResponse<List<BedsEntity>> response = new ApiResponse<>();
        List<BedsEntity> beds = bedsService.getBedsByRoomId(roomId);
        response.setData(beds);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Camas por habitación");
        return logApiResponse(response);
    }

    // 🔹 Crear cama
    @PostMapping("/create")
    public ApiResponse<BedsEntity> createBed(@RequestBody BedsEntity bedsEntity) {
        ApiResponse<BedsEntity> response = new ApiResponse<>();
        try {
            Optional<RoomsEntity> room = roomsService.getRoomById(bedsEntity.getRoom().getRoomId());
            if (room.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Sala no encontrada");
                return logApiResponse(response);
            }

            bedsEntity.setRoom(room.get());
            Optional<BedsEntity> created = bedsService.createBed(bedsEntity);
            response.setData(created.get());
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage("Cama creada correctamente");

        } catch (ConstraintViolationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Restricción violada");
        }
        return logApiResponse(response);
    }

    // 🔹 Actualizar cama
// 🔹 Actualizar cama
    @PutMapping("/update/{id}")
    public ApiResponse<BedsEntity> updateBed(@PathVariable Long id, @RequestBody BedsEntity bedsEntity) {
        ApiResponse<BedsEntity> response = new ApiResponse<>();
        try {
            // ⚠️ Forzar el ID correcto en la entidad, por seguridad
            bedsEntity.setBedId(id);

            Optional<BedsEntity> updated = bedsService.updateBed(bedsEntity);
            if (updated.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Cama no encontrada");
                return logApiResponse(response);
            }

            response.setData(updated.get());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Cama actualizada correctamente");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Error al actualizar la cama: " + e.getMessage());
        }
        return logApiResponse(response);
    }


    // 🔹 Eliminar cama
    @DeleteMapping("/delete/{id}")
    public ApiResponse<BedsEntity> deleteBed(@PathVariable Long id) {
        ApiResponse<BedsEntity> response = new ApiResponse<>();
        Optional<BedsEntity> deleted = bedsService.deleteBed(id);
        response.setData(deleted.get());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Cama eliminada correctamente");
        return logApiResponse(response);
    }
}
