package org.os;

public class Process {
    public int id;
    public int burstTime;
    public int remainingTime;
    public int priority;
    public int arrivalTime;
    public int waitingTime;
    public int turnaroundTime;

    public Process(int id, int burstTime, int priority, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime; // Used for SRTF
        this.priority = priority;      // Used for Priority Scheduling
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
    }
}
