package com.axelor.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CurrencyRateDto {

    private String code;
    private String name;
    private int nominal;
    private BigDecimal rate;

}
