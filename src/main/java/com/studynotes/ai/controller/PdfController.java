package com.studynotes.ai.controller;

import com.studynotes.ai.model.NotesRequest;
import com.studynotes.ai.service.NotesService;
import com.studynotes.ai.service.PdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class PdfController {

    private final NotesService notesService;

    private PdfService pdfService;

    public PdfController(NotesService notesService, PdfService pdfService) {
        this.pdfService = pdfService;
        this.notesService = notesService;
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<Map<String, String>> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            String pdfText = notesService.extractTextFromPdf(file);

            NotesRequest notesRequest = new NotesRequest();
            notesRequest.setText(pdfText);
            notesRequest.setActions(List.of("summary"));

            String summary = notesService.generateNotes(notesRequest);

            return ResponseEntity.ok(Map.of("summary", summary));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process PDF"));
        }
    }

    @PostMapping("/download/pdf")
    public ResponseEntity<ByteArrayResource> downloadPdf(@RequestBody NotesRequest request) {
        String cleanText = (request.getText() != null) ? request.getText().trim() : "";
        List<String> actions = request.getActions() != null ? request.getActions() : new ArrayList<>();

        // Generate AI notes for all actions
        List<String> sections = new ArrayList<>();
        List<String> sectionTitles = new ArrayList<>();

        NotesRequest singleRequest = new NotesRequest();
        singleRequest.setText(cleanText);
        singleRequest.setActions(request.getActions());
        singleRequest.setCustomPrompt(request.getCustomPrompt());


        for (String action : actions) {
            String result = notesService.generateNotes(singleRequest);
            sections.add(result);
            sectionTitles.add(action.substring(0, 1).toUpperCase() + action.substring(1)); // e.g., "Summary"
        }

        NotesRequest notesRequest = new NotesRequest();
        singleRequest.setText(cleanText);
        singleRequest.setActions(request.getActions());
        singleRequest.setCustomPrompt(request.getCustomPrompt());

        if (request.getCustomPrompt() != null && !request.getCustomPrompt().isEmpty()) {
            String customResult = notesService.generateNotes(notesRequest);
            sections.add(customResult);
            sectionTitles.add("Custom Prompt");
        }

        byte[] pdfBytes = pdfService.generatePdf(cleanText, sections, sectionTitles);

        // Generate a safe filename based on topic
        String safeFilename = cleanText.replaceAll("[^a-zA-Z0-9\\-_ ]", "_"); // remove special chars
        if(safeFilename.length() > 50) safeFilename = safeFilename.substring(0,50); // limit length
        safeFilename = safeFilename.replaceAll(" ", "_"); // replace spaces with underscores
        safeFilename += ".pdf";

        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("StudyNotes.pdf").build());
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(resource);
    }

}