/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 *
 * @author David
 */
public class ArrayBytesFile {

    // Attributes

    private File file;
    private byte[] bFile;

    // Constructors

    public ArrayBytesFile() {
        this.file = null;
        this.bFile = null;
    }

    public ArrayBytesFile (File file) {
        this.file = file;
        fileToBFile();
    }

    public ArrayBytesFile (String filePath, byte[] bFile) {
        this.file = new File(filePath);
        this.bFile = bFile;
    }

    // Public methods

    public File getFile() {
        return this.file;
    }

    public byte[] getBFile() {
        return this.bFile;
    }

    public void setFile (File file) {
        this.file = file;
    }

    public void setBfile (byte[] bFile) {
        this.bFile = bFile;
    }

    public void writeFile() {

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(this.file);
            outputStream.write(this.bFile);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Private methods

    private void fileToBFile() {
        this.bFile = new byte[(int) this.file.length()];

        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(this.file);
            inputStream.read(bFile);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
