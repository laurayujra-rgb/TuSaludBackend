package bo.com.edu.diplomado.tuSaliud.Service;

import bo.com.edu.diplomado.tuSaliud.Entity.ReportsEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Dto.ReportsDto;
import bo.com.edu.diplomado.tuSaliud.Repository.ReportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportsService {

    @Autowired
    private ReportsRepository reportsRepository;

    // ===== Listar todo
    public List<ReportsEntity> getAllReports() {
        return reportsRepository.findAll();
    }

    // ===== Listar solo activos
    public List<ReportsEntity> getAllReportsByStatus() {
        return reportsRepository.findAllByStatus();
    }

    // ===== Buscar por id
    public Optional<ReportsEntity> getReportById(Long id) {
        return Optional.ofNullable(reportsRepository.findByIdAndByStatus(id, 1L));
    }

    // ===== Crear
    public Optional<ReportsEntity> createReport(ReportsEntity report) {
        return Optional.of(reportsRepository.save(report));
    }

    // ===== Update
    public Optional<ReportsEntity> updateReport(Long id, ReportsEntity reportEntity) {
        Optional<ReportsEntity> existing = reportsRepository.findById(id);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        ReportsEntity report = existing.get();
        report.setKardex(reportEntity.getKardex());
        report.setReportDetails(reportEntity.getReportDetails()); // ✅ nuevo campo
        return Optional.of(reportsRepository.save(report));
    }


    // ===== Soft delete
    public Optional<ReportsEntity> deleteReport(Long id) {
        Optional<ReportsEntity> reportOpt = reportsRepository.findById(id);
        if (reportOpt.isEmpty()) {
            return Optional.empty();
        }
        ReportsEntity report = reportOpt.get();
        report.setReportStatus(0);
        return Optional.of(reportsRepository.save(report));
    }

    // ===== Buscar por kardex
    public List<ReportsEntity> getReportsByKardexId(Long kardexId) {
        return reportsRepository.findAllByKardexId(kardexId);
    }


//last
    public ReportsDto toDto(ReportsEntity entity) {
        if (entity == null) return null;
        return new ReportsDto(
                entity.getReportId(),
                entity.getReportStatus(),
                entity.getReportDetails(),
                entity.getKardex() != null ? entity.getKardex().getKardexId() : null
        );
    }

    public List<ReportsDto> toDtoList(List<ReportsEntity> entities) {
        return entities.stream().map(this::toDto).toList();
    }


}
