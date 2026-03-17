package com.studynotes.ai.controller;

import com.studynotes.ai.model.NotesRequest;
import com.studynotes.ai.service.NotesService;
import com.studynotes.ai.service.PdfService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NotesController {

    private final NotesService notesService;

    private PdfService pdfService;

    public NotesController(NotesService notesService, PdfService pdfService) {
        this.notesService = notesService;
        this.pdfService = pdfService;
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody NotesRequest request) {
        // Trim text
        String cleanText = (request.getText() != null) ? request.getText().trim() : "";

        // Basic validation: text or custom prompt must be provided
        if (cleanText.isEmpty() && (request.getCustomPrompt() == null || request.getCustomPrompt().trim().isEmpty())) {
            return ResponseEntity.badRequest()
                    .body("Cannot generate notes: Please provide text or a custom prompt.");
        }

        // Copy actions to avoid modifying the original list (thread-safe)
        List<String> finalActions = new ArrayList<>();
        if (request.getActions() != null) {
            finalActions.addAll(request.getActions());
        }

        // Create a new NotesRequest object to pass to service
        NotesRequest safeRequest = new NotesRequest();
        safeRequest.setText(cleanText);
        safeRequest.setActions(finalActions);
        safeRequest.setCustomPrompt(request.getCustomPrompt());

        // Call service
        String result = notesService.generateNotes(safeRequest);

        return ResponseEntity.ok(result);
    }
}