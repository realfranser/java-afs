// Implementación de la interfaz de servidor que define los métodos remotos
// para completar la descarga de un fichero
package afs;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;

public class ViceReaderImpl extends UnicastRemoteObject implements ViceReader {

    private static final String AFSDir = "AFSDir/";// dir
    private File file;
    private RandomAccessFile ra_file; // en modo lectura

    /* añada los parámetros que requiera */
    // constructor
    public ViceReaderImpl(String fileName, String mode) throws FileNotFoundException, RemoteException {

        String full_path = AFSDir + fileName;
        this.ra_file = new RandomAccessFile(full_path, mode); // y lo abrimos en modo lectura
        this.file = new File(full_path); // sacamos la dir

    }

    // leemos del archivo segun tam
    public byte[] read(int tam) throws RemoteException, IOException {

        long offset = this.ra_file.getFilePointer();
        long size = this.ra_file.length();
        byte buffer[] = null;

        // si ya hemos leido todo ,lo devolvemos
        if (size - offset <= 0)
            return buffer;

        buffer = ((offset + tam) > size) ? new byte[(int) (size - offset)] : new byte[tam];
        this.ra_file.read(buffer);
        return buffer;
    }

    // cerramos el archivo
    public void close() throws RemoteException, IOException {
        this.ra_file.close();
    }
}
