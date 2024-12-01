package org.os;

import java.util.*;

public class SchedulerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter burst time, priority, and arrival time for Process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            int priority = scanner.nextInt();
            int arrivalTime = scanner.nextInt();
            processes.add(new Process(i + 1, burstTime, priority, arrivalTime));
        }

        // Ask user which scheduler to run
        System.out.println("\nChoose the scheduler to run:");
        System.out.println("1. Shortest Remaining Time First (SRTF)");
        System.out.println("2. Non-Preemptive Priority");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("\nExecuting SRTF Scheduler:");
                SRTFScheduler srtfScheduler = new SRTFScheduler();
                srtfScheduler.schedule(new ArrayList<>(processes)); // Pass a copy for independent runs
                break;

            case 2:
                System.out.println("\nExecuting Non-Preemptive Priority Scheduler:");
                NonPreemptivePriorityScheduler priorityScheduler = new NonPreemptivePriorityScheduler();
                priorityScheduler.schedule(new ArrayList<>(processes)); // Pass a copy for independent runs
                break;

            default:
                System.out.println("Invalid choice! Please enter 1 or 2.");
        }

        scanner.close();
    }
}

