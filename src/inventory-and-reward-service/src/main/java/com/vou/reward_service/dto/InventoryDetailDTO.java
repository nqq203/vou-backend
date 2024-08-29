package com.vou.reward_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
public class InventoryDetailDTO {
    private String voucher_type;
    private String voucher_code;
    private String voucher_description;
    private String voucher_name;
    private Long voucher_price;
    private String imageUrl;
    private String qrCode;
    private Long aim_coin;
    private Timestamp expiration_date;
    private List<ItemDetailDTO> items;
    private Long event_id;

    public InventoryDetailDTO(
            String voucher_type,
            String voucher_code,
            String voucher_description,
            String voucher_name,
            Long voucher_price,
            String imageUrl,
            String qrCode,
            Long aim_coin,
            Timestamp expiration_date,
            List<ItemDetailDTO> items,
            Long event_id
    ) {
        this.voucher_type = voucher_type;
        this.voucher_code = voucher_code;
        this.voucher_description = voucher_description;
        this.voucher_name = voucher_name;
        this.voucher_price = voucher_price;
        this.imageUrl = imageUrl;
        this.qrCode = qrCode;
        this.aim_coin = aim_coin;
        this.expiration_date = expiration_date;
        this.items = items;
        this.event_id = event_id;
    }
}
