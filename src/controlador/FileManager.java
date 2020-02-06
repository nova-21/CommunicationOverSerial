/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListModel;
import vistas.Vista;


/**
 *
 * @author Jose Villalta
 */
public class FileManager {
    private static FileManager fm = null;
    private DefaultListModel listModel;
    private Vista vista;
    
    private FileManager(Vista vista) {
        this.vista = vista;
        this.listModel = new DefaultListModel();
    }
    
    public static FileManager getObject(Vista vista) {
        if(FileManager.fm == null) 
            fm = new FileManager(vista);        
        
        return fm;
    }
    
    public static FileManager getObject() {
        return fm;
    }   
    
    public void cargarArchivosRecibidos() {
        vista.jListArchivosRecibidos.removeAll();
        vista.jListArchivosRecibidos.clearSelection();
        
        String sFolder = Variables.CARPETA_RECIBIDOS;
        File folder = new File(sFolder);
        String[] list = folder.list();
        if(list != null && list.length > 0) {
            listModel.removeAllElements();
            for (String l : list) {
                listModel.addElement(l);
            }
            
            vista.jListArchivosRecibidos.setModel(listModel);
        }                
        
    }
    
    public void abrirArchivo() {                
        if(vista.jListArchivosRecibidos.getSelectedIndex() == -1) {
            return;
        }
        
        try {
            String url = Variables.CARPETA_RECIBIDOS + "\\" + vista.jListArchivosRecibidos.getSelectedValue();
            ProcessBuilder p = new ProcessBuilder();
            p.command("cmd.exe", "/c", url);
            p.start();
        } catch(IOException e) {
            System.out.println("Error al tratar de abrir el archivo\n" + e);
        }
    }
}
