/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.impl;

import br.net.gvt.efika.customer.EfikaCustomer;
import model.dslam.consulta.VlanBanda;

/**
 *
 * @author G0042204
 */
public class ValidacaoVlanBanda extends ValidacaoValidavel {

    private final transient VlanBanda vlan;

    public ValidacaoVlanBanda(VlanBanda v, EfikaCustomer cust) {
        super(cust, v);
        this.vlan = v;
    }

    @Override
    protected String frasePositiva() {
        return "Vlan de Banda configurado corretamente.";
    }

    @Override
    protected String fraseNegativa() {
        return "Vlan de Banda configurado incorretamente.";
    }

}