package org.os;
import java.util.*;

public class FCAI_Scheduler
{
    public void schedule(List<Process> processes)
    {
        //Track the number of processes completed
        int completed = 0;
        int n = processes.size();
        final int lastArrivalTime = processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(1);
        final int maxBurstTime = processes.stream().mapToInt(p -> p.burstTime).max().orElse(1);
        final double V1 = lastArrivalTime / 10.0;
        final double V2 = maxBurstTime / 10.0;

        //Create a map to store the quantum history of each process.
        Map<Process, List<Integer>> quantumHistory = new HashMap<>();
        //create an empty ArrayList to store Processes execution order
        List<String> executionLog = new ArrayList<>();

        for (Process process : processes)
        {
            quantumHistory.put(process, new ArrayList<>(List.of(process.quantum)));

        }

        //Sorts processes by arrival time to facilitate scheduling.
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        PriorityQueue<Process> pq = new PriorityQueue<>((p1, p2) -> {
            double fcaiFactor1 = (10 - p1.priority) + Math.ceil(p1.arrivalTime / V1) + Math.ceil(p1.remainingTime / V2);
            double fcaiFactor2 = (10 - p2.priority) + Math.ceil(p2.arrivalTime / V1) + Math.ceil(p2.remainingTime / V2);
            return Double.compare(fcaiFactor1, fcaiFactor2);
        });

        //count the time of the run
        int time = processes.stream().mapToInt(p -> p.arrivalTime).min().orElse(0);

        while (completed < n)
        {
            int currentTime = time;

            for (Process p : processes)
            {
                if (p.arrivalTime <= currentTime && !pq.contains(p) && p.remainingTime > 0) {
                    pq.add(p);
                }
            }

            Process current = pq.poll();
            int quantum = current.quantum;
            int executeTime = Math.min((int) Math.ceil(0.4 * quantum), current.remainingTime);

            executionLog.add(current.name + " starts execution, runs for " + executeTime + " units.");
            time += executeTime;
            current.remainingTime -= executeTime;

            if (current.remainingTime == 0)
            {
                completed++;
                current.completionTime = time;
                current.turnaroundTime = time - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                executionLog.add(current.name + " completes execution.");
            }
            else
            {
                if (quantum-executeTime==0)
                {
                    int updatedQuantum = quantum + 2;
                    current.quantum = updatedQuantum;
                    quantumHistory.get(current).add(updatedQuantum);
                    pq.add(current);
                    executionLog.add(current.name + " is preempted with " + current.remainingTime + " units remaining.");
                }

                else
                {
                    int unusedQuantum = quantum - executeTime;
                    int updatedQuantum = quantum + unusedQuantum;
                    current.quantum = updatedQuantum;
                    quantumHistory.get(current).add(updatedQuantum);
                    pq.add(current);
                    executionLog.add(current.name + " is preempted with " + current.remainingTime + " units remaining.");
                }

            }
        }

        int totalWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).sum();
        int totalTurnaroundTime = processes.stream().mapToInt(p -> p.turnaroundTime).sum();

        double avgWaitingTime = (double) totalWaitingTime / n;
        double avgTurnaroundTime = (double) totalTurnaroundTime / n;

        System.out.printf("%-4s %-6s %-8s %-8s %-8s %-10s %-12s %-20s%n",
                "Name", "Burst", "Arrival", "Priority", "Waiting", "Completion", "Turnaround", "Quantum History");

        for (Process p : processes) {
            System.out.printf("%-4s %-6d %-8d %-8d %-8d %-10d %-12d %-20s%n",
                    p.name, p.burstTime, p.arrivalTime, p.priority, p.waitingTime, p.completionTime, p.turnaroundTime, quantumHistory.get(p));
        }

        System.out.printf("\nAverage Waiting Time: %.2f units\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f units\n", avgTurnaroundTime);
        System.out.println("\nProcesses Execution Log:");
        executionLog.forEach(System.out::println);
    }
}
