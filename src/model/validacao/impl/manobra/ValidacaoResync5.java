/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.impl.manobra;

import model.validacao.impl.*;
import model.dslam.consulta.metalico.TabelaRedeMetalico;

/**
 *
 * @author G0042204
 */
public class ValidacaoResync5 extends Validacao {

    private final TabelaRedeMetalico tab;

    public ValidacaoResync5(TabelaRedeMetalico tab) {
        super("Resync < 5?");
        this.tab = tab;
    }

    @Override
    public Boolean checar() {
        return tab.resync5();
    }

    @Override
    protected String frasePositiva() {
        return "frasePositiva - resync5";
    }

    @Override
    protected String fraseNegativa() {
        return "fraseNegativa - resync5";
    }

}
