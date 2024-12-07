package org.os;

import java.util.*;

public class FCAI_Scheduler
{

    public void schedule(List<Process> processes)
    {
        int time = 0;
        int completed = 0;
        int n = processes.size();
        int lastArrivalTime = processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(1);
        int maxBurstTime = processes.stream().mapToInt(p -> p.burstTime).max().orElse(1);
        double V1 = Math.ceil(lastArrivalTime / 10.0);
        double V2 = Math.ceil(maxBurstTime / 10.0);

        Map<Process, Integer> quantumMap = new HashMap<>();
        Map<Process, List<Integer>> quantumHistory = new HashMap<>();
        List<Integer> executionOrder = new ArrayList<>();

        for (Process process : processes) {
            int initialQuantum = (int) Math.ceil(process.burstTime / 2.0); // Use ceil
            quantumMap.put(process, initialQuantum);
            quantumHistory.put(process, new ArrayList<>(List.of(initialQuantum)));
        }

        PriorityQueue<Process> pq = new PriorityQueue<>((p1, p2) -> {
            double fcaiFactor1 = (10 - p1.priority) + (p1.arrivalTime / V1) + (p1.remainingTime / V2);
            double fcaiFactor2 = (10 - p2.priority) + (p2.arrivalTime / V1) + (p2.remainingTime / V2);
            return Double.compare(fcaiFactor1, fcaiFactor2);
        });

        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        while (completed < n) {
            for (Process p : processes) {
                if (p.arrivalTime <= time && !pq.contains(p) && p.remainingTime > 0) {
                    pq.add(p);
                }
            }

            if (pq.isEmpty()) {
                time++;
                continue;
            }

            Process current = pq.poll();
            int quantum = quantumMap.get(current);

            executionOrder.add(current.id);

            int executeTime = (int) Math.ceil(0.4 * quantum);
            executeTime = Math.min(executeTime, current.remainingTime);

            time += executeTime;
            current.remainingTime -= executeTime;

            if (current.remainingTime > 0 && executeTime < quantum) {
                int remainingQuantum = quantum - executeTime;
                int preemptTime = (int) Math.ceil(Math.min(remainingQuantum, current.remainingTime));
                time += preemptTime;
                current.remainingTime -= preemptTime;
            }

            if (current.remainingTime > 0) {
                int unusedQuantum = quantum - executeTime;
                int updatedQuantum = (int) Math.ceil(quantum + unusedQuantum);
                quantumMap.put(current, updatedQuantum);
                quantumHistory.get(current).add(updatedQuantum);
                pq.add(current);
            } else {
                completed++;
                current.completionTime = time;
                current.turnaroundTime = time - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
            }
        }

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process p : processes) {
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }

        double avgWaitingTime = Math.ceil((double) totalWaitingTime / processes.size());
        double avgTurnaroundTime = Math.ceil((double) totalTurnaroundTime / processes.size());

        System.out.printf("%-4s %-6s %-8s %-8s %-8s %-10s %-12s %-20s%n",
                "ID", "Burst", "Arrival", "Priority", "Waiting", "Completion", "Turnaround", "Quantum History");

        for (Process p : processes) {
            System.out.printf("%-4d %-6d %-8d %-8d %-8d %-10d %-12d %-20s%n",
                    p.id, p.burstTime, p.arrivalTime, p.priority, p.waitingTime, p.completionTime, p.turnaroundTime , quantumHistory.get(p));
        }

        System.out.printf("\nAverage Waiting Time: %.2f units\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f units\n", avgTurnaroundTime);
        System.out.println("\nProcesses Execution Order:");
        System.out.println(executionOrder);
    }
}
