package com.vou.streaming_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardDetail {
    RewardDTO rewardDTO;
    int turns;

    public RewardDetail(RewardDTO rewardDTO, int turns) {
        this.rewardDTO = rewardDTO;
        this.turns = turns;
    }
}
