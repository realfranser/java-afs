// Implementación de la interfaz de servidor que define los métodos remotos
// para iniciar la carga y descarga de ficheros
package afs;

import java.io.FileNotFoundException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ViceImpl extends UnicastRemoteObject implements Vice {

  public ViceImpl() throws RemoteException {
  }

  public ViceReader download(String fileName, String mode) throws RemoteException, FileNotFoundException {
    ViceReader reader = new ViceReaderImpl(fileName, mode);
    return reader;
  }

  public ViceWriter upload(String fileName, String mode) throws RemoteException, FileNotFoundException {
    ViceWriter writer = new ViceWriterImpl(fileName, mode);
    return writer;
  }

}
