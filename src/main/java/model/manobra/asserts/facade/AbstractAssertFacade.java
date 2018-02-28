/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.manobra.asserts.facade;

import br.net.gvt.efika.efika_customer.model.customer.CustomerAssert;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author G0042204
 */
public abstract class AbstractAssertFacade implements Assertter {

    private List<CustomerAssert> as = new ArrayList<>();

    @Override
    public List<CustomerAssert> assertThese() throws Exception {
        afirmar();
        return as;
    }

    protected void adicionarAssert(CustomerAssert asserts) {
        if (asserts != null) {
            as.add(asserts);
        }
    }

    public abstract void afirmar() throws Exception;

}
