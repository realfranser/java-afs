// Implementación de la interfaz de cliente que define los métodos remotos
// para gestionar callbacks
package afs;

import java.io.File;
import java.rmi.*;
import java.rmi.server.*;

public class VenusCBImpl extends UnicastRemoteObject implements VenusCB {
    // constructor
    public VenusCBImpl() throws RemoteException {
    }

    public void invalidate(String file_name) throws RemoteException {
        return;
    }
}
