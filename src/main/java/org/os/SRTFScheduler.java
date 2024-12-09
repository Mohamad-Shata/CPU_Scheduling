package org.os;

import java.util.*;

public class SRTFScheduler {

    public void schedule(List<Process> processes) {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        PriorityQueue<Process> pq = new PriorityQueue<>((p1, p2) -> {
            if (p1.remainingTime == p2.remainingTime) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
            return Integer.compare(p1.remainingTime, p2.remainingTime);
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

            time++;
            current.remainingTime--;


            if (current.remainingTime == 0) {
                completed++;
                current.turnaroundTime = time - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
            } else {
                pq.add(current);
            }
        }

        System.out.println("\nSRTF Scheduling Results:");
        System.out.printf("%-4s %-6s %-8s %-8s %-10s%n", "ID", "Burst", "Arrival", "Waiting", "Turnaround");

        for (Process p : processes) {
            System.out.printf(
                    "%-4d %-6d %-8d %-8d %-10d%n",
                    p.id, p.burstTime, p.arrivalTime, p.waitingTime, p.turnaroundTime
            );
        }

        int totalWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).sum();
        int totalTurnaroundTime = processes.stream().mapToInt(p -> p.turnaroundTime).sum();
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.printf("\nAverage Waiting Time: %.2f units\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f units\n", avgTurnaroundTime);

    }
}
