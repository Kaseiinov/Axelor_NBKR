package com.axelor.contact;

import com.axelor.app.AxelorModule;
import com.axelor.contact.service.CurrencyRateService;
import com.axelor.contact.service.NbkrHttpClient;
import com.axelor.contact.service.NbkrXmlParser;
import com.axelor.contact.service.impl.CurrencyRateServiceImpl;

public class ContactModule extends AxelorModule {

    @Override
    protected void configure() {
        bind(CurrencyRateService.class).to(CurrencyRateServiceImpl.class);
        bind(NbkrHttpClient.class);
        bind(NbkrXmlParser.class);
    }
}