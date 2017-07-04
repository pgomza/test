package com.horeca.site.services.services;

import com.openhtmltopdf.DOMBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class HtmlToPdfService {

    private static final String BASE_URI = "http://example.com";

    public synchronized OutputStream convert(InputStream htmlInputStream) throws Exception {
        OutputStream os = new FileOutputStream("tmpPdfFile.pdf");

        PdfRendererBuilder builder = new PdfRendererBuilder();
        Document doc = html5ParseDocument(htmlInputStream);
        builder.withW3cDocument(doc, BASE_URI);
        builder.useDefaultPageSize(11.69f, 8.27f, PdfRendererBuilder.PageSizeUnits.INCHES);
        builder.toStream(os);
        builder.run();

        return os;
    }

    private static org.w3c.dom.Document html5ParseDocument(InputStream documentStream) throws IOException
    {
        org.jsoup.nodes.Document document = Jsoup.parse(documentStream, "UTF-8", BASE_URI);
        return DOMBuilder.jsoup2DOM(document);
    }
}
