package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexMedicinesEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.KardexMedicineDto;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.KardexMedicinesService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kardex-medicines")
public class KardexMedicinesController extends ApiController {

    @Autowired
    public KardexMedicinesService kardexMedicinesService;

    // ===== GET /all
    @GetMapping("/all")
    public ApiResponse<List<KardexMedicineDto>> getAll() {
        ApiResponse<List<KardexMedicineDto>> response = new ApiResponse<>();
        try {
            List<KardexMedicineDto> list = kardexMedicinesService.toDtoList(kardexMedicinesService.getAll());
            response.setData(list);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== GET /{id}
    @GetMapping("/{id}")
    public ApiResponse<KardexMedicineDto> getById(@PathVariable Long id) {
        ApiResponse<KardexMedicineDto> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> opt = kardexMedicinesService.getById(id);
            if (opt.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No encontrado");
                return logApiResponse(response);
            }
            response.setData(kardexMedicinesService.toDto(opt.get()));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
    // GET /api/kardex-medicines/by-kardex/{kardexId}
    @GetMapping("/by-kardex/{kardexId}")
    public ApiResponse<List<KardexMedicineDto>> getByKardex(@PathVariable Long kardexId) {
        ApiResponse<List<KardexMedicineDto>> response = new ApiResponse<>();
        try {
            List<KardexMedicineDto> list = kardexMedicinesService.getByKardex(kardexId);
            if (list.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No hay medicamentos registrados para este kardex");
            } else {
                response.setData(list);
                response.setStatus(HttpStatus.OK.value());
                response.setMessage(HttpStatus.OK.getReasonPhrase());
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== POST /create
    @PostMapping("/create")
    public ApiResponse<KardexMedicineDto> create(@RequestBody KardexMedicinesEntity body) {
        ApiResponse<KardexMedicineDto> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> created = kardexMedicinesService.create(body);
            if (created.isPresent()) {
                response.setData(kardexMedicinesService.toDto(created.get()));
                response.setStatus(HttpStatus.CREATED.value());
                response.setMessage(HttpStatus.CREATED.getReasonPhrase());
            } else {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("No se pudo crear");
            }
        } catch (ConstraintViolationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Restricción violada");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== PUT /update/{id}
    @PutMapping("/update/{id}")
    public ApiResponse<KardexMedicineDto> update(@PathVariable Long id, @RequestBody KardexMedicinesEntity body) {
        ApiResponse<KardexMedicineDto> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> updated = kardexMedicinesService.update(id, body);
            if (updated.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No encontrado");
                return logApiResponse(response);
            }
            response.setData(kardexMedicinesService.toDto(updated.get()));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== DELETE /delete/{id}
    @DeleteMapping("/delete/{id}")
    public ApiResponse<KardexMedicineDto> delete(@PathVariable Long id) {
        ApiResponse<KardexMedicineDto> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> deleted = kardexMedicinesService.delete(id);
            if (deleted.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No encontrado");
                return logApiResponse(response);
            }
            response.setData(kardexMedicinesService.toDto(deleted.get()));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
}
