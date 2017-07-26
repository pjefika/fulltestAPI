/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.manobra.asserts.impl;

import br.net.gvt.efika.asserts.AssertsEnum;
import br.net.gvt.efika.asserts.EfikaAssertable;
import br.net.gvt.efika.customer.CustomerAssert;
import model.validacao.impl.manobra.ValidacaoResync50;

/**
 *
 * @author G0042204
 */
public class AssertResync50 implements EfikaAssertable {

    private final ValidacaoResync50 v;

    public AssertResync50(ValidacaoResync50 v) {
        this.v = v;
    }

    @Override
    public CustomerAssert claim() {
        try {
            return new CustomerAssert(AssertsEnum.RESYNC_MENOR_50, v.validar().getResultado());
        } catch (Exception e) {
            return new CustomerAssert(AssertsEnum.RESYNC_MENOR_50, Boolean.FALSE);
        }
    }

}