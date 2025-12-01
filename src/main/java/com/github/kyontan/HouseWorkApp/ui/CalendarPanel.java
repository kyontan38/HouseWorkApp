package com.github.kyontan.HouseWorkApp.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.github.kyontan.HouseWorkApp.model.ScheduleItem;

public class CalendarPanel extends JPanel {

    private LocalDate weekStart;
    private JTable calendarTable;
    private List<String> persons;
    private List<String> tasks;
    private List<ScheduleItem> scheduleItems;

    private JButton backButton = new JButton("← 結果画面へ戻る");

    public CalendarPanel(List<String> persons, List<String> tasks, List<ScheduleItem> scheduleItems) {
        this.persons = persons;
        this.tasks = tasks;
        this.scheduleItems = scheduleItems;

        weekStart = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);

        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton prevBtn = new JButton("◀ 前の週");
        JButton nextBtn = new JButton("次の週 ▶");

        JLabel dateLabel = new JLabel(getWeekLabel(), SwingConstants.CENTER);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));

        topPanel.add(prevBtn, BorderLayout.WEST);
        topPanel.add(dateLabel, BorderLayout.CENTER);
        topPanel.add(nextBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        prevBtn.addActionListener(e -> {
            weekStart = weekStart.minusWeeks(1);
            dateLabel.setText(getWeekLabel());
            refreshTable();
        });

        nextBtn.addActionListener(e -> {
            weekStart = weekStart.plusWeeks(1);
            dateLabel.setText(getWeekLabel());
            refreshTable();
        });

        refreshTable();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ⭐----- scheduleItems を表示へ反映 -------⭐
    private void refreshTable() {
        if (calendarTable != null) {
            remove(calendarTable.getParent());
        }

        String[] columnNames = new String[7];
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd (E)");

        LocalDate day = weekStart;
        for (int i = 0; i < 7; i++) {
            columnNames[i] = day.format(format);
            day = day.plusDays(1);
        }

        Object[][] tableData = new Object[tasks.size()][7];

        for (int row = 0; row < tasks.size(); row++) {
            String task = tasks.get(row);
            LocalDate d = weekStart;

            for (int col = 0; col < 7; col++) {

                String assigned = "未割当";

                // 🔥 スケジュールデータに該当日があれば表示
                for (ScheduleItem item : scheduleItems) {
                    if (item.getTask().equals(task) && item.getDate().equals(d)) {
                        assigned = item.getAssignedPerson();
                        break;
                    }
                }

                tableData[row][col] = assigned;
                d = d.plusDays(1);
            }
        }

        calendarTable = new JTable(tableData, columnNames);
        calendarTable.setRowHeight(35);

        JScrollPane scrollPane = new JScrollPane(calendarTable);
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private String getWeekLabel() {
        LocalDate end = weekStart.plusDays(6);
        return weekStart + " 〜 " + end;
    }

    //⭐----- ResultPanel → カレンダー遷移時に割り当て生成 -------⭐
    public void updateCalendar(List<String> persons, List<String> tasks, List<ScheduleItem> scheduleItems) {
        this.persons = persons;
        this.tasks = tasks;
        this.scheduleItems = scheduleItems;

        refreshTable();
    }

    // ⭐---- タスクを自動で参加者に割り当てるロジック ----⭐
    private void generateSchedule() {
        scheduleItems.clear();

        LocalDate d = weekStart;
        int personIndex = 0;

        for (String task : tasks) {

            String person = persons.get(personIndex);

            scheduleItems.add(new ScheduleItem(d, person, task));

            personIndex++;

            if (personIndex >= persons.size()) {
                personIndex = 0;
                d = d.plusDays(1);
            }
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
