package com.axelor.contact.web;

import com.axelor.contact.service.CurrencyRateService;
import com.axelor.contact.service.impl.CurrencyRateServiceImpl;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor(onConstructor_ = @Inject)
@Slf4j
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    public void fetchRates(ActionRequest request, ActionResponse response) {
        try {
            log.info("Ручной запуск загрузки курсов валют");
            currencyRateService.fetchAndSave();
            response.setInfo("Курсы валют успешно обновлены!");
        } catch (Exception e) {
            log.error("Ошибка при ручном обновлении курсов: {}", e.getMessage(), e);
            response.setError("Ошибка при обновлении курсов: " + e.getMessage());
        }
    }
}