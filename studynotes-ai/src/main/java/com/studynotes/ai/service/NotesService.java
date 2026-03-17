package com.studynotes.ai.service;

import com.studynotes.ai.ai.OpenAIClient;
import com.studynotes.ai.model.NotesRequest;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.studynotes.ai.service.NotesService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class NotesService {

    @Autowired
    private PromptService promptService;

    @Autowired
    private OpenAIClient aiClient;

    public String generateNotes(NotesRequest request) {

        String prompt = promptService.buildPrompt(
                request.getText(),
                request.getActions(),
                request.getCustomPrompt()
        );

        return aiClient.generateNotes(prompt);
    }

    // Generate user-friendly summary from uploaded PDF
    public String generateUserFriendlySummary(MultipartFile file) throws IOException {
        // Extract text from PDF
        String pdfText = extractTextFromPdf(file);

        // Build a NotesRequest for summary generation
        NotesRequest request = new NotesRequest();
        request.setText(pdfText);
        request.setActions(List.of("summary")); // specify "summary" action
        request.setCustomPrompt(
                "Generate a readable, engaging summary from the following text. " +
                        "Use multiple paragraphs, proper flow, and bullet points where needed. " +
                        "Make it enjoyable for users to read."
        );

        // Call existing generateNotes logic
        return generateNotes(request);
    }

    public String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(file.getInputStream()))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }
}