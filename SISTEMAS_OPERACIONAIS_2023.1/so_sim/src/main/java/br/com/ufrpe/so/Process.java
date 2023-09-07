package br.com.ufrpe.so;

public class Process {

    private int pid;
    private int cpuBurst;
    private int waiting;
    private int turnaround;
    private int contaBursts = 0;
    private int priority;

    public Process() {
    }

    public Process(int pid, int cpuBurst, int waiting, int turnaround) {
        this.pid = pid;
        this.cpuBurst = cpuBurst;
        this.waiting = waiting;
        this.turnaround = turnaround;
    }

    public Process(int pid, int cpuBurst, int waiting, int turnaround, int priority) {
        this.pid = pid;
        this.cpuBurst = cpuBurst;
        this.waiting = waiting;
        this.turnaround = turnaround;
        this.priority = priority;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(int cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public int getWaiting() {
        return waiting;
    }

    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }

    public void incrementWaiting() {
        this.waiting++;
    }

    public int getTurnaround() {
        return turnaround;
    }

    public void setTurnaround(int turnaround) {
        this.turnaround = turnaround;
    }

    public void incrementTurnaround() {
        this.turnaround++;
    }

    public int getContaBursts() {
        return contaBursts;
    }

    public void setContaBursts(int contaBursts) {
        this.contaBursts = contaBursts;
    }

    public void incrementContaBursts() {
        this.contaBursts++;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}