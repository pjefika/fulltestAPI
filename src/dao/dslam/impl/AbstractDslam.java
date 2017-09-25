/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl;

import model.dslam.credencial.Credencial;
import dao.dslam.impl.login.LoginDslamStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.dslam.velocidade.VelocidadeVendor;
import model.dslam.velocidade.Velocidades;

/**
 *
 * @author G0041775
 */
public abstract class AbstractDslam implements ConsultaClienteInter {

    private final String ipDslam;
    private Credencial credencial;
    public LoginDslamStrategy loginStrategy;
    private ConsultaDslam cd;
    protected List<VelocidadeVendor> vels;

    public AbstractDslam(String ipDslam, Credencial credencial, LoginDslamStrategy loginStrategy) {
        this.ipDslam = ipDslam;
        this.credencial = credencial;
        this.loginStrategy = loginStrategy;
        this.cd = new ConsultaDslam(this);
        this.vels = new ArrayList<>();
    }

    public void conectar() throws Exception {
        this.loginStrategy.conectar(this.getCd());
    }

    @Override
    public void desconectar() {
        try {
            this.cd.close();
        } catch (IOException ex) {
            Logger.getLogger(AbstractDslam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract List<VelocidadeVendor> obterVelocidadesVendor();

    protected Velocidades compare(String sintaxVendor) {
        for (VelocidadeVendor v : obterVelocidadesVendor()) {
            if (v.getSintax().equalsIgnoreCase(sintaxVendor)) {
                return v.getVel();
            }
        }
        return null;
    }

    public String getIpDslam() {
        return this.ipDslam;
    }

    public Credencial getCredencial() {
        return credencial;
    }

    public void setCredencial(Credencial credencial) {
        this.credencial = credencial;
    }

    public LoginDslamStrategy getLoginStrategy() {
        return loginStrategy;
    }

    public void setLoginStrategy(LoginDslamStrategy loginStrategy) {
        this.loginStrategy = loginStrategy;
    }

    public ConsultaDslam getCd() {
        return cd;
    }

    public void setCd(ConsultaDslam cd) {
        this.cd = cd;
    }

}
