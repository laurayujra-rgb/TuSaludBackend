package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.ReportsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.ReportsDto;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reports")
public class ReportsController extends ApiController {

    @Autowired
    private ReportsService reportsService;

    // ===== GET /all
    @GetMapping("/all")
    public ApiResponse<List<ReportsDto>> getAllReports() {
        ApiResponse<List<ReportsDto>> response = new ApiResponse<>();
        try {
            List<ReportsDto> list = reportsService.toDtoList(reportsService.getAllReports());
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
    public ApiResponse<ReportsDto> getReportById(@PathVariable Long id) {
        ApiResponse<ReportsDto> response = new ApiResponse<>();
        try {
            Optional<ReportsEntity> opt = reportsService.getReportById(id);
            if (opt.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Reporte no encontrado");
                return logApiResponse(response);
            }
            response.setData(reportsService.toDto(opt.get()));
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== GET /kardex/{kardexId}
    @GetMapping("/kardex/{kardexId}")
    public ApiResponse<List<ReportsDto>> getReportsByKardex(@PathVariable Long kardexId) {
        ApiResponse<List<ReportsDto>> response = new ApiResponse<>();
        try {
            List<ReportsEntity> list = reportsService.getReportsByKardexId(kardexId);
            if (list.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("No se encontraron reportes para este kardex");
                return logApiResponse(response);
            }
            response.setData(reportsService.toDtoList(list));
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
    public ApiResponse<ReportsEntity> createReport(@RequestBody ReportsEntity report) {
        ApiResponse<ReportsEntity> response = new ApiResponse<>();
        try {
            Optional<ReportsEntity> created = reportsService.createReport(report);
            response.setData(created.get());
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }

    // ===== PUT /update/{id}
    @PutMapping("/update/{id}")
    public ApiResponse<ReportsEntity> updateReport(@PathVariable Long id, @RequestBody ReportsEntity report) {
        ApiResponse<ReportsEntity> response = new ApiResponse<>();
        try {
            Optional<ReportsEntity> updated = reportsService.updateReport(id, report);
            if (updated.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Reporte no encontrado");
                return logApiResponse(response);
            }
            response.setData(updated.get());
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
    public ApiResponse<ReportsEntity> deleteReport(@PathVariable Long id) {
        ApiResponse<ReportsEntity> response = new ApiResponse<>();
        try {
            Optional<ReportsEntity> deleted = reportsService.deleteReport(id);
            if (deleted.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage("Reporte no encontrado");
                return logApiResponse(response);
            }
            response.setData(deleted.get());
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
        }
        return logApiResponse(response);
    }
}
