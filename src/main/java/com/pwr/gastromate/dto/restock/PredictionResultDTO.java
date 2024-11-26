package com.pwr.gastromate.dto.restock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PredictionResultDTO {
    private String date;
    private Double predictedValue;

}