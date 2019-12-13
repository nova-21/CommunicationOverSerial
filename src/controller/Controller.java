/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import View.View;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author David
 */
public class Controller implements ActionListener {
    private View view;
    
    public Controller(View view) {
        this.view = view;
        this.view.btnBinario.addActionListener(this);
        this.view.btnEnviar.addActionListener(this);
        this.view.chkError.addActionListener(this);
        this.view.chkPerdidaTrama.addActionListener(this);
    }
    
    public void init() {
        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view.btnBinario) {
            btnBinarioEvent();
        }
        
        if(e.getSource() == view.btnEnviar) {
            btnEnviarEvent();
        }
        
        if(e.getSource() == view.chkError) {
            chkErrorEvent();
        }
        
        if(e.getSource() == view.chkPerdidaTrama) {
            chkPerdidaTramaEvent();
        }
    }

    private void btnBinarioEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void btnEnviarEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void chkErrorEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void chkPerdidaTramaEvent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
