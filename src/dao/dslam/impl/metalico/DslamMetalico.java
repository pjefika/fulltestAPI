/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl.metalico;

import br.net.gvt.efika.customer.InventarioRede;
import dao.dslam.impl.AbstractDslam;
import dao.dslam.impl.AlteracaoMetalicoDefault;
import dao.dslam.impl.ConsultaMetalicoDefault;
import dao.dslam.impl.login.LoginDslamStrategy;
import model.dslam.consulta.DeviceMAC;
import model.dslam.credencial.Credencial;

/**
 *
 * @author G0041775
 */
public abstract class DslamMetalico extends AbstractDslam
        implements
        ConsultaMetalicoDefault, AlteracaoMetalicoDefault {

    public DslamMetalico(String ipDslam, Credencial credencial, LoginDslamStrategy loginStrategy) {
        super(ipDslam, credencial, loginStrategy);
    }

    @Override
    public DeviceMAC getDeviceMac(InventarioRede i) throws Exception {
        return null;
    }

//    @Override
//    protected List<VelocidadeVendor> obterVelocidadesUpVendor() {
//        return null;
//    }
//
//    @Override
//    protected List<VelocidadeVendor> obterVelocidadesDownVendor() {
//        return null;
//    }

}
