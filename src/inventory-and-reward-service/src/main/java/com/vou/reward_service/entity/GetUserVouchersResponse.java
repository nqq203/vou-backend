package com.vou.reward_service.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetUserVouchersResponse {
    private List<UserVoucher> userVoucher;
}
