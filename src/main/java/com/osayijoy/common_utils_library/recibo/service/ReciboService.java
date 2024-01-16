package com.osayijoy.common_utils_library.recibo.service;

import com.osayijoy.common_utils_library.helper.exception.ZeusRuntimeException;
import com.osayijoy.common_utils_library.recibo.config.ReciboApplicationConfig;
import com.osayijoy.common_utils_library.recibo.config.ReciboApplicationErrorConfig;
import com.osayijoy.common_utils_library.recibo.model.ReciboBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.Arrays;


import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReciboService {

    private final ReciboApplicationErrorConfig errorConfig;
    private final ReciboApplicationConfig applicationConfig;
    private final TemplateEngine templateEngine;

    private Context buildContext(ReciboBody reciboBody) {
        Context context = new Context();
        Arrays.stream(reciboBody.getClass().getDeclaredFields()).forEach(field -> {
            try {
                field.setAccessible(true);
                context.setVariable(field.getName(), field.get(reciboBody));
            } catch (Exception e) {
                log.error("Recibo Service was unable to build context, the logic failed because : {}", e.getMessage());
                throw new ZeusRuntimeException(errorConfig.getContextBuildingFailedErrorMessage(), errorConfig.getContextBuildingFailedErrorCode());
            }
        });
        return context;
    }

    public void generatePdfReceipt(ReciboBody reciboBody, OutputStream outputStream) {
        String html = templateEngine.process(applicationConfig.getHtmlFileName(), buildContext(reciboBody));
        generatePdfReceiptFromHtml(html, outputStream);
    }

    private void generatePdfReceiptFromHtml(String html, OutputStream outputStream) {
        try {
            String xhtml = htmlToXhtml(getCleanedHtml(html));
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(xhtml);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            log.error("Error while generating PDF from HTML: {}", e.getMessage());
            throw new ZeusRuntimeException(errorConfig.getContextBuildingFailedErrorMessage(), errorConfig.getContextBuildingFailedErrorCode());
        }
    }

    private static String htmlToXhtml(String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return document.html();
    }

    private static String getCleanedHtml(String html) {
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode rootTagNode = cleaner.clean(html);
        CleanerProperties cleanerProperties = cleaner.getProperties();
        XmlSerializer xmlSerializer = new PrettyXmlSerializer(cleanerProperties);
        return xmlSerializer.getAsString(rootTagNode);
    }
}

