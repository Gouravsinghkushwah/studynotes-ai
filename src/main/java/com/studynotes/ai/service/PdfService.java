package com.studynotes.ai.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import com.lowagie.text.pdf.draw.LineSeparator;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    // Generate PDF from multiple sections
    public byte[] generatePdf(String topic, List<String> sections, List<String> sectionTitles) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 50, 50); // margins
            PdfWriter.getInstance(document, baos);
            document.open();

            // Title
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph(topic, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add each section
            Font sectionTitleFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font contentFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            for (int i = 0; i < sections.size(); i++) {
                String sectionText = sections.get(i);
                String sectionTitle = sectionTitles.get(i);

                if (sectionText == null || sectionText.trim().isEmpty()) continue;

                Paragraph secTitlePara = new Paragraph(sectionTitle, sectionTitleFont);
                secTitlePara.setSpacingBefore(10);
                secTitlePara.setSpacingAfter(5);
                document.add(secTitlePara);

                Paragraph contentPara = new Paragraph(sectionText, contentFont);
                contentPara.setSpacingAfter(10);
                document.add(contentPara);

                // Optional: add line separator
                document.add(new LineSeparator());
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}