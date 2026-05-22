package com.axelor.contact.job;

import com.axelor.contact.service.CurrencyRateService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@RequiredArgsConstructor(onConstructor_ = @Inject)
@Slf4j
public class CurrencyRateJob implements Job {

    private final CurrencyRateService currencyRateService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("CRON: Запуск автоматической загрузки курсов валют...");
        try {
            currencyRateService.fetchAndSave();
            log.info("CRON: Загрузка курсов завершена успешно.");
        } catch (Exception e) {
            log.error("CRON: Ошибка при загрузке курсов: {}", e.getMessage(), e);
            throw new JobExecutionException(e);
        }
    }
}