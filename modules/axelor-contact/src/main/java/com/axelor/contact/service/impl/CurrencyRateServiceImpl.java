package com.axelor.contact.service.impl;

import com.axelor.contact.db.CurrencyRate;
import com.axelor.contact.db.repo.CurrencyRateRepository;
import com.axelor.contact.dto.CurrencyRateDto;
import com.axelor.contact.service.CurrencyRateService;
import com.axelor.contact.service.NbkrHttpClient;
import com.axelor.contact.service.NbkrXmlParser;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CurrencyRateServiceImpl implements CurrencyRateService {

    private final CurrencyRateRepository repository;
    private final NbkrHttpClient httpClient;
    private final NbkrXmlParser parser;


    @Override
    @Transactional
    public void fetchAndSave() {
        log.info("Начало загрузки курсов валют с НБКР");
        try {

            InputStream stream = httpClient.fetchRatesStream();
            Document document = parser.fetchDocument(stream);
            LocalDate rateDate = parser.parseDate(document);

            log.info("Дата курса: {}", rateDate);

            List<CurrencyRateDto> rates = parser.parseCurrencies(document);
            int created = 0;
            int updated = 0;

            for (CurrencyRateDto dto : rates) {
                if (updateIfExists(dto, rateDate)) {
                    updated++;
                } else {
                    createNew(dto, rateDate);
                    created++;
                }
            }

            log.info("Завершено. Создано: {}, Обновлено: {}", created, updated);

        } catch (Exception e) {
            log.error("Ошибка загрузки курсов: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка загрузки курсов с НБКР", e);
        }
    }

    private boolean updateIfExists(CurrencyRateDto dto, LocalDate rateDate) {
        CurrencyRate existing = repository.all()
                .filter("self.code = :code AND self.rateDate = :rateDate")
                .bind("code", dto.getCode())
                .bind("rateDate", rateDate)
                .fetchOne();

        if (existing == null) return false;

        existing.setName(dto.getName());
        existing.setNominal(dto.getNominal());
        existing.setRate(dto.getRate());
        repository.save(existing);
        return true;
    }

    private void createNew(CurrencyRateDto dto, LocalDate rateDate) {
        CurrencyRate entity = new CurrencyRate();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setNominal(dto.getNominal());
        entity.setRate(dto.getRate());
        entity.setRateDate(rateDate);
        repository.save(entity);
    }
}