package org.os;

import java.util.*;

public class NonPreemptivePriorityScheduler {

    public void schedule(List<Process> processes) {
        // Sort by priority (lower value = higher priority)
        processes.sort(Comparator.comparingInt(p -> p.priority));

        int currentTime = 0;
        int contextSwitchTime = 1;

        System.out.println("\nNon-Preemptive Priority Scheduling Results:");
        System.out.println("ID\tBurst\tPriority\tWaiting\tTurnaround");

        for (Process process : processes) {
            process.waitingTime = currentTime - process.arrivalTime;
            if (process.waitingTime < 0) {
                process.waitingTime = 0; // Adjust for idle time
                currentTime = process.arrivalTime;
            }

            process.turnaroundTime = process.waitingTime + process.burstTime;
            currentTime += process.burstTime;
            currentTime += contextSwitchTime;

            System.out.printf("%d\t%d\t%d\t\t%d\t%d\n", process.id, process.burstTime, process.priority, process.waitingTime, process.turnaroundTime);
        }

        double totalWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).sum();
        double averageWaitingTime = totalWaitingTime / processes.size();

        System.out.printf("\nAverage Waiting Time: %.2f units\n", averageWaitingTime);
    }
}
