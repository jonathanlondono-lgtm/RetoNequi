package co.com.nequi.r2dbc.mapper;

import co.com.nequi.model.franchise.Franchise;
import co.com.nequi.r2dbc.entity.FranchiseData;

public class FranchiseMapper {

    public static Franchise toModel(FranchiseData franchiseData){
        return Franchise.builder()
                .id(franchiseData.getId())
                .name(franchiseData.getName())
                .build();
    }

    public static FranchiseData toData(Franchise franchise){
        return FranchiseData.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}
