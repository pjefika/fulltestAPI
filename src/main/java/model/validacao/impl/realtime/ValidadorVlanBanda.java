/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.impl.realtime;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import dao.dslam.impl.AbstractDslam;
import java.util.Locale;
import model.validacao.impl.both.Validacao;
import model.validacao.impl.both.ValidacaoVlanBanda;

/**
 *
 * @author G0042204
 */
public class ValidadorVlanBanda extends Validador {

    public ValidadorVlanBanda(AbstractDslam dslam, EfikaCustomer cust, Locale local) {
        super(dslam, cust, local);
        nome = "Vlan de Banda";
    }

    @Override
    protected Validacao consultar() throws Exception {
        return new ValidacaoVlanBanda(consulta.getVlanBanda(cust.getRede()), cust, bundle.getLocale());
    }
}
