package br.com.ufrpe.so.operations;

import java.util.List;

import br.com.ufrpe.so.enumerator.FileExtension;
import br.com.ufrpe.so.exceptions.CommandNotRecognizedException;
import br.com.ufrpe.so.exceptions.FileSystemIntegrityException;
import br.com.ufrpe.so.exceptions.SystemExitException;
import br.com.ufrpe.so.model.fileSistem.FileDirectory;
import br.com.ufrpe.so.model.fileSistem.FileObject;
import br.com.ufrpe.so.model.fileSistem.FileSystemMemory;

public class FileSystemOperations {

    public static String commandExec(String[] args, String actualDirectory,  FileSystemMemory memory){

        String command = args.length > 0 ? args[0] : "";

        String directory = null;
        String fileName = null;
        String extension = null;

        FileDirectory fd = null;
        FileObject file = null;

        Object newObject = null;

        switch (command) {
            case "mkfile":

                if (args.length < 4) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                try {
                    directory = args[1];
                    fileName = args[2] + args[3];
                    extension = args[3];
                } catch (Exception e) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory) {
                        file = (FileObject) ((FileDirectory) obj).findFileObjectByName(fileName);
                        if (file != null){break;}
                    } else if (obj instanceof FileObject && ((FileObject) obj).getName().equals(fileName)){
                        file = (FileObject) obj;
                        if (file != null){break;}
                    }
                }

                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory) {
                        fd = (FileDirectory) ((FileDirectory) obj).findFileDirectoryByName(directory);
                        if (fd != null) {break;}
                    }
                }

                if (file != null){
                    throw new FileSystemIntegrityException(String.format("The file '%s' already exists!", fileName));
                } else if (fd != null) {
                    newObject = new FileObject(fileName, extension);
                    memory.addFile(newObject, FileExtension.getSizeByExtension(((FileObject) newObject).getExtension()));
                    fd.addFile(newObject);
                    actualDirectory = directory;
                } else {
                    throw new FileSystemIntegrityException(String.format("The directory '%s' not exists!", directory));
                }

                return actualDirectory;
            
            case "mkdir":
                if (args.length < 3) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                try {
                    directory = args[1];
                    fileName = args[2];
                } catch (Exception e) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory) {
                        fd = (FileDirectory) ((FileDirectory) obj).findFileDirectoryByName(directory);
                        if (fd != null) {break;}
                    }
                }

                if (fd != null) {
                    newObject = new FileDirectory(fileName);
                    memory.addFile(newObject, 1);
                    fd.addFile(newObject);
                    actualDirectory = directory;
                } else {
                    throw new FileSystemIntegrityException(String.format("The directory '%s' not exists!", directory));
                }

                return actualDirectory;
            
            case "dltfile":
                if (args.length < 2) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                try {
                    directory = args[1];
                } catch (Exception e) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory) {
                        file = (FileObject) ((FileDirectory) obj).findFileObjectByName(directory);
                        if (file != null){break;}
                    } else if (obj instanceof FileObject && ((FileObject) obj).getName().equals(directory)){
                        file = (FileObject) obj;
                        if (file != null){break;}
                    }
                }

                if (file != null) {
                    memory.removeFileObject(file);
                } else {
                    throw new FileSystemIntegrityException(String.format("The directory '%s' not exists!", directory));
                }

                return actualDirectory;
            
            case "dltdir":

                if (args.length < 2) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                try {
                    directory = args[1];
                } catch (Exception e) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory) {
                        fd = (FileDirectory) ((FileDirectory) obj).findFileDirectoryByName(directory);
                        if (fd != null) {break;}
                    }
                }

                if (fd != null) {
                    if (fd.getFiles().isEmpty()) {
                        memory.removeFileDirectory(fd);
                        actualDirectory = "home";
                    } else {
                        throw new FileSystemIntegrityException("The directory must be empty to be deleted.");
                    }
                } else {
                    throw new FileSystemIntegrityException(String.format("The directory '%s' not exists!", directory));
                }

                return actualDirectory;
            
            case "ls":
                
                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory && ((FileDirectory) obj).getName().equalsIgnoreCase(actualDirectory)) {
                        fd = ((FileDirectory) ((FileDirectory) obj).findFileDirectoryByName(actualDirectory));
                        printDirectoryStructure(fd, "");
                    }
                }

                return actualDirectory;

            case "cd":
                
                if (args.length < 2) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                try {
                    directory = args[1];
                } catch (Exception e) {
                    throw new FileSystemIntegrityException("The parsed args do not match the command");
                }

                for (Object obj : memory.getMemory()) {
                    if (obj instanceof FileDirectory) {
                        fd = (FileDirectory) ((FileDirectory) obj).findFileDirectoryByName(directory);
                        if (fd != null) {break;}
                    }
                }

                if (fd != null) {
                    actualDirectory = fd.getName();
                } else {
                    throw new FileSystemIntegrityException(String.format("The directory '%s' not exists!", directory));
                }

                return actualDirectory;

            case "showmem":
                
                memory.printMemoryFragmentation();

                return actualDirectory;
            
            case "exit":
                throw new SystemExitException("The system is shutting down, please wait.");
            
            case "":
                return actualDirectory;
            
            default:
            throw new CommandNotRecognizedException(String.format("The command '%s' is not recognized by the system.", command));
        }
    }

    public static void showUserFilesInterface(List<FileDirectory> directories){
        for (FileDirectory rootDirectory : directories) {
            printDirectoryStructure(rootDirectory, "");
        }
    }

    public static void printDirectoryStructure(FileDirectory directory, String indent) {
        if (directory != null){
            System.out.println(indent + "> " + directory.getName() + ":");
            for (FileObject file : directory.getFileArchives()) {
                System.out.println(indent + "      - " + file.getName());
            }
            for (FileDirectory subDirectory : directory.getSubDirectories()) {
                printDirectoryStructure(subDirectory, indent + "    ");
            }
        }
    }
}