package com.example.Timely.Events;

import org.springframework.context.ApplicationEvent;

import com.example.Timely.Models.ClassSlot;

import lombok.Getter;

@Getter
public class TimetableUpdateEvent extends ApplicationEvent {

    private ClassSlot updatedSlot;
    private String updateType; // e.g., "ADDED", "MODIFIED", "DELETED"

    public TimetableUpdateEvent(Object source, ClassSlot updatedSlot, String updateType) {
        super(source);
        this.updatedSlot = updatedSlot;
        this.updateType = updateType;
    }
}
