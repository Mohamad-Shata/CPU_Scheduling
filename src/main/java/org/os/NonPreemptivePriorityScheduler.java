package org.os;

import java.util.*;

public class NonPreemptivePriorityScheduler {

    public void schedule(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.priority));

        int currentTime = 0;
        int contextSwitchTime = 1;

        System.out.println("\nNon-Preemptive Priority Scheduling Results:");
        System.out.printf("%-4s %-6s %-8s %-8s %-10s%n", "ID", "Burst", "Priority", "Waiting", "Turnaround");

        for (Process process : processes) {
            process.waitingTime = currentTime - process.arrivalTime;
            if (process.waitingTime < 0) {
                process.waitingTime = 0;
                currentTime = process.arrivalTime;
            }

            process.turnaroundTime = process.waitingTime + process.burstTime;
            currentTime += process.burstTime;
            currentTime += contextSwitchTime;

            System.out.printf(
                    "%-4d %-6d %-8d %-8d %-10d%n",
                    process.id, process.burstTime, process.priority, process.waitingTime, process.turnaroundTime
            );
        }

        // Calculate averages
        int totalWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).sum();
        int totalTurnaroundTime = processes.stream().mapToInt(p -> p.turnaroundTime).sum();
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        // Print averages
        System.out.printf("\nAverage Waiting Time: %.2f units\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f units\n", averageTurnaroundTime);
    }
}
