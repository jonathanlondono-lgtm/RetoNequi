package co.com.nequi.r2dbc.mapper;

import co.com.nequi.model.branch.Branch;
import co.com.nequi.r2dbc.entity.BranchData;

public class BranchMapper {

    public static Branch toModel(BranchData data) {
        return Branch.builder()
                .id(data.getId())
                .name(data.getName())
                .franchiseId(data.getFranchiseId())
                .build();
    }

    public static BranchData toData(Branch branch) {
        return BranchData.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}
