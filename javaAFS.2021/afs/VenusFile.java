// Clase de cliente que define la interfaz a las aplicaciones.
// Proporciona la misma API que RandomAccessFile.
package afs;

import java.rmi.*;

import java.io.*;

public class VenusFile {

    private Venus venus;
    private static final String cache_dir = "Cache/";
    private boolean updated;
    private File f;
    private String mode;
    private int bloq_size;
    private RandomAccessFile ra_file;
    private String f_name;

    public VenusFile(Venus venus, String file_name, String mode)
            throws RemoteException, IOError, FileNotFoundException, IOException {

        String full_path = cache_dir + file_name;
        File file = new File(full_path);
        this.venus = venus;
        this.bloq_size = venus.getBlocksize();
        this.mode = mode;
        this.f_name = file_name;

        if (!file.exists()) {
            ViceReader vice_reader = venus.getVice().download(file_name, mode);
            /* Create a random access file in read/write mode given the full path */
            this.ra_file = new RandomAccessFile(full_path, "rw");
            /*
             * Write the content of the file given as parameter into the random access file
             * in chunks of "bloq size" which is an env variable stored in the venus obj
             */
            byte buffer[] = new byte[bloq_size];

            while ((buffer = vice_reader.read(this.bloq_size)) != null) {
                this.ra_file.write(buffer);
            }

            /* Close random access file and vice reader */
            vice_reader.close();
            this.ra_file.close();
        }

        this.ra_file = new RandomAccessFile(full_path, mode);
    }

    // operacion read
    public int read(byte[] b) throws RemoteException, IOException {
        return this.ra_file.read(b);
    }

    // operacion write
    public void write(byte[] b) throws RemoteException, IOException {
        this.ra_file.write(b);
        this.updated = true;
    }

    // operacion seek
    public void seek(long p) throws RemoteException, IOException {
        this.ra_file.seek(p);
    }

    // operacion setLength
    public void setLength(long l) throws RemoteException, IOException {
        this.ra_file.setLength(l);
        this.updated = true;
    }

    public void close() throws RemoteException, IOException {

        if (updated) {

            /* Si se ha esctito en el, proceder al reseteo de punteros */
            ViceWriter vice_writer = this.venus.getVice().upload(this.f_name, this.mode);
            /* Obtenemos el size y el desplazamiento y ponemos este a 0 */
            this.ra_file.seek(0);
            long size = ra_file.length();
            long offset = ra_file.getFilePointer();

            byte[] buffer;

            while (offset + this.bloq_size <= size) {
                buffer = new byte[this.bloq_size];
                this.ra_file.read(buffer);
                vice_writer.write(buffer);
                offset = this.ra_file.getFilePointer();
            }

            int rest = (int) (size - offset);
            if (rest > 0) {
                buffer = new byte[rest];
                this.ra_file.read(buffer);
                vice_writer.write(buffer);
            }

            vice_writer.close();
        }
        /* If the file hasn't been updated -> direct close */
        ra_file.close();
    }
}
