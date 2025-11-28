package com.github.kyontan.HouseWorkApp.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import com.github.kyontan.HouseWorkApp.model.ScheduleItem;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private PersonInputPanel personInputPanel;
    private TaskInputPanel taskInputPanel;
    private ResultPanel resultPanel;
    private CalendarPanel calendarPanel;

    public MainFrame() {
        // ===== 画面基本設定 =====
        setTitle("家事・役割分担アプリ");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== カードレイアウト生成 =====
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // ===== 画面生成 =====
        personInputPanel = new PersonInputPanel();
        taskInputPanel = new TaskInputPanel();
        resultPanel = new ResultPanel();
        calendarPanel = new CalendarPanel(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<ScheduleItem>());

        // ===== 画面登録 =====
        cardPanel.add(personInputPanel, "PersonInput");
        cardPanel.add(taskInputPanel, "TaskInput");
        cardPanel.add(resultPanel, "Result");
        cardPanel.add(calendarPanel, "Calendar");

        add(cardPanel);

        // ===== 画面遷移イベント =====
        // ▶ Person → Task
        personInputPanel.getNextButton().addActionListener(e -> {
            if (personInputPanel.getParticipants().isEmpty()) {
                JOptionPane.showMessageDialog(this, "少なくとも1人は登録してください！");
                return;
            }
            cardLayout.show(cardPanel, "TaskInput");
        });

        // ▶ Task → Person（戻る）
        taskInputPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(cardPanel, "PersonInput");
        });

        // ▶ Task → Result
        taskInputPanel.getNextButton().addActionListener(e -> {
            if (taskInputPanel.getTasks().isEmpty()) {
                JOptionPane.showMessageDialog(this, "タスクを追加してください！");
                return;
            }

            //結果画面にデータを渡す
            resultPanel.setResult(
                    personInputPanel.getParticipants(),
                    taskInputPanel.getTasks()
            );

            cardLayout.show(cardPanel, "Result");
        });

        // ▶ Result → Task（戻る）
        resultPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(cardPanel, "TaskInput");
        });

        // ▶ Result → Calendar（表示）
        resultPanel.getCalendarButton().addActionListener(e -> {
            //カレンダーへデータ反映
            calendarPanel.updateCalendar(
                    personInputPanel.getParticipants(),
                    taskInputPanel.getTasks(),
                    resultPanel.getScheduleItems()
            );

            cardLayout.show(cardPanel, "Calendar");
        });

        // ▶ Calendar → Result
        calendarPanel.getBackButton().addActionListener(e -> {
            cardLayout.show(cardPanel, "Result");
        });

        setVisible(true);
    }
}
