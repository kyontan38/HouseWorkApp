package com.github.kyontan.HouseWorkApp.service;

import com.github.kyontan.HouseWorkApp.model.Participant;
import com.github.kyontan.HouseWorkApp.model.Task;
import java.util.List;

public class ScheduleGenerator {

    public static String[][] generateWeekly(List<Participant> participants, List<Task> tasks) {
        int days = 7;
        String[][] schedule = new String[days][tasks.size()];

        for (int day = 0; day < days; day++) {
            for (int t = 0; t < tasks.size(); t++) {
                int participantIndex = (day + t) % participants.size(); // (day + t) を人数で割った余りが、担当者のインデックス
                schedule[day][t] = participants.get(participantIndex).getName();
            }
        }
        return schedule;
    }
}
