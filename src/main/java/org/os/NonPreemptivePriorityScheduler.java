package org.os;

import java.util.*;

public class NonPreemptivePriorityScheduler {

    public void schedule(List<Process> processes) {
        int currentTime = 0;
        int contextSwitchTime = 1;
        List<Process> readyQueue = new ArrayList<>();
        List<Process> remainingProcesses = new ArrayList<>(processes);

        System.out.println("\nNon-Preemptive Priority Scheduling Results:");
        System.out.printf("%-4s %-6s %-8s %-8s %-10s%n", "ID", "Burst", "Priority", "Waiting", "Turnaround");

        while (!remainingProcesses.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes that have arrived to the ready queue
            Iterator<Process> iterator = remainingProcesses.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            // If no process is ready, advance time
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            // Choose the highest priority process from the ready queue
            Process currentProcess = readyQueue.stream()
                    .min(Comparator.comparingInt(p -> p.priority))
                    .orElseThrow();

            readyQueue.remove(currentProcess);

            // Calculate waiting and turnaround times
            currentProcess.waitingTime = currentTime - currentProcess.arrivalTime;
            currentProcess.turnaroundTime = currentProcess.waitingTime + currentProcess.burstTime;

            // Update the current time
            currentTime += currentProcess.burstTime;
            currentTime += contextSwitchTime;

            // Print process details
            System.out.printf(
                    "%-4s   %-6d  %-8d %-8d  %-10d%n",
                    currentProcess.name, currentProcess.burstTime, currentProcess.priority,
                    currentProcess.waitingTime, currentProcess.turnaroundTime
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
