package com.pwr.gastromate.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyAverageUsageDTO {
    private int dayOfWeek;
    private double averageUsage;
}
