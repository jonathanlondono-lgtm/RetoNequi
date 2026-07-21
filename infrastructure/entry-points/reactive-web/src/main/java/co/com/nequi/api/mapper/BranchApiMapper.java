package co.com.nequi.api.mapper;

import co.com.nequi.api.dto.request.BranchDTO;
import co.com.nequi.api.dto.request.UpdateNameDTO;
import co.com.nequi.api.dto.response.BranchResponseDTO;
import co.com.nequi.model.branch.Branch;
import co.com.nequi.model.branch.command.CreateBranchCommand;
import co.com.nequi.model.branch.command.UpdateBranchNameCommand;

public final class BranchApiMapper {
    private BranchApiMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CreateBranchCommand toCommand(BranchDTO request) {
        return new CreateBranchCommand(request.name(), request.franchiseId());
    }

    public static BranchResponseDTO toResponse(Branch branch) {
        return new BranchResponseDTO(branch.getId(), branch.getName(), branch.getFranchiseId());
    }

    public static UpdateBranchNameCommand toUpdateNameCommand(Long branchId, UpdateNameDTO dto) {
        return new UpdateBranchNameCommand(branchId, dto.name());
    }
}
