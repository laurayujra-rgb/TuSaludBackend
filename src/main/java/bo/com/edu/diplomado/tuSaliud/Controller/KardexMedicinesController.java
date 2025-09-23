package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.MedicinesEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.KardexMedicinesEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.KardexMedicinesService;
import bo.com.edu.diplomado.tuSaliud.Service.KardexService;
import bo.com.edu.diplomado.tuSaliud.Service.MedicinesService;
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

    @Autowired
    public KardexService kardexService;

    @Autowired
    public MedicinesService medicinesService;

    // ===== GET /all
    @GetMapping("/all")
    public ApiResponse<List<KardexMedicinesEntity>> getAll() {
        ApiResponse<List<KardexMedicinesEntity>> response = new ApiResponse<>();
        try {
            List<KardexMedicinesEntity> list = kardexMedicinesService.getAll();
            response.setData(list);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== GET /{id}
    @GetMapping("/{id}")
    public ApiResponse<KardexMedicinesEntity> getById(@PathVariable Long id) {
        ApiResponse<KardexMedicinesEntity> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> opt = kardexMedicinesService.getById(id);
            if (opt.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Relación Kardex–Medicamento no encontrada");
                return logApiResponse(response);
            }
            response.setData(opt.get());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== GET /kardex/{kardexId}
    @GetMapping("/kardex/{kardexId}")
    public ApiResponse<List<KardexMedicinesEntity>> getByKardex(@PathVariable Long kardexId) {
        ApiResponse<List<KardexMedicinesEntity>> response = new ApiResponse<>();
        try {
            List<KardexMedicinesEntity> list = kardexMedicinesService.getByKardexId(kardexId);
            response.setData(list);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== GET /medicine/{medicineId}
    @GetMapping("/medicine/{medicineId}")
    public ApiResponse<List<KardexMedicinesEntity>> getByMedicine(@PathVariable Long medicineId) {
        ApiResponse<List<KardexMedicinesEntity>> response = new ApiResponse<>();
        try {
            List<KardexMedicinesEntity> list = kardexMedicinesService.getByMedicineId(medicineId);
            response.setData(list);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== POST /create (body = entidad)
    @PostMapping("/create")
    public ApiResponse<Optional<KardexMedicinesEntity>> create(@RequestBody KardexMedicinesEntity body) {
        ApiResponse<Optional<KardexMedicinesEntity>> response = new ApiResponse<>();
        try {
            // Validar FKs como en tus controllers
            if (body.getKardex() == null || body.getKardex().getKardexId() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Kardex inválido");
                return logApiResponse(response);
            }
            if (body.getMedicine() == null || body.getMedicine().getMedicineId() == 0) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Medicamento inválido");
                return logApiResponse(response);
            }

            // Service hará validaciones y guardado
            Optional<KardexMedicinesEntity> created = kardexMedicinesService.create(body);
            response.setData(created);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());

        } catch (ConstraintViolationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage() != null ? e.getMessage() : HttpStatus.BAD_REQUEST.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== PUT /update/{id} (body = entidad con cambios)
    @PutMapping("/update/{id}")
    public ApiResponse<Optional<KardexMedicinesEntity>> update(@PathVariable Long id,
                                                               @RequestBody KardexMedicinesEntity body) {
        ApiResponse<Optional<KardexMedicinesEntity>> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> updated = kardexMedicinesService.update(id, body);
            if (updated.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Relación Kardex–Medicamento no encontrada");
                return logApiResponse(response);
            }
            response.setData(updated);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());

        } catch (IllegalArgumentException | IllegalStateException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage() != null ? e.getMessage() : HttpStatus.BAD_REQUEST.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== DELETE /delete/{id} (soft-delete: status=0)
    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<KardexMedicinesEntity>> delete(@PathVariable Long id) {
        ApiResponse<Optional<KardexMedicinesEntity>> response = new ApiResponse<>();
        try {
            Optional<KardexMedicinesEntity> deleted = kardexMedicinesService.delete(id);
            if (deleted.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Relación Kardex–Medicamento no encontrada");
                return logApiResponse(response);
            }
            response.setData(deleted);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    // ===== (Opcional) DELETE por par FK (borrado físico)
    @DeleteMapping("/delete/kardex/{kardexId}/medicine/{medicineId}")
    public ApiResponse<Void> deleteByPair(@PathVariable Long kardexId, @PathVariable Long medicineId) {
        ApiResponse<Void> response = new ApiResponse<>();
        try {
            kardexMedicinesService.deleteByPair(kardexId, medicineId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}
