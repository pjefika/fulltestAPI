/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.fulltest.validacao;

import dao.dslam.impl.AbstractDslam;

/**
 *
 * @author G0041775
 */
public interface Validador {
    
    public Boolean validar(AbstractDslam ds);
    
}
