package id.co.pat.ticketapp.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class PdfUtil {

    public static byte[] writePdfToFile(String html) {
        Document document = Jsoup.parse(html, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            renderer.setDocumentFromString(document.html());
            renderer.layout();

            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (IOException ignored) {
            return new byte[10];
        }
    }

}
