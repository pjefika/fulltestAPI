/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.impl.both;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.mongo.model.entity.AbstractMongoEntity;
import java.util.Date;
import org.mongodb.morphia.annotations.Entity;

/**
 *
 * @author G0041775
 */
@Entity("validacao")
public class ValidacaoEntity extends AbstractMongoEntity {

    private ValidacaoResult valid;

    private EfikaCustomer cust;

    private Date dataInicio;

    private Date dataFim;

    public ValidacaoEntity() {
    }

    public ValidacaoResult getValid() {
        return valid;
    }

    public void setValid(ValidacaoResult valid) {
        this.valid = valid;
    }

    public EfikaCustomer getCust() {
        return cust;
    }

    public void setCust(EfikaCustomer cust) {
        this.cust = cust;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

}
