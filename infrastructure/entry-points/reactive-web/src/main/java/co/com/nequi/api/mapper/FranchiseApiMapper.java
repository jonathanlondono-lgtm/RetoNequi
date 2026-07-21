package co.com.nequi.api.mapper;

import co.com.nequi.api.dto.request.FranchiseDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.response.FranchiseResponseDTO;
import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.model.franchise.command.CreateFranchiseCommand;
import co.com.nequi.model.franchise.command.UpdateFranchiseNameCommand;

public final class FranchiseApiMapper {
    private FranchiseApiMapper() {throw new UnsupportedOperationException("Utility class");}

    public static CreateFranchiseCommand toCommand(FranchiseDTO request) {
        return new CreateFranchiseCommand(request.name());
    }
    public static FranchiseResponseDTO toResponse(Franchise franchise){
        return new FranchiseResponseDTO(franchise.getId(),franchise.getName());
    }

    public static UpdateFranchiseNameCommand toUpdateNameCommand(Long franchiseId, UpdateNameDTO dto) {
        return new UpdateFranchiseNameCommand(franchiseId, dto.name());
    }
}
