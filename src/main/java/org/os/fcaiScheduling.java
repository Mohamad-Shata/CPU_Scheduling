package org.os;
import java.util.*;

public class fcaiScheduling {

    ArrayList<Process> processes;
    Queue<Process> arrivedQueue = new LinkedList<>();
    PriorityQueue<Process> queue;
    ArrayList<Process> executedProcesses = new ArrayList<>();
    ArrayList<Process> completedProcesses = new ArrayList<>();
    ArrayList<ArrayList<Integer>> quantumUpdateHistory = new ArrayList<>();
    double avgTurnAroundTime = 0, avgWaitingTime = 0;
    //private int contextSwitchTime;


    public fcaiScheduling(ArrayList<Process> processes) {
        this.processes = processes;
        //this.contextSwitchTime = contextSwitchTime;
        queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.fcaiFactor));
        calculateInitialFCAIFactors();
    }

    private void calculateInitialFCAIFactors() {
        int currentTime = 0;
        int lastArrivalTime = processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(0);
        int maxBurstTime = processes.stream().mapToInt(p -> p.burstTime).max().orElse(1);
        double V1 = lastArrivalTime / 10.0;
        double V2 = maxBurstTime / 10.0;

        for (Process p : processes) {

            double fcaiFactor = Math.ceil(10 - p.priority) + Math.ceil(p.arrivalTime / V1) + Math.ceil(p.remainingTime / V2);
            p.fcaiFactor = (int) fcaiFactor;
        }
    }

    public void updateQuantum() {
        ArrayList<Integer> quantumUpdate = new ArrayList<>();
        for (Process p : processes) {
            quantumUpdate.add(p.quantum);
        }
        quantumUpdateHistory.add(quantumUpdate);
    }

    public void start() {
        Process currentProcess = null;
        updateQuantum();

        for (int time = 0, pNum = 0; !arrivedQueue.isEmpty() || pNum < processes.size() || currentProcess != null; time++) {
            while (pNum < processes.size() && processes.get(pNum).arrivalTime <= time) {
                arrivedQueue.add(processes.get(pNum));
                queue.add(processes.get(pNum));
                pNum++;
            }

            if (currentProcess == null && !arrivedQueue.isEmpty()) {
                currentProcess = arrivedQueue.poll();
                currentProcess.starttime = time;
            }

            if (currentProcess != null) {
                int executionTime = Math.min(currentProcess.quantum, currentProcess.burstTime);
                currentProcess.burstTime -= executionTime;
                time += executionTime;

                // If there is time for a context switch, add it
//                     if (currentProcess.burstTime > 0) {
//                         time += contextSwitchTime;
//                     }

                // Update timings
                currentProcess.endtime = time;
                currentProcess.turnaroundTime = currentProcess.endtime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - (processes.get(processes.indexOf(currentProcess)).burstTime);

                avgTurnAroundTime += currentProcess.turnaroundTime;
                avgWaitingTime += currentProcess.waitingTime;

                executedProcesses.add(currentProcess);
                completedProcesses.add(currentProcess);


                if (currentProcess.burstTime > 0) {
                    currentProcess.quantum += 2; // Increase quantum if not finished
                    queue.add(currentProcess); // Re-add to queue
                }

                currentProcess = null; // Process finished
            }

            // Re-evaluate queue and pick the next process
            if (currentProcess == null && !queue.isEmpty()) {
                currentProcess = queue.poll();
                currentProcess.starttime = time;
            }

            // Update FCAI factors for all processes in the queue
            double V1 = (double) processes.stream().mapToInt(p -> p.arrivalTime).max().orElse(0) / 10;
            double V2 = (double) processes.stream().mapToInt(p -> p.burstTime).max().orElse(0) / 10;
            for (Process p : queue) {
                double fcaiFactor = Math.ceil(10 - p.priority) + Math.ceil(p.arrivalTime / V1) + Math.ceil(p.remainingTime / V2);
                p.fcaiFactor = (int) fcaiFactor;
            }
        }

        printResult();

    }

    void printResult() {
        System.out.println("\nQuantum Update History:");
        for (ArrayList<Integer> quantumHistory : quantumUpdateHistory) {
            System.out.print("( ");
            for (int j = 0; j < quantumHistory.size(); j++) {
                System.out.print(quantumHistory.get(j) + (j < quantumHistory.size() - 1 ? ", " : " "));
            }
            System.out.println(")");
        }

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Execution Order:");
        executedProcesses.forEach(p -> System.out.print(p.name + " "));
        System.out.println("\n---------------------------------------------------------------------------------------");

        System.out.printf("%-15s%-15s%-20s%-15s%-20s%-20s\n",
                "Process Name", "Arrival Time", "Burst Time", "Priority", "Turnaround Time", "Waiting Time");
        System.out.println("---------------------------------------------------------------------------------------");

        for (Process p : completedProcesses) {
            System.out.printf("%-15s%-15d%-20d%-15d%-20d%-20d\n",
                    p.name, p.arrivalTime, processes.get(processes.indexOf(p)).burstTime,
                    p.priority, p.turnaroundTime, p.waitingTime);
        }

        System.out.println("---------------------------------------------------------------------------------------");
        avgWaitingTime /= processes.size();
        avgTurnAroundTime /= processes.size();
        System.out.printf("%-20s%-20s\n", "Average Turnaround Time", "Average Waiting Time");
        System.out.printf("%-20.2f%-20.2f\n", avgTurnAroundTime, avgWaitingTime);
        System.out.println("---------------------------------------------------------------------------------------");
    }

}

