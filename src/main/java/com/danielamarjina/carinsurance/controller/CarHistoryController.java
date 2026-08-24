package com.danielamarjina.carinsurance.controller;

import com.danielamarjina.carinsurance.dto.response.CarHistoryResponse;
import com.danielamarjina.carinsurance.enums.CarHistoryType;
import com.danielamarjina.carinsurance.service.CarHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class CarHistoryController {
    private final CarHistoryService service;

    @GetMapping("/cars/{carId}/history")
    public List<CarHistoryResponse> getCarHistory(@PathVariable UUID carId,
                                                  @RequestParam(required = false)CarHistoryType type){
        return service.getCarHistory(carId,type);
    }
}
