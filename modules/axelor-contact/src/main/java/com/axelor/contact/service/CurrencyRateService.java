package com.axelor.contact.service;

import com.google.inject.persist.Transactional;

public interface CurrencyRateService {
    @Transactional
    void fetchAndSave();
}
