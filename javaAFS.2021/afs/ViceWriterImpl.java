// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la carga de un fichero
package afs;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.rmi.*;
import java.rmi.server.*;

// clase para escribir en archivo
public class ViceWriterImpl extends UnicastRemoteObject implements ViceWriter {

    private static final String AFSDir = "AFSDir/";// direccion en server
    private File file;
    private RandomAccessFile ra_file; // en escritura
    // constructor

    public ViceWriterImpl(final String fileName, String mode) throws RemoteException, FileNotFoundException {

        String full_path = AFSDir + fileName; // sacamos la ubicacion del archivo
        this.file = new File(full_path);
        this.ra_file = new RandomAccessFile(full_path, mode);// en escritura

    }

    // escribimos el bloque pasado en el archivo
    public void write(byte[] b) throws IOException {
        this.ra_file.write(b);
    }

    // cerramos el archivo
    public void close() throws RemoteException, IOException {
        this.ra_file.close();
    }

    /* Establece el size del archivo */
    public void setLength(long l) throws RemoteException, IOException {
        this.ra_file.setLength(l);
    }
}
