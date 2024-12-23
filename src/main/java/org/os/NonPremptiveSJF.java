package org.os;
import java.util.*;


public class NonPremptiveSJF {
    public void schedule(ArrayList<Process> processes) {
        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int numProcesses = processes.size();

        List<Process> outputProcesses = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Add processes to the ready queue that have arrived by the current time
            Iterator<Process> iterator = processes.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.arrivalTime <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            // If the ready queue is empty, increment time to the next process's arrival
            if (readyQueue.isEmpty()) {
                currentTime = processes.get(0).arrivalTime;
                continue;
            }

            // Apply aging: Increase priority of processes that have been waiting
            for (Process process : readyQueue) {
                process.priority += 1; // Increase priority as they wait longer
            }

            // Select the process with the shortest burst time, breaking ties by priority
            readyQueue.sort((p1, p2) -> {
                if (p1.burstTime == p2.burstTime) {
                    return Integer.compare(p2.priority, p1.priority); // Higher priority first
                }
                return Integer.compare(p1.burstTime, p2.burstTime);
            });

            // Execute the process with the shortest burst time
            Process selectedProcess = readyQueue.remove(0);

            // Calculate waiting time and update current time
            selectedProcess.waitingTime = currentTime - selectedProcess.arrivalTime;
            currentTime += selectedProcess.burstTime;

            // Add the process to the output
            outputProcesses.add(selectedProcess);
        }

        // Print results
        System.out.println("\nExecution Order and Details:");
        System.out.println("ProcessID | ArrivalTime | BurstTime | WaitingTime | TurnaroundTime | Priority");

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : outputProcesses) {
            int turnaroundTime = process.waitingTime + process.burstTime;
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += turnaroundTime;

            System.out.printf("   %s     |      %d      |     %d     |      %d      |       %d  |       %d\n",
                    process.name, process.arrivalTime, process.burstTime, process.waitingTime, turnaroundTime, process.priority);
        }

        System.out.printf("\nAverage Waiting Time: %.2f units\n", totalWaitingTime / numProcesses);
        System.out.printf("Average Turnaround Time: %.2f units\n", totalTurnaroundTime / numProcesses);
    }
}
