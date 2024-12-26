package org.os;

import java.util.*;

public class SchedulerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter burst time,  arrival time, priority, and quantum for Process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            int arrivalTime = scanner.nextInt();
            int priority = scanner.nextInt();
            int quantum = scanner.nextInt();
            System.out.print("enter process name: ");
            String name = scanner.next();
            Process process = new Process(name, burstTime, arrivalTime, priority , quantum);
            processes.add(process);
        }

        System.out.println("\nChoose the scheduler to run:");
        System.out.println("1. Shortest Remaining Time First (SRTF)");
        System.out.println("2. Non-Preemptive Priority");
        System.out.println("3. FCAI Scheduling");
        System.out.println("4. Non-Preemptive SJF Scheduling");
        System.out.print("Enter your choice (1, 2, 3 or 4): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("\nExecuting SRTF Scheduler:");
                SRTFScheduler srtfScheduler = new SRTFScheduler();
                srtfScheduler.schedule(new ArrayList<>(processes));
                break;

            case 2:
                System.out.println("\nExecuting Non-Preemptive Priority Scheduler:");
                NonPreemptivePriorityScheduler priorityScheduler = new NonPreemptivePriorityScheduler();
                priorityScheduler.schedule(new ArrayList<>(processes));
                break;

            case 3:
                System.out.println("\nExecuting FCAI Scheduler:");
                FCAI_Scheduler fcaiScheduler = new FCAI_Scheduler();
                fcaiScheduler.schedule(new ArrayList<>(processes));
                break;

            case 4:
                System.out.println("\nExecuting SJF Scheduler:");
                NonPremptiveSJF sjf = new NonPremptiveSJF() ;
                sjf.schedule(new ArrayList<>(processes));
                break;


            default:
                System.out.println("Invalid choice! Please enter 1, 2, 3 or 4.");
        }

        scanner.close();
    }
}
