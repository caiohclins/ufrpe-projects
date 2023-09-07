package br.com.ufrpe.so;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        // Iniciando as Veriaveis
        int nProcessos, quantum;

        // Apenas 5 processos por vez podem estar com o status "PRONTO"
        List<Process> processosProntos = new ArrayList<>(5);

        List<Process> processosParaEntrar = new ArrayList<>();

        List<Process> processosFinalizados = new ArrayList<>();

        Scanner s = new Scanner(System.in);

        System.out.println("\n\n--------------------------------------------------------------------------------");
        System.out.printf("%44s\n", "ROUND ROBIN");
        System.out.println("--------------------------------------------------------------------------------\n\n");

        System.out.print("Digite o Tempo de Quantum do Algoritmo: ");
        quantum = s.nextInt();

        while (true){

            // Quantidade de processos a serem criados
            System.out.print("Digite a Quantidade de Processos (maximo 10) = ");
            nProcessos = s.nextInt();

            if (nProcessos > 10){
                System.out.println("A quantidade de processos nao pode ser maior que 10!!\n");
                continue;
            }
            break;

        }

        System.out.print("\nDigite o Tempo de CPU do Processo:\n");

        // Criação dos processos
        for (int i = 0; i < nProcessos; i++) {
            System.out.print("Peocesso " + i + " = ");

            // Verifica se a Lista de Processos Prontos está cheia
            if (Operations.checaDisponibilidade(processosProntos)) {
                processosProntos.add(new Process(i, s.nextInt(), 0, 0));
            } else {
                processosParaEntrar.add(new Process(i, s.nextInt(), 0, 0));
            }
        }

        // Enquanto a Lista de Processos Prontos não estiver vazia
        while (!processosProntos.isEmpty()) {

            // Seleciona o primeiro processo da Lista de Processo Prontos
            Process process = processosProntos.get(0);
            int contaQuantum = 0;

            // Enquanto todos os Bursts do processo não forem executados
            while (process.getContaBursts() <= process.getCpuBurst()) {

                // Verifica se todos os Bursts foram executados
                if (process.getContaBursts() == process.getCpuBurst()) {
                    Operations.moveProcessoFinalizado(processosProntos, processosFinalizados, process);
                    Operations.moveProcessoParaEntrar(processosProntos, processosParaEntrar);
                } else {

                    // Verifica se o limite de Quantum foi atingido
                    if (contaQuantum == quantum) {
                        Operations.moveProcessoParaFinalDaLista(processosProntos, process);
                        break;
                    }

                    Operations.incrementWaiting(processosProntos, process);
                    Operations.incrementTurnaround(processosProntos);
                    contaQuantum++;
                }

                process.incrementContaBursts();

            }
        }

        // Imprime a tabela de estatística da execucao dos processos

        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-19s%-21s%-25s%-18s\n", "Process", "Burst Time", "Waiting Time", "Turnaround Time");
        System.out.println("--------------------------------------------------------------------------------");
        for (Process process : processosFinalizados) {
            System.out.printf("   %-21s%-22s%-26s%-15s\n", process.getPid(), process.getCpuBurst(),
                    process.getWaiting(), process.getTurnaround());
        }

        System.out.println("\nTempo Medio de Espera = " + Operations.tempoMedioDeEspera(processosFinalizados));

        s.close();

    }
}
