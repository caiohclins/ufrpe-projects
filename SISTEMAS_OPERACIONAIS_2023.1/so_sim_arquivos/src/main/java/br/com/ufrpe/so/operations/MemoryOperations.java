package br.com.ufrpe.so.operations;

import java.util.List;

import br.com.ufrpe.so.model.Memory;
import br.com.ufrpe.so.model.Process;

public class MemoryOperations {

    // Implementação do First-Fit
    public static boolean allocateFirstFit(Memory memory, Process process) {
        int fitIndex = -1;
        int freeSpace = 0;
        int processSize = process.getSize();
        List<Process> alocationList = memory.getSize();

        for (Process p : alocationList) {
            if (p == null){
                freeSpace++;
                if (fitIndex == -1){
                    fitIndex = alocationList.indexOf(p);
                }
                if (freeSpace >= processSize) {
                    try {
                        memory.addProcess(processSize, fitIndex, process);
                        return true;
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }
            } else {
                freeSpace = 0;
                fitIndex = -1;
            }
        }

        return false; // Não há espaço suficiente para alocar o processo
    }

    // Implementação do Best-Fit
    public static boolean allocateBestFit(Memory memory, Process process) {
        List<Process> alocationList = memory.getSize();
        int processSize = process.getSize();
        int bestFitIndex = -1;
        int bestFitSize = -1;
        int initialIndex = 0;
        int free = 0;

        for (Process p : alocationList){
            if (p == null){
                free++;
            } else {
                if (free != 0){
                    initialIndex = alocationList.indexOf(p) - free;
                    if (free == processSize){
                         try {
                            memory.addProcess(processSize, initialIndex, process);
                            return true;
                        } catch (Exception e) {
                            return true;
                        }
                    } else if (bestFitSize == -1 || ((bestFitSize == -1 || free < bestFitSize) && free > processSize)){
                        bestFitIndex = initialIndex;
                        bestFitSize = free;
                    }
                    free = 0;
                } else {
                    continue;
                }
            }
        }

        if (bestFitSize == -1 || ((bestFitSize == -1 || free < bestFitSize) && free >= processSize)){
            bestFitIndex = alocationList.size() - free;
            bestFitSize = free;
        }

        if (bestFitIndex != -1 && (free >= processSize || bestFitSize >= processSize)){
            try {
                memory.addProcess(processSize, bestFitIndex, process);
                return true;
            } catch (Exception e) {
                return true;
            }
        }

        if (free != 0 && free >= processSize){
            try {
                memory.addProcess(processSize, alocationList.size() - free, process);
                return true;
            } catch (Exception e) {
                return true;
            }
        }

        return false; // Não há espaço suficiente para alocar o processo
    }

    // Implementação do Worst-Fit
    public static boolean allocateWorstFit(Memory memory, Process process) {
       List<Process> alocationList = memory.getSize();
        int processSize = process.getSize();
        int worstFitIndex = -1;
        int worstFitSize = -1;
        int initialIndex = 0;
        int free = 0;

        for (Process p : alocationList){
            if (p == null){
                free++;
            } else {
                if (free != 0){
                    initialIndex = alocationList.indexOf(p) - free;
                    if (free == processSize){
                         try {
                            memory.addProcess(processSize, initialIndex, process);
                            return true;
                        } catch (Exception e) {
                            return true;
                        }
                    } else if (worstFitSize == -1 || ((worstFitSize == -1 || free > worstFitSize) && free > processSize)){
                        worstFitIndex = initialIndex;
                        worstFitSize = free;
                    }
                    free = 0;
                } else {
                    continue;
                }
            }
        }

        if (worstFitSize == -1 || ((worstFitSize == -1 || free > worstFitSize) && free >= processSize)){
            worstFitIndex = alocationList.size() - free;
            worstFitSize = free;
        }

        if (worstFitIndex != -1 && (free >= processSize || worstFitSize >= processSize)){
            try {
                memory.addProcess(processSize, worstFitIndex, process);
                return true;
            } catch (Exception e) {
                return true;
            }
        }

        if (free != 0 && free >= processSize){
            try {
                memory.addProcess(processSize, alocationList.size() - free, process);
                return true;
            } catch (Exception e) {
                return true;
            }
        }

        return false; // Não há espaço suficiente para alocar o processo
    }
    
}