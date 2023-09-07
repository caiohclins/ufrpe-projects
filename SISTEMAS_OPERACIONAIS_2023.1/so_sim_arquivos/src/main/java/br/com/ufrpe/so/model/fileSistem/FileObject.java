package br.com.ufrpe.so.model.fileSistem;

import java.time.LocalDateTime;

import br.com.ufrpe.so.enumerator.FileExtension;

public class FileObject {

    private String name;
    private Integer size;
    private LocalDateTime creationDate;
    private String extension;

    public FileObject() {
    }

    public FileObject(String name) {
        this.name = name;
    }

    public FileObject(String name, String extension) {
        this.name = name;
        this.size = FileExtension.getSizeByExtension(extension);
        this.creationDate = LocalDateTime.now();
        this.extension = extension;
    }

    public FileObject(String name, Integer size, LocalDateTime creationDate, String extension) {
        this.name = name;
        this.size = size;
        this.creationDate = creationDate;
        this.extension = extension;
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}