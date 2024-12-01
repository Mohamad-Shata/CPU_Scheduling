package org.os;

import java.util.*;

public class SRTFScheduler {

    public void schedule(List<Process> processes) {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        // PriorityQueue to sort by remaining time, breaking ties by arrival time
        PriorityQueue<Process> pq = new PriorityQueue<>((p1, p2) -> {
            if (p1.remainingTime == p2.remainingTime) {
                return Integer.compare(p1.arrivalTime, p2.arrivalTime);
            }
            return Integer.compare(p1.remainingTime, p2.remainingTime);
        });

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        while (completed < n) {
            // Add all processes that have arrived by the current time to the queue
            for (Process p : processes) {
                if (p.arrivalTime <= time && !pq.contains(p) && p.remainingTime > 0) {
                    pq.add(p);
                }
            }

            if (pq.isEmpty()) {
                time++;
                continue;
            }

            // Select the process with the shortest remaining time
            Process current = pq.poll();

            // Simulate execution for 1 time unit
            time++;
            current.remainingTime--;

            // If the process is complete
            if (current.remainingTime == 0) {
                completed++;
                current.turnaroundTime = time - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
            } else {
                pq.add(current);
            }
        }

        // Print results
        System.out.println("\nSRTF Scheduling Results:");
        System.out.println("ID\tBurst\tArrival\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.printf("%d\t%d\t%d\t%d\t%d\n", p.id, p.burstTime, p.arrivalTime, p.waitingTime, p.turnaroundTime);
        }
    }
}
