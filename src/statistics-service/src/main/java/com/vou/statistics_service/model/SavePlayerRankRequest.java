package com.vou.statistics_service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Data
public class SavePlayerRankRequest {
    private List<PlayerFinalRank> list;
}
