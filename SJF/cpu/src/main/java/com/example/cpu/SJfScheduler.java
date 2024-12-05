package com.example.cpu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

public class SJfScheduler extends Application {


    private static List<Process> scheduledProcesses = new ArrayList<>();
    private static double avgWaitingTime = 0;
    private static double avgTurnaroundTime = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter Process Name: ");
            String name = scanner.next();
            System.out.print("Enter Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Enter Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter Priority: ");
            int priority = scanner.nextInt();
            System.out.print("Enter Process Color (Hex Code, e.g., #FF5733): ");
            String colorHex = scanner.next();
            Color color = Color.web(colorHex);

            processes.add(new Process(name, arrivalTime, burstTime, priority, color));
        }


        performScheduling(processes);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Gantt chart setup
        Canvas ganttCanvas = new Canvas(800, 200);
        drawGanttChart(ganttCanvas.getGraphicsContext2D(), scheduledProcesses);

        // Process details table
        TableView<Process> processTable = createProcessTable(scheduledProcesses);

        // Statistics section
        Label statsLabel = new Label(String.format(
                "Statistics\nAverage Waiting Time: %.2f\nAverage Turnaround Time: %.2f",
                avgWaitingTime, avgTurnaroundTime));
        statsLabel.setFont(Font.font("Arial", 16));
        statsLabel.setStyle("-fx-text-fill: red;");

        // Layout
        VBox root = new VBox(10);
        root.getChildren().addAll(ganttCanvas, processTable, statsLabel);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CPU Scheduling Visualization");
        primaryStage.show();
    }

    // Method to draw Gantt chart
    private void drawGanttChart(GraphicsContext gc, List<Process> processes) {
        double x = 50; // Initial x-coordinate
        double y = 50; // Fixed y-coordinate
        double height = 50; // Height of each process block
        double scale = 50; // Scale for burst time to pixel ratio

        for (Process process : processes) {
            gc.setFill(process.color); // Use process color
            double width = process.burstTime * scale; // Burst time to width conversion
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height); // Border
            gc.setFill(Color.BLACK);
            gc.fillText(process.id, x + width / 2 - 10, y + height / 2); // Process ID in the middle
            x += width; // Shift x-coordinate for next process
        }
    }

    // Method to create a process table
    private TableView<Process> createProcessTable(List<Process> processes) {
        TableView<Process> table = new TableView<>();

        TableColumn<Process, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().id));

        TableColumn<Process, Integer> burstCol = new TableColumn<>("Burst Time");
        burstCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().burstTime).asObject());

        TableColumn<Process, Integer> arrivalCol = new TableColumn<>("Arrival Time");
        arrivalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().arrivalTime).asObject());

        TableColumn<Process, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().priority).asObject());

        TableColumn<Process, Integer> waitingCol = new TableColumn<>("Waiting Time");
        waitingCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().waitingTime).asObject());

        TableColumn<Process, Integer> turnaroundCol = new TableColumn<>("Turnaround Time");
        turnaroundCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().turnaroundTime).asObject());

        table.getColumns().addAll(nameCol, burstCol, arrivalCol, priorityCol, waitingCol, turnaroundCol);
        table.getItems().addAll(processes);

        return table;
    }

    private static void performScheduling(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        while (!processes.isEmpty()) {
            Process selectedProcess = null;

            for (Process process : processes) {
                if (process.arrivalTime <= currentTime) {
                    if (selectedProcess == null || process.burstTime < selectedProcess.burstTime) {
                        selectedProcess = process;
                    }
                }
            }

            if (selectedProcess == null) {
                currentTime++;
                continue;
            }

            processes.remove(selectedProcess);
            selectedProcess.waitingTime = currentTime - selectedProcess.arrivalTime;
            selectedProcess.turnaroundTime = selectedProcess.waitingTime + selectedProcess.burstTime;
            currentTime += selectedProcess.burstTime;

            totalWaitingTime += selectedProcess.waitingTime;
            totalTurnaroundTime += selectedProcess.turnaroundTime;

            scheduledProcesses.add(selectedProcess);
        }

        avgWaitingTime = totalWaitingTime / scheduledProcesses.size();
        avgTurnaroundTime = totalTurnaroundTime / scheduledProcesses.size();
    }


    static class Process {
        String id;
        int arrivalTime;
        int burstTime;
        int priority;
        int waitingTime;
        int turnaroundTime;
        Color color;

        Process(String id, int arrivalTime, int burstTime, int priority, Color color) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.burstTime = burstTime;
            this.priority = priority;
            this.waitingTime = 0;
            this.turnaroundTime = 0;
            this.color = color;
        }
    }
}
