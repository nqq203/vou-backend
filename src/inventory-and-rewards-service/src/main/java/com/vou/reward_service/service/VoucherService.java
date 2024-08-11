package com.vou.reward_service.service;

import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.entity.CreateVoucherRequest;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Integer createVoucher(CreateVoucherRequest request) {
        try {
            Voucher voucher = new Voucher();
            voucher.setCode(request.getCode());
            voucher.setQrCode(request.getQrCode());
            voucher.setVoucherName(request.getVoucherName());
            voucher.setImageUrl(request.getImageUrl());
            voucher.setDescription(request.getDescription());
            voucher.setExpirationDate(request.getExpirationDate());
            voucher.setStatus("active");
            voucher.setIdItem1(request.getIdItem1());
            voucher.setIdItem2(request.getIdItem2());
            voucher.setIdItem3(request.getIdItem3());
            voucher.setIdItem4(request.getIdItem4());
            voucher.setIdItem5(request.getIdItem5());
            voucher.setAmCoin(request.getAmCoin());
            voucher.setIdEvent(request.getIdEvent());
            voucherRepository.save(voucher);

            return HttpStatus.CREATED;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public Voucher findVoucherByCode(String code) {
        try {
            return voucherRepository.findByCode(code);
        } catch (Exception e) {
            return null;
        }
    }

    public Integer updateVoucherByCode(String code, CreateVoucherRequest request) {
        try {
            Voucher voucherFound = voucherRepository.findByCode(code);
            if (voucherFound == null) {
                return HttpStatus.NOT_FOUND;
            } else {
                if (request.getQrCode() != null) {
                    voucherFound.setQrCode(request.getQrCode());
                }
                if (request.getVoucherName() != null) {
                    voucherFound.setVoucherName(request.getVoucherName());
                }
                if (request.getImageUrl() != null) {
                    voucherFound.setImageUrl(request.getImageUrl());
                }
                if (request.getDescription() != null) {
                    voucherFound.setDescription(request.getDescription());
                }
                if (request.getExpirationDate() != null) {
                    voucherFound.setExpirationDate(request.getExpirationDate());
                }
                if (request.getStatus() != null) {
                    voucherFound.setStatus(request.getStatus());
                }
                if (request.getIdItem1() != null) {
                    voucherFound.setIdItem1(request.getIdItem1());
                }
                if (request.getIdItem2() != null) {
                    voucherFound.setIdItem2(request.getIdItem2());
                }
                if (request.getIdItem3() != null) {
                    voucherFound.setIdItem3(request.getIdItem3());
                }
                if (request.getIdItem4() != null) {
                    voucherFound.setIdItem4(request.getIdItem4());
                }
                if (request.getIdItem5() != null) {
                    voucherFound.setIdItem5(request.getIdItem5());
                }
                if (request.getAmCoin() != null) {
                    voucherFound.setAmCoin(request.getAmCoin());
                }
                if (request.getIdEvent() != null) {
                    voucherFound.setIdEvent(request.getIdEvent());
                }
            }
            voucherRepository.save(voucherFound);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    // TODO: Delete from voucherrepo first
    public Integer deleteVoucherByCode(String code) {
        try {
            Voucher voucherFound = voucherRepository.findByCode(code);
            if (voucherFound == null) {
                return HttpStatus.NOT_FOUND;
            }
            voucherRepository.delete(voucherFound);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
