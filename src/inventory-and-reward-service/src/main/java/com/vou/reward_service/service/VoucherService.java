package com.vou.reward_service.service;

import com.vou.reward_service.common.NotFoundException;
import com.vou.reward_service.constant.HttpStatus;
import com.vou.reward_service.entity.CreateVoucherRequest;
import com.vou.reward_service.entity.UserVoucher;
import com.vou.reward_service.model.Item;
import com.vou.reward_service.model.Voucher;
import com.vou.reward_service.repository.VoucherRepoRepository;
import com.vou.reward_service.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherRepoRepository voucherRepoRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Integer createVoucher(CreateVoucherRequest request) {
        try {
            Voucher voucher = new Voucher();
            voucher.setCode(request.getCode().toUpperCase());
            voucher.setQrCode(request.getQrCode());
            voucher.setVoucherName(request.getVoucherName());
            voucher.setImageUrl(request.getImageUrl());
            voucher.setDescription(request.getDescription());
            voucher.setExpirationDate(request.getExpirationDate());
            voucher.setType("active");
            voucher.setIdItem1(request.getIdItem1());
            voucher.setIdItem2(request.getIdItem2());
            voucher.setIdItem3(request.getIdItem3());
            voucher.setIdItem4(request.getIdItem4());
            voucher.setIdItem5(request.getIdItem5());
            voucher.setAimCoin(request.getAimCoin());
            voucher.setIdEvent(request.getIdEvent());
            voucherRepository.save(voucher);

            return HttpStatus.CREATED;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public Voucher findVoucherByCode(String code) {
        try {
            return voucherRepository.findByCode(code.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public Integer updateVoucherByCode(String code, CreateVoucherRequest request) {
        try {
            Voucher voucherFound = voucherRepository.findByCode(code.toUpperCase());
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
                if (request.getType() != null) {
                    voucherFound.setType(request.getType());
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
                if (request.getAimCoin() != null) {
                    voucherFound.setAimCoin(request.getAimCoin());
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
            Voucher voucherFound = voucherRepository.findByCode(code.toUpperCase());
            if (voucherFound == null) {
                return HttpStatus.NOT_FOUND;
            }
            voucherRepository.delete(voucherFound);
            return HttpStatus.OK;
        } catch (Exception e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public List<UserVoucher> getVouchersByUserId(Long id) throws Exception{
        try {
            List<UserVoucher> vouchersFound = voucherRepoRepository.findVouchersByUserId(id);
            if (vouchersFound == null || vouchersFound.isEmpty()) {
                throw new NotFoundException("Not found any vouchers of this user");
            }
            return vouchersFound;
        } catch (NotFoundException notFoundException) {
            throw notFoundException;
        } catch (Exception e) {
            throw new Exception("Internal Error getting vouchers of user");
        }
    }

    public Boolean uploadInventoryImages(Voucher voucher, String qrImgUrl, String voucherImgUrl) {
        try {
            voucher.setQrCode(qrImgUrl);
            voucher.setImageUrl(voucherImgUrl);
            voucherRepository.save(voucher);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
