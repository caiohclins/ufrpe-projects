package br.com.ufrpe.so;

import java.util.List;

public class Operations {

    public static boolean checaDisponibilidade(List<Process> processosProntos){
        return processosProntos.size() < 5;
    }

    public static void incrementWaiting(List<Process> processosProntos, Process processoAtual){
        for (Process process : processosProntos) {
            if (processoAtual.equals(process)){continue;}
            process.incrementWaiting();
        }
    }

    public static void incrementTurnaround(List<Process> processosProntos){
        for (Process process : processosProntos) {
            process.incrementTurnaround();
        }
    }

    public static void moveProcessoFinalizado(List<Process> processosProntos, List<Process> processosfinalizados, Process processoAtual){
        processosfinalizados.add(processoAtual);
        processosProntos.remove(processoAtual);
    }

    public static void moveProcessoParaEntrar(List<Process> processosProntos, List<Process> processosParaEntrar){
        if (checaDisponibilidade(processosProntos) && !processosParaEntrar.isEmpty()){
            processosProntos.add(processosParaEntrar.get(0));
            processosParaEntrar.remove(0);
        }
    }

    public static void moveProcessoParaFinalDaLista(List<Process> processosProntos, Process processoAtual){
        processosProntos.remove(processoAtual);
        processosProntos.add(processoAtual);
    }

    public static double tempoMedioDeEspera(List<Process> processosfinalizados){
        double tempoTotalDeEspera = 0.0;
        for (Process process : processosfinalizados) {
            tempoTotalDeEspera = tempoTotalDeEspera + process.getWaiting();
        }

        return tempoTotalDeEspera / (double) processosfinalizados.size();
    }
    
}