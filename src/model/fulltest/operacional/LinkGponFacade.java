/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.fulltest.operacional;

import br.net.gvt.efika.customer.EfikaCustomer;
import br.net.gvt.efika.customer.InventarioRede;
import dao.dslam.factory.exception.DslamNaoImplException;
import dao.dslam.factory.exception.FuncIndisponivelDslamException;
import exception.MetodoNaoImplementadoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.validacao.Validacao;
import model.validacao.realtime.gpon.ValidacaoRtEstadoAdmPorta;
import model.validacao.realtime.gpon.ValidacaoRtEstadoOperPorta;
import model.validacao.realtime.gpon.ValidacaoRtParametrosGpon;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author G0042204
 */
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true, value = {"cl", "dslam", "bateria"})
public class LinkGponFacade extends FullTestGponFacade {

    public LinkGponFacade(EfikaCustomer cl) throws DslamNaoImplException, FuncIndisponivelDslamException {
        super(cl);
    }

    @Override
    protected void preparaDslam() throws DslamNaoImplException {
        InventarioRede rede = cl.getRede();
    }

    @Override
    protected void preparaBateria() {
        bateria = new ArrayList<>();
        bateria.add(new ValidacaoRtEstadoAdmPorta(dslam, cl));
        bateria.add(new ValidacaoRtEstadoOperPorta(dslam, cl));
        bateria.add(new ValidacaoRtParametrosGpon(dslam, cl));
    }

    @Override
    public Boolean validar() throws Exception {
        valids = new ArrayList<>();
        dataInicio = Calendar.getInstance();
        for (Validacao v : bateria) {
            Boolean res;
            try {
                res = v.validar();
                valids.add(v);
            } catch (MetodoNaoImplementadoException e) {
                res = true;
            }

            if (!res) {
                dslam.desconectar();
                mensagem = "Mensagem Negativa.";
                dataFim = Calendar.getInstance();
                return false;
            }
        }

        dslam.desconectar();
        mensagem = "Mensagem Positiva";
        dataFim = Calendar.getInstance();
        return true;
    }

}
