package com.horeca.site.services.services;

import com.openhtmltopdf.DOMBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class HtmlToPdfService {

    private static final String BASE_URI = "http://example.com";

    public synchronized ByteArrayOutputStream convert(InputStream htmlInputStream, float width, float height)
            throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();
        Document doc = html5ParseDocument(htmlInputStream);
        builder.withW3cDocument(doc, BASE_URI);
        builder.useDefaultPageSize(width, height, PdfRendererBuilder.PageSizeUnits.INCHES);
        builder.toStream(outputStream);
        builder.run();

        return outputStream;
    }

    private static org.w3c.dom.Document html5ParseDocument(InputStream documentStream) throws IOException
    {
        org.jsoup.nodes.Document document = Jsoup.parse(documentStream, "UTF-8", BASE_URI);
        return DOMBuilder.jsoup2DOM(document);
    }
}
