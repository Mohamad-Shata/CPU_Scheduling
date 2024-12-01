package org.os;
import java.util.*;

class Process {
    int id;
    int burstTime;
    int priority;
    int waitingTime;

    public Process(int id, int burstTime, int priority) {
        this.id = id;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
    }
}

public class NonPreemptivePriorityScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < numProcesses; i++) {
            System.out.print("Enter burst time and priority for Process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            int priority = scanner.nextInt();
            processes.add(new Process(i + 1, burstTime, priority));
        }

        processes.sort(Comparator.comparingInt(p -> p.priority));

        int currentTime = 0;
        int contextSwitchTime = 1;

        System.out.println("\nExecution Order and Details:");
        System.out.println("ProcessID | BurstTime | Priority | WaitingTime");

        for (Process process : processes) {
            process.waitingTime = currentTime;
            System.out.printf("   P%d     |    %d      |    %d    |    %d\n",
                    process.id, process.burstTime, process.priority, process.waitingTime);
            currentTime += process.burstTime;
            currentTime += contextSwitchTime;
        }

        double totalWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).sum();
        double averageWaitingTime = totalWaitingTime / numProcesses;

        System.out.printf("\nAverage Waiting Time: %.2f units\n", averageWaitingTime);

        scanner.close();
    }
}