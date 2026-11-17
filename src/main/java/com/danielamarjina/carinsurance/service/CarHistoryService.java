package com.danielamarjina.carinsurance.service;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.entity.Car;
import com.danielamarjina.carinsurance.entity.Claim;
import com.danielamarjina.carinsurance.entity.InsurancePolicy;
import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.exception.CarNotFoundException;
import com.danielamarjina.carinsurance.exception.InsurancePolicyNotFoundException;
import com.danielamarjina.carinsurance.repository.CarRepository;
import com.danielamarjina.carinsurance.repository.ClaimRepository;
import com.danielamarjina.carinsurance.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarHistoryService {
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final ClaimRepository claimRepository;
    private final CarRepository carRepository;

    public Page<CarHistoryResponse> getCarHistory(UUID carId, CarHistoryType type, Pageable pageable){

        if (!carRepository.existsById(carId)) {
            throw new CarNotFoundException(carId);
        }

        List<CarHistoryResponse> carHistoryList=new ArrayList<>();

        if(type!=CarHistoryType.CLAIM){
            List<InsurancePolicy> insurancePolicies=insurancePolicyRepository.findByCarId(carId);
            for(InsurancePolicy policy: insurancePolicies){
                carHistoryList.add(new CarHistoryResponse(
                        CarHistoryType.POLICY,
                        policy.getId(),
                        policy.getStartDate(),
                        policy.getEndDate(),
                        policy.getProvider(),
                        policy.getPaidAmount(),
                        policy.getStatus(),
                        null,
                        null,
                        null,
                        null
                ));
            }


        }

        if(type!=CarHistoryType.POLICY){
            List<Claim> claims=claimRepository.findByCarId(carId);
            for(Claim claim: claims){
                carHistoryList.add(new CarHistoryResponse(
                        CarHistoryType.CLAIM,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        claim.getId(),
                        claim.getClaimDate(),
                        claim.getAmount(),
                        claim.getDescription()
                ));
            }


        }

        carHistoryList.sort(Comparator.comparing(
                CarHistoryResponse::getDate,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        int start= Math.toIntExact(pageable.getOffset());
        int end=Math.min(start+pageable.getPageSize(),carHistoryList.size());

        List<CarHistoryResponse> pageContent=
                start>=carHistoryList.size() ? List.of() : carHistoryList.subList(start,end);

        return new PageImpl<>(pageContent,pageable,carHistoryList.size());
    }
}
