/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dslam.consulta;

import dao.dslam.ComandoDslam;

/**
 *
 * @author G0041775
 */
public interface ConsultaVlanBanda {
    
    public ComandoDslam getComandoConsultaVlanBanda();
    
    public Vlan getVlanBanda(ComandoDslam cmd);
    
}
