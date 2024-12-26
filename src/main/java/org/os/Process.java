package org.os;

class Process {
    public String name;
    public int burstTime;
    public int arrivalTime;
    public int priority;
    public int remainingTime;
    public int quantum;
    public int waitingTime;
    public int turnaroundTime;
    public int completionTime;

    public Process(String name, int burstTime, int arrivalTime, int priority, int  quantum) {
        this.name = name;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantum = quantum;
    }


}
