package com.studynotes.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromptService {

    private static final Logger logger = LoggerFactory.getLogger(PromptService.class);

    public String buildPrompt(String text, List<String> actions, String customPrompt) {
        StringBuilder prompt = new StringBuilder();
        int i = 1;

        // Trim the text to remove unnecessary spaces
        String cleanText = (text != null) ? text.trim() : "";

// Copy actions to prevent modifying original list (thread-safe)
        List<String> finalActions = new ArrayList<>();
        if (actions != null) finalActions.addAll(actions);

        // Add custom prompt automatically if user provided it
        if (customPrompt != null && !customPrompt.isEmpty()) {
            finalActions.add("customPrompt");
            logger.info("Custom prompt detected, adding 'customPrompt' action");
        }

        // Build prompt based on actions
        for (String action : finalActions) {
            switch (action) {
                case "summary":
                    prompt.append(i++).append(". Generate a concise summary of the text:\n").append(cleanText).append("\n");
                    break;
                case "mcq":
                    prompt.append(i++).append(". Create 5-10 MCQ questions from the text:\n").append(cleanText).append("\n");
                    break;
                case "flashcards":
                    prompt.append(i++).append(". Make flashcards from the text with question and answer:\n").append(cleanText).append("\n");
                    break;
                case "shortNotes":
                    prompt.append(i++).append(". Create short notes or bullet points from the text:\n").append(cleanText).append("\n");
                    break;
                case "revision":
                    prompt.append(i++).append(". Make revision notes for quick learning:\n").append(cleanText).append("\n");
                    break;
                case "definitions":
                    prompt.append(i++).append(". Extract key terms and definitions from the text:\n").append(cleanText).append("\n");
                    break;
                case "keyPoints":
                    prompt.append(i++).append(". Highlight important points and facts from the text:\n").append(cleanText).append("\n");
                    break;
                case "importantDates":
                    prompt.append(i++).append(". Extract important dates, events, and timelines from the text:\n").append(cleanText).append("\n");
                    break;
                case "causesEffects":
                    prompt.append(i++).append(". Explain causes and effects mentioned in the text:\n").append(cleanText).append("\n");
                    break;
                case "prosCons":
                    prompt.append(i++).append(". List pros and cons or advantages and disadvantages:\n").append(cleanText).append("\n");
                    break;
                case "examples":
                    prompt.append(i++).append(". Give examples or case studies from the text:\n").append(cleanText).append("\n");
                    break;
                case "mindMap":
                    prompt.append(i++).append(". Create a mind map structure from the text:\n").append(cleanText).append("\n");
                    break;
                case "diagram":
                    prompt.append(i++).append(". Suggest diagrams or charts to explain the text:\n").append(cleanText).append("\n");
                    break;
                case "keywords":
                    prompt.append(i++).append(". Extract key keywords and concepts from the text:\n").append(cleanText).append("\n");
                    break;
                case "formulas":
                    prompt.append(i++).append(". Extract important formulas or equations from the text:\n").append(cleanText).append("\n");
                    break;
                case "historicalEvents":
                    prompt.append(i++).append(". List important historical events mentioned in the text:\n").append(cleanText).append("\n");
                    break;
                case "geography":
                    prompt.append(i++).append(". Extract key geography facts and locations from the text:\n").append(cleanText).append("\n");
                    break;
                case "scienceFacts":
                    prompt.append(i++).append(". Extract key science facts and concepts from the text:\n").append(cleanText).append("\n");
                    break;
                case "currentAffairs":
                    prompt.append(i++).append(". Summarize current affairs and news points from the text:\n").append(cleanText).append("\n");
                    break;
                case "pdf":
                    prompt.append(i++)
                            .append(". Create an extended version of the following topic suitable for PDF notes, including summaries, examples, and key points:\n")
                            .append(cleanText)
                            .append("\n");
                    break;
                case "customPrompt":
                    prompt.append(i++).append(". ").append(customPrompt).append("\n");
                    logger.info("Adding custom prompt: {}", customPrompt);
                    break;
                default:
                    logger.warn("Unknown action: {}", action);
            }
        }

        logger.info("Final prompt built:\n{}", prompt.toString());
        return prompt.toString();
    }
}