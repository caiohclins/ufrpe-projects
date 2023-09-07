package br.com.ufrpe;

import java.util.ArrayList;
import java.util.Scanner;

import br.com.ufrpe.model.DiskMemory;
import br.com.ufrpe.model.Look;
import br.com.ufrpe.model.Scan;

public class App {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        int minBlock = 0;
        int requestCount;
        int maxBlock;
        int headStart;

        System.out.println("\n\n--------------------------------------------------------------------------------");
        System.out.printf("%44s\n", "Gerenciamento de Entrada e Saída");
        System.out.println("--------------------------------------------------------------------------------\n\n");


        while (true){
            // Quantidade de processos a serem criados
            System.out.print("Digite a quantidade de blocos a serem gerenciados: ");
            maxBlock = s.nextInt();
 
             if (maxBlock < 10){
                 System.out.println("A quantidade de blocos deve ser no mínimo 10!\n");
                 continue;
             }
             break;
        }

        headStart = (int) maxBlock / 2;

        while (true){
             // Quantidade de processos a serem criados
             System.out.print("Digite a Quantidade de requisicoes a serem criadas (maximo " + maxBlock + ") = ");
             requestCount = s.nextInt();
 
             if (requestCount > maxBlock){
                 System.out.println("A quantidade de requisicoes nao pode ser maior que a quantidade de blocos!\n");
                 continue;
             }
             break;
        }

        DiskMemory disk = new DiskMemory(maxBlock, maxBlock);
        ArrayList<Integer> requests = new ArrayList<>();
        
        for (int i = 0; i < requestCount; i++) {
            int block = (int) (Math.random() * (maxBlock - minBlock)) + minBlock;
            while (requests.contains(block)) {
                block = (int) (Math.random() * (maxBlock - minBlock)) + minBlock;
            }
            requests.add(block);
        }

        for (Integer integer : requests) {
            try {
                disk.addFile(1, integer, integer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Scan scanScheduler = new Scan(headStart, requests, maxBlock);
        scanScheduler.simulate(disk);

        Look lookScheduler = new Look(headStart, requests, maxBlock);
        lookScheduler.simulate(disk);

        s.close();
    }
}
