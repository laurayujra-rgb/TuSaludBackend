package bo.com.edu.diplomado.tuSaliud.Models.Dto;

public record AccountResponse(
        Long accountId,
        String accountEmail,
        Integer accountStatus,
        Long personId
) {}