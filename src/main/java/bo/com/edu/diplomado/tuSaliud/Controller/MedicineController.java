package bo.com.edu.diplomado.tuSaliud.Controller;

import bo.com.edu.diplomado.tuSaliud.Entity.MedicinesEntity;
import bo.com.edu.diplomado.tuSaliud.Entity.ViaEntity;
import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
import bo.com.edu.diplomado.tuSaliud.Service.MedicinesService;
import bo.com.edu.diplomado.tuSaliud.Service.ViaService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/medicine")
public class MedicineController extends ApiController {
    @Autowired
    private ViaService viaService;
    @Autowired
    private MedicinesService medicinesService;

    @GetMapping("/all")
    public ApiResponse<List<MedicinesEntity>> getAllMedicines() {
        ApiResponse<List<MedicinesEntity>> response = new ApiResponse<>();
        List<MedicinesEntity> medicines = medicinesService.getAllMedicines();
        response.setData(medicines);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage(HttpStatus.OK.getReasonPhrase());
        return logApiResponse(response);
    }

    @GetMapping("/{id}")
    public ApiResponse<MedicinesEntity> getMedicineById(@PathVariable Long id) {
        ApiResponse<MedicinesEntity> response = new ApiResponse<>();
        try {
            Optional<MedicinesEntity> medicine = medicinesService.getMedicineById(id);
            if (medicine.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setMessage("Medicamento no encontrado");
                return logApiResponse(response);
            }
            MedicinesEntity medicineResponse = new MedicinesEntity();
            medicineResponse.setMedicineId(medicine.get().getMedicineId());
            medicineResponse.setMedicineName(medicine.get().getMedicineName());
            medicineResponse.setMedicineLaboratory(medicine.get().getMedicineLaboratory());

            medicineResponse.setMedicineStatus(medicine.get().getMedicineStatus());
            medicineResponse.setVia(medicine.get().getVia());
            response.setData(medicineResponse);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @PostMapping("/create")
    public ApiResponse<Optional<MedicinesEntity>> createMedicine(@RequestBody MedicinesEntity medicinesEntity) {
        ApiResponse<Optional<MedicinesEntity>> response = new ApiResponse<>();
        try {
            Optional<ViaEntity> via = viaService.getViaById(medicinesEntity.getVia().getViaId());
            if (via.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("la via no fue encontrada");
                return logApiResponse(response);
            }
            MedicinesEntity newMedicine = new MedicinesEntity();
            newMedicine.setMedicineName(medicinesEntity.getMedicineName());
            newMedicine.setMedicineLaboratory(medicinesEntity.getMedicineLaboratory());
            newMedicine.setMedicineStatus(medicinesEntity.getMedicineStatus());
            newMedicine.setVia(via.get());
            Optional<MedicinesEntity> createdMedicine = medicinesService.createMedicine(newMedicine);
            response.setData(createdMedicine);
            response.setStatus(HttpStatus.CREATED.value());
            response.setMessage(HttpStatus.CREATED.getReasonPhrase());
        } catch (ConstraintViolationException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Optional<MedicinesEntity>> updateMedicne(@PathVariable Long id, @RequestBody MedicinesEntity medicinesEntity) {
        ApiResponse<Optional<MedicinesEntity>> response = new ApiResponse<>();
        try {
            Optional<ViaEntity> via = viaService.getViaById(medicinesEntity.getVia().getViaId());
            if (via.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
                response.setMessage("la via no fue encontrada");
                return logApiResponse(response);
            }
            MedicinesEntity updatedMedicine = new MedicinesEntity();
            updatedMedicine.setMedicineName(medicinesEntity.getMedicineName());
            updatedMedicine.setMedicineLaboratory(medicinesEntity.getMedicineLaboratory());
            updatedMedicine.setVia(via.get());
            Optional<MedicinesEntity> medicine = medicinesService.updateMedicnes(id, updatedMedicine);
            response.setData(medicine);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());

        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Optional<MedicinesEntity>> deleteMedicine(@PathVariable Long id) {
        ApiResponse<Optional<MedicinesEntity>> response = new ApiResponse<>();
        try {
            Optional<MedicinesEntity> deletedMedicine = medicinesService.deleteMedicine(id);
            if (deletedMedicine.isEmpty()) {
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.setMessage(HttpStatus.NOT_FOUND.getReasonPhrase());
                response.setMessage("Medicamento no encontrado");
                return logApiResponse(response);
            }
            response.setData(deletedMedicine);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(HttpStatus.OK.getReasonPhrase());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        return logApiResponse(response);
    }
}