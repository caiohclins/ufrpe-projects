package br.com.ufrpe.model;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.exceptions.FullMemoryException;

public class DiskMemory {

    private Integer blockSize;
    private List<Object> memory;

    public DiskMemory(){
        this.memory = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            this.memory.add(null);
        }
    }

    public DiskMemory(int blockSize, int memorySize){
        this.blockSize = blockSize;
        this.memory = new ArrayList<>(memorySize);
        for (int i = 0; i < memorySize; i++) {
            this.memory.add(null);
        }
    }

    public int getBlockSize(){
        return this.blockSize;
    }

    public int getMemmorySize(){
        return this.memory.size() - 1;
    }

    public void removeFile(Object file) throws Exception{
        if (memory.contains(file)) {
            for (Object obj : memory) {
                if (obj != null && obj.equals(file)) {
                    memory.set(memory.indexOf(obj), null);
                }
            }
        } else {
            throw new Exception();
        }
    }

    public void addFile(Object file, int fileSize) {

        int freeSpace = memory.stream().filter(f -> f == null).toList().size();

        if (fileSize <= freeSpace) {
            int index = 0;

            for (Object obj : memory) {
                if (fileSize == 0){break;}
                if (obj == null) {
                    memory.set(index, file);
                    fileSize--;
                }
                index++;
            }
        } else {
            throw new FullMemoryException("Have no more space at FileSystem Memory! Delete some file.");
        }
    }

    public void addFile(int size, int position, Object file) throws Exception{
        for (int i = 0; i < size; i++) {
            if (this.memory.get(position) != null){
                throw new Exception("Já existe um Arquivo alocado nesse endereço de memória!");
            }
        }

        for (int i = 0; i < size; i++) {
            this.memory.set(position, file);
            position++;
        }
    }

    // public void printMemoryFragmentation() {
    //     System.out.println("\n- - - - - - Fragmentação de Memória - - - - - -");

    //     int total = 0;
    //     int totalBlocks = memory.size() / blockSize;
    //     int rest = memory.size() % blockSize;
        
    //     for (int block = 0; rest > 0 ? block < totalBlocks + 1 : block < totalBlocks; block++) {
    //         System.out.println("Bloco " + block + ":");
    //         try {
    //             for (int index = block * blockSize; index < (block + 1) * blockSize; index++) {
    //             Object obj = memory.get(index);
    //             if (obj == null) {
    //                 total++;
    //                 System.out.println(String.format("    Índice %d: Livre", index));
    //             } else {
    //                 if (obj instanceof FileDirectory) {
    //                     System.out.println(String.format("    Índice %d: [dir] > %s", index, ((FileDirectory) obj).getName()));
    //                 } else if (obj instanceof FileObject) {
    //                     System.out.println(String.format("    Índice %d: [file] - %s", index, ((FileObject) obj).getName()));
    //                 }
    //             }
    //         }
    //         } catch (Exception e) {
    //         }
    //     }

    //     System.out.println(String.format("[Espaço Total Disponível = %s]", total));
    //     System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - -\n");
    // }

    // public void removeFileObject(FileObject obj){

    //     for (int i = 0; i < memory.size(); i++) {
    //         if (memory.get(i) != null && memory.get(i) instanceof FileObject) {
    //             if (memory.get(i).equals(obj)) {
    //                 memory.set(i, null);
    //             }
    //         } else if (memory.get(i) != null && memory.get(i) instanceof FileDirectory){
    //             if (((FileDirectory) memory.get(i)).getFiles().contains(obj)){
    //                 ((FileDirectory) memory.get(i)).getFiles().remove(obj);
    //             }
    //         }
    //     }
            
    // }

    // public void removeFileDirectory(FileDirectory obj){

    //     for (int i = 0; i < memory.size(); i++) {
    //          if (memory.get(i) != null && memory.get(i) instanceof FileDirectory){
    //             if (memory.get(i).equals(obj)){
    //                 memory.set(i, null);
    //             } else {
    //                 if (((FileDirectory) memory.get(i)).getFiles().contains(obj)){
    //                     ((FileDirectory) memory.get(i)).getFiles().remove(obj);
    //                 }
    //             }
    //         }
    //     }
            
    // }

    public List<Object> getMemory() {
        return memory;
    }

}