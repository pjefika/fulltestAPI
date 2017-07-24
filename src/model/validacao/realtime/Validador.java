/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.validacao.realtime;

import br.net.gvt.efika.customer.EfikaCustomer;
import dao.dslam.factory.exception.FuncIndisponivelDslamException;
import dao.dslam.impl.AbstractDslam;
import dao.dslam.impl.ConsultaClienteInter;
import model.validacao.ValidFlow;
import model.validacao.ValidacaoResult;
import model.validacao.validador.Validator;
import model.validacao.impl.Validacao;

/**
 *
 * @author G0042204
 */
public abstract class Validador extends ValidFlow implements Validator {

    private AbstractDslam dslam;

    protected Validacao valid;

    protected EfikaCustomer cust;

    protected ConsultaClienteInter consulta;

    public Validador(AbstractDslam dslam, EfikaCustomer cust) {
        this.dslam = dslam;
        this.cust = cust;
    }

    @Override
    public ValidacaoResult validar() {
        try {
            iniciar();
            this.valid = consultar();
            processar();
            return new ValidacaoResult(valid.getNome(), valid.getMensagem(), valid.getResultado());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ValidacaoResult(valid.getNome(), ex.getMessage(), Boolean.TRUE);
        }
    }

    @Override
    protected void iniciar() throws FuncIndisponivelDslamException {
        if (this.getDslam() instanceof ConsultaClienteInter) {
            this.consulta = (ConsultaClienteInter) this.getDslam();
        } else {
            throw new FuncIndisponivelDslamException();
        }
    }

    @Override
    public Boolean checar() {
        return valid.validar().getResultado();
    }

    @Override
    protected void processar() {
        checar();
    }

    protected ValidacaoResult finalizar(String mensagem, Boolean resultado) {
        return new ValidacaoResult(valid.getNome(), valid.getNome(), valid.getResultado());
    }

    protected abstract Validacao consultar() throws Exception;

    public AbstractDslam getDslam() {
        return dslam;
    }

    public void setDslam(AbstractDslam dslam) {
        this.dslam = dslam;
    }

    public Validacao getValid() {
        return valid;
    }

    public void setValid(Validacao valid) {
        this.valid = valid;
    }

    public EfikaCustomer getCust() {
        return cust;
    }

    public void setCust(EfikaCustomer cust) {
        this.cust = cust;
    }

}
