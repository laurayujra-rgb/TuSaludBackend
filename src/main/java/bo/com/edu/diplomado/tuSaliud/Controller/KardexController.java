package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.DietsEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.KardexEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.KardexDto;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.DietsService;
import bo.com.edu.diplomado.tuSaliud.Service.KardexService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/kardex")
public class KardexController extends ApiController {

    @Autowired
    public KardexService kardexService;

    @Autowired
    public DietsService dietsService;

    // ===== GET /all
    @GetMapping("/all")
    public ApiResponse<List<KardexDto>> getAllKardex() {
        ApiResponse<List<KardexDto>> response = new ApiResponse<>();
        try {
            List<KardexDto> list = kardexService.toDtoList(kardexService.getAllKardex());
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
    public ApiResponse<KardexDto> getKardexById(@PathVariable Long id) {
        ApiResponse<KardexDto> response = new ApiResponse<>();
        try {
            Optional<KardexEntity> opt = kardexService.getKardexById(id);
            if (opt.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Kardex no encontrado");
                return logApiResponse(response);
            }
            response.setData(kardexService.toDto(opt.get()));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== GET /patient/{patientId}
    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<KardexDto>> getKardexByPatient(@PathVariable Long patientId) {
        ApiResponse<List<KardexDto>> response = new ApiResponse<>();
        try {
            List<KardexDto> kardexList = kardexService.getKardexDtoByPatientId(patientId);
            if (kardexList.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron kardex para este paciente");
                return logApiResponse(response);
            }
            response.setData(kardexList);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== GET /patient/{patientId}/role/{roleId}
    @GetMapping("/patient/{patientId}/role/{roleId}")
    public ApiResponse<List<KardexDto>> getKardexByPatientAndRole(
            @PathVariable Long patientId,
            @PathVariable Long roleId) {

        ApiResponse<List<KardexDto>> response = new ApiResponse<>();
        try {
            List<KardexDto> kardexList = kardexService.getKardexDtoByPatientAndRole(patientId, roleId);
            if (kardexList.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron kardex para este paciente y rol");
                return logApiResponse(response);
            }
            response.setData(kardexList);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    @GetMapping("/{id}/patient-info")
    public ApiResponse<Object> getPatientInfoByKardexId(@PathVariable Long id) {
        ApiResponse<Object> response = new ApiResponse<>();

        try {
            Optional<KardexEntity> opt = kardexService.getKardexById(id);
            if (opt.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Kardex no encontrado");
                return logApiResponse(response);
            }

            var kardex = opt.get();
            var patient = kardex.getPatient();

            if (patient == null) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Paciente no encontrado en el Kardex");
                return logApiResponse(response);
            }

            // 🔹 Calcular edad
            Integer edad = null;
            if (patient.getPersonBirthdate() != null) {
                java.time.LocalDate birth = java.time.LocalDate.parse(patient.getPersonBirthdate().toString());
                java.time.LocalDate now = java.time.LocalDate.now();
                edad = java.time.Period.between(birth, now).getYears();
            }

            // 🔹 Crear objeto simple
            var data = new java.util.HashMap<String, Object>();
            data.put("patientId", patient.getPersonId());
            data.put("patientName", (patient.getPersonName() + " " +
                    (patient.getPersonFatherSurname() != null ? patient.getPersonFatherSurname() : "") + " " +
                    (patient.getPersonMotherSurname() != null ? patient.getPersonMotherSurname() : "")).trim());
            data.put("personBirthdate", patient.getPersonBirthdate());
            data.put("personAge", edad);

            response.setData(data);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());

        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }

        return logApiResponse(response);
    }


    // ===== POST /create
    @PostMapping("/create")
    public ApiResponse<KardexDto> createKardex(@RequestBody KardexEntity kardexEntity) {
        ApiResponse<KardexDto> response = new ApiResponse<>();
        try {
            Optional<DietsEntity> diet = dietsService.getDietById(kardexEntity.getDiets().getDietId());
            if (diet.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Dieta no encontrada");
                return logApiResponse(response);
            }
            kardexEntity.setDiets(diet.get());

            Optional<KardexEntity> created = kardexService.createKardex(kardexEntity);
            if (created.isPresent()) {
                response.setData(kardexService.toDto(created.get()));
                response.setStatus(HttpStatus.CREATED.value());
                response.setMessage(HttpStatus.CREATED.getReasonPhrase());
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
    public ApiResponse<KardexDto> updateKardex(@PathVariable Long id, @RequestBody KardexEntity kardexEntity) {
        ApiResponse<KardexDto> response = new ApiResponse<>();
        try {
            Optional<DietsEntity> diet = dietsService.getDietById(kardexEntity.getDiets().getDietId());
            if (diet.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Dieta no encontrada");
                return logApiResponse(response);
            }
            kardexEntity.setDiets(diet.get());
            Optional<KardexEntity> updated = kardexService.updateKardex(id, kardexEntity);
            if (updated.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Kardex no encontrado");
                return logApiResponse(response);
            }
            response.setData(kardexService.toDto(updated.get()));
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
    public ApiResponse<KardexDto> deleteKardex(@PathVariable Long id) {
        ApiResponse<KardexDto> response = new ApiResponse<>();
        try {
            Optional<KardexEntity> deleted = kardexService.deleteKardex(id);
            if (deleted.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Kardex no encontrado");
                return logApiResponse(response);
            }
            response.setData(kardexService.toDto(deleted.get()));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse<List<KardexDto>> getKardexByRoomId(@PathVariable Long roomId) {
        ApiResponse<List<KardexDto>> response = new ApiResponse<>();
        try {
            List<KardexDto> kardexList = kardexService.getKardexDtoByRoomId(roomId);
            if (kardexList.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron kardex en esta sala");
                return logApiResponse(response);
            }
            response.setData(kardexList);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }





}
