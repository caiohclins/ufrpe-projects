package br.com.ufrpe.so.enumerator;

import br.com.ufrpe.so.exceptions.ExtensionNotFoundException;

public enum FileExtension {
	DOT_BAT(".bat", 1),
	DOT_TXT(".txt", 1),
	DOT_JPG(".jpg", 2),
	DOT_MP4(".mp4", 5),
	DOT_MSI(".msi", 10);

	private String extension;
	private Integer size;

	private FileExtension(String extension, Integer size) {
		this.extension = extension;
		this.size = size;
	}

	public String getExtension() {
		return this.extension;
	}

	public Integer getSize() {
		return this.size;
	}
	
	public static Integer getSizeByExtension(String extension){
		for (FileExtension ext : FileExtension.values()) {
			if (ext.getExtension().equals(extension)) {
				return ext.getSize();
			}
		}
		throw new ExtensionNotFoundException(String.format("The extension '%s' is not suported.", extension));
	}

}
