package br.com.ufrpe.so;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import br.com.ufrpe.so.exceptions.SystemExitException;
import br.com.ufrpe.so.model.Memory;
import br.com.ufrpe.so.model.Process;
import br.com.ufrpe.so.model.fileSistem.FileDirectory;
import br.com.ufrpe.so.model.fileSistem.FileObject;
import br.com.ufrpe.so.model.fileSistem.FileSystemMemory;
import br.com.ufrpe.so.operations.FileSystemOperations;
import br.com.ufrpe.so.operations.MemoryOperations;
import br.com.ufrpe.so.operations.ProcessOperations;

public class Execution {

    public static void memorySim(){

        int nProcessos, tamanhoMemoria;

        List<Process> listaDeProcessos = new ArrayList<>();

        Scanner s = new Scanner(System.in);

        System.out.println("\n\n--------------------------------------------------------------------------------");
        System.out.printf("%44s\n", "Gerenciamento de Memória");
        System.out.println("--------------------------------------------------------------------------------\n\n");

        System.out.print("Digite o Tamanho da Memória a ser gerenciada: ");
        tamanhoMemoria = s.nextInt();

        Memory memory = new Memory(tamanhoMemoria);

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

        System.out.print("\nDigite o Tamanho do Processo:\n");

        // Criação dos processos
        for (int i = 0; i < nProcessos; i++) {
            System.out.print("Processo " + i + " = ");

            int tamanhoProcesso = s.nextInt();

            // Verifica se a Lista de Processos Prontos está cheia
            if (tamanhoProcesso > tamanhoMemoria) {
                System.out.println("A tamanho do processos nao pode ser maior que o tamanho da memória!! O 'Processo " + i +"'' não será criado.\n");
            } else {
                listaDeProcessos.add(new Process(i, "Processo " + i, tamanhoProcesso));
            }
        }

        boolean cond = true;
        int faltaMemoria = 0;
        while (cond){

            for (Process process : listaDeProcessos) {
                if (!memory.getSize().contains(process)){
                    if (!MemoryOperations.allocateFirstFit(memory, process)){
                        System.out.println("Falta de memória ao tentar inserir o '" + process.getName() +"'.");
                        faltaMemoria++;
                    }
                }
                if (faltaMemoria > 0) {
                    while (true){
                        try {
                            memory.removeProcess(listaDeProcessos.get((int)(Math.random() * nProcessos) + 0));
                            faltaMemoria = 0;
                            break;
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
            }
        }

        s.close();

    }

    // ------------------------------------------------------------------

    public static void processSim(){
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

        System.out.print("\nDigite o Tamanho do Processo:\n");

        // Criação dos processos
        for (int i = 0; i < nProcessos; i++) {
            System.out.print("Peocesso " + i + " = ");

            // Verifica se a Lista de Processos Prontos está cheia
            if (ProcessOperations.checaDisponibilidade(processosProntos)) {
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
                    ProcessOperations.moveProcessoFinalizado(processosProntos, processosFinalizados, process);
                    ProcessOperations.moveProcessoParaEntrar(processosProntos, processosParaEntrar);
                } else {

                    // Verifica se o limite de Quantum foi atingido
                    if (contaQuantum == quantum) {
                        ProcessOperations.moveProcessoParaFinalDaLista(processosProntos, process);
                        break;
                    }

                    ProcessOperations.incrementWaiting(processosProntos, process);
                    ProcessOperations.incrementTurnaround(processosProntos);
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

        System.out.println("\nTempo Medio de Espera = " + ProcessOperations.tempoMedioDeEspera(processosFinalizados));

        s.close();
    }

// ------------------------------------------------------------------

    public static void fileSystemSim(){

        int tamanhoMemoria, tamanhoBloco;

        Scanner s = new Scanner(System.in);

        System.out.println("\n\n--------------------------------------------------------------------------------");
        System.out.printf("%44s\n", "Sistema de Arquivos");
        System.out.println("--------------------------------------------------------------------------------\n\n");

        while (true){

            System.out.print("Digite o Tamanho da Memória do Sistema de Arquivos: ");
            tamanhoMemoria = s.nextInt();
            if (tamanhoMemoria < 10){
                System.out.println("O tamanho minimo de memoria permitido é '10'.");
                continue;
            }
            break;
        }

        while (true){

            System.out.print("Digite o Tamanho dos Blocos de Memória: ");
            tamanhoBloco = s.nextInt();
            if (tamanhoBloco > tamanhoMemoria){
                System.out.println("O tamanho do bloco nao pode ser maior que o tamanho da memoria.");
                continue;
            }
            break;
        }

        FileSystemMemory memory = new FileSystemMemory(tamanhoBloco, tamanhoMemoria);

        boolean cond = true;
        String actualDirectory = "home";
        String command = "";
        
        memory.addFile(new FileDirectory("home"), 1);

        s.nextLine();
        System.out.print("\nIniciando terminal, aguarde.");
        try {
            Thread.sleep(500l);
            System.out.print(".");
            Thread.sleep(500l);
            System.out.print(".\n\n");
        } catch (Exception ex) {
        }
        while (cond){
            try {

                System.out.print(String.format("[sys] %s >: ", actualDirectory));
                command = s.nextLine();
                actualDirectory = FileSystemOperations.commandExec(command.split(" "), actualDirectory, memory);

            } catch (SystemExitException e) {
                System.out.print(e.getMessage());
                try {
                    Thread.sleep(1000l);
                    System.out.print(".");
                    Thread.sleep(1000l);
                    System.out.print(".");
                } catch (Exception ex) {
                }
                cond = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        s.close();

    }

// ------------------------------------------------------------------

    public static void fileSystemSimTeste(){

        FileObject fileOne = new FileObject("FileOneName");
        FileObject fileTwo = new FileObject("FileTwoName");
        FileObject fileThree = new FileObject("FileThreeName");
        FileObject fileFour = new FileObject("FileFourName");
        FileObject fileFive = new FileObject("FileFiveName");
        FileObject fileSix = new FileObject("FileSixName");
        FileObject fileSeven = new FileObject("FileSevenName");
        FileObject fileEight = new FileObject("FileEightName");

        FileDirectory directoryFour = new FileDirectory("directoryFourName");
        directoryFour.addFile(fileSeven);
        directoryFour.addFile(fileEight);

        FileDirectory directoryOne = new FileDirectory("directoryOneName");
        directoryOne.addFile(fileOne);
        directoryOne.addFile(fileTwo);

        FileDirectory directoryTwo = new FileDirectory("directoryTwoName");
        directoryTwo.addFile(fileThree);
        directoryTwo.addFile(fileFour);
        
        directoryTwo.addFile(directoryFour);
        
        FileDirectory directoryThree = new FileDirectory("directoryThreeName");
        directoryThree.addFile(fileFive);
        directoryThree.addFile(fileSix);

        List<FileDirectory> list = Arrays.asList(directoryOne, directoryTwo, directoryThree);

        FileSystemOperations.showUserFilesInterface(list);

    }
}
