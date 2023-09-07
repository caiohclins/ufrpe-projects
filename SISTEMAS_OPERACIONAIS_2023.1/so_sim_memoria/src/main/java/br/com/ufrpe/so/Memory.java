package br.com.ufrpe.so;

import java.util.ArrayList;
import java.util.List;

public class Memory {

    private List<Process> size;

    public Memory(){
        this.size = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            this.size.add(null);
        }
    }

    public Memory(int size){
        this.size = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.size.add(null);
        }
    }

    public int getMemmorySize(){
        return this.size.size() - 1;
    }

    public void removeProcess(Process process) throws Exception{
        if (size.contains(process)) {
            for (Process p : size) {
                if (p != null && p.equals(process)) {
                    size.set(size.indexOf(p), null);
                }
            }
        } else {
            throw new Exception();
        }
       printMemoryFragmentation(process);
    }

    public void addProcess(int size, int position, Process process) throws Exception{
        for (int i = 0; i < size; i++) {
            if (this.size.get(position) != null){
                throw new Exception("Já existe um processo alocado nesse endereço de memória!");
            }
        }

        for (int i = 0; i < size; i++) {
            this.size.set(position, process);
            position++;
        }

        printMemoryFragmentation(process);
    }

    public void printMemoryFragmentation(Process process){
        System.out.println(String.format("- - - - - - Fragmentação de Memória após Adicionar ou Remover o Processo %s - - - - - -", process.getName()));
        int initialIndex = 0;
        int finalIndex = 0;
        int free = 0;
        int total = 0;

        for (Process p : size){
            if (p == null){
                free++;
                total++;
            } else {
                if (free != 0){
                    finalIndex = size.indexOf(p) - 1;
                    initialIndex = size.indexOf(p) - free;
                    System.out.println(String.format("[Espaço Disponível = %s | Indice Inicial = %s | Indice Final = %s]", free, initialIndex, finalIndex));
                    free = 0;
                    initialIndex = 0;
                    finalIndex = 0;
                } else {
                    continue;
                }
            }
        }

        if (free != 0){
            finalIndex = getMemmorySize();
            initialIndex = this.size.size() - free;
            System.out.println(String.format("[Espaço Disponível = %s | Indice Inicial = %s | Indice Final = %s]", free, initialIndex, finalIndex));
        } else {
            System.out.println(String.format("[Espaço Disponível = %s]", 0));
        }

        System.out.println(String.format("[Espaço Total Disponível = %s]", total));

    }

    public List<Process> getSize() {
        return size;
    }

}