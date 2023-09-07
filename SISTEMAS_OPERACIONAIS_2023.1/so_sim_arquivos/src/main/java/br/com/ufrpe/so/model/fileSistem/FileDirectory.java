package br.com.ufrpe.so.model.fileSistem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.so.exceptions.InstanceNotCompatibledException;

public class FileDirectory {

    private String name;
    private Integer size;
    private LocalDateTime creationDate;
    private List<Object> files = new ArrayList<Object>();

    public FileDirectory() {
    }

    public FileDirectory(String name, Integer size, LocalDateTime creationDate) {
        this.name = name;
        this.size = size;
        this.creationDate = creationDate;
    }

    public FileDirectory(String name) {
        this.name = name;
        this.size = 1;
        this.creationDate = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Object> getFiles() {
        return files;
    }

    public void setFiles(List<Object> files) {
        this.files = files;
    }

    public void addFile(Object file){
        if (file instanceof FileObject || file instanceof FileDirectory) {
            files.add(file);
        } else {
            throw new InstanceNotCompatibledException("The file must be a 'FileObject' or a 'FileDirectory'");
        }
    }

    public List<FileObject> getFileArchives(){
        return files.stream().filter(f -> (
            f instanceof FileObject
            )).toList()
            .stream().map(f -> {return (FileObject) f;}).toList();
    }
    
    public List<FileDirectory> getSubDirectories(){
        return files.stream().filter(f -> (
            f instanceof FileDirectory
            )).toList()
            .stream().map(f -> {return (FileDirectory) f;}).toList();
    }

    public Object findFileObjectByName(String targetName) {
        return findFileObjectByNameRecursive(this, targetName);
    }

    public Object findFileDirectoryByName(String targetName) {
        return findFileDirectoryByNameRecursive(this, targetName);
    }

    private Object findFileObjectByNameRecursive(FileDirectory directory, String targetName) {
        for (Object file : directory.getFiles()) {
            if (file instanceof FileObject && ((FileObject) file).getName().equalsIgnoreCase(targetName)) {
                return file;
            } else if (file instanceof FileDirectory) {
                Object foundFile = findFileObjectByNameRecursive((FileDirectory) file, targetName);
                if (foundFile != null) {
                    return foundFile;
                }
            }
        }
        return null;
    }

    private Object findFileDirectoryByNameRecursive(FileDirectory directory, String targetName) {
        if (directory.getName().equalsIgnoreCase(targetName)) {
            return this;
        }
        for (Object file : directory.getFiles()) {
            if (file instanceof FileDirectory && ((FileDirectory) file).getName().equalsIgnoreCase(targetName)) {
                return file;
            } else if (file instanceof FileDirectory) {
                Object foundFile = findFileDirectoryByNameRecursive((FileDirectory) file, targetName);
                if (foundFile != null) {
                    return foundFile;
                }
            }
        }
        return null;
    }

}