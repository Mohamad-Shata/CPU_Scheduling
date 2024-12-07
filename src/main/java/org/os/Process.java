package org.os;

public class Process {
    public int id;
    public int burstTime;
    public int remainingTime;
    public int priority;
    public int arrivalTime;
    public int waitingTime;
    public int turnaroundTime;
    public int quantum;
    public int completionTime;



    public Process(int id, int burstTime, int priority, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.quantum = 0;
        this.completionTime=0;

    }
}
