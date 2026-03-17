package com.studynotes.ai.model;

import java.util.List;

public class NotesRequest {

    private String text;
    private List<String> actions;
    private String customPrompt;

    public String getText() {
        return text;
    }

    public List<String> getActions() {
        return actions;
    }

    public String getCustomPrompt() {
        return customPrompt;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public void setCustomPrompt(String customPrompt) {
        this.customPrompt = customPrompt;
    }
}