package com.github.kyontan.HouseWorkApp.model;

import java.time.LocalDate;

public class ScheduleItem {
    private LocalDate date;
    private String task;
    private String assignedPerson;

    public ScheduleItem(LocalDate date, String task, String assignedPerson) {
        this.date = date;
        this.task = task;
        this.assignedPerson = assignedPerson;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTask() {
        return task;
    }

    public String getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }
}

