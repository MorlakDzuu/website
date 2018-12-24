package com.website.website.service.notes;

import java.util.List;

public class Notification {
    List<String> notes;

    public void addNote(String note) {
        if (note != null) {
            notes.add(note);
        }
    }

    public List<String> getNotes() {
        return notes;
    }
}
