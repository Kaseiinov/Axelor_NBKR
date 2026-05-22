package com.axelor.contact.service;

import com.axelor.contact.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NbkrXmlParser {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public LocalDate parseDate(Document document) {
        String dateStr = document.getDocumentElement().getAttribute("Date");
        return LocalDate.parse(dateStr, DATE_FORMAT);
    }

    public List<CurrencyRateDto> parseCurrencies(Document document) {
        NodeList nodes = document.getElementsByTagName("Currency");
        List<CurrencyRateDto> result = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element el = (Element) nodes.item(i);
            result.add(new CurrencyRateDto(
                    el.getAttribute("ISOCode"),
                    getTagValue("Name", el),
                    Integer.parseInt(getTagValue("Nominal", el)),
                    new BigDecimal(getTagValue("Value", el).replace(",", "."))
            ));
        }
        return result;
    }

    public Document fetchDocument(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();
        return document;
    }

    private String getTagValue(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        return list.getLength() > 0 ? list.item(0).getTextContent().trim() : "";
    }
}