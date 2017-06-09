/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.factory;

import dao.dslam.impl.AbstractDslam;
import dao.dslam.factory.exception.DslamNaoImplException;
import dao.dslam.impl.metalico.keymile.KeymileMetalicoSuadDslam;
import dao.dslam.impl.metalico.keymile.KeymileMetalicoSuvd11;
import dao.dslam.impl.metalico.keymile.KeymileMetalicoSuvd3;
import dao.dslam.impl.metalico.zhone.ZhoneMetalicoComboDslam;
import dao.dslam.impl.metalico.zhone.ZhoneMetalicoMxkDslam;

/**
 *
 * @author G0042204
 */
public class DslamMetalicoDAOFactory{

    public static AbstractDslam getInstance(String modelo, String ip) throws DslamNaoImplException {

        if (modelo.equalsIgnoreCase("COMBOZH48")) {
            return new ZhoneMetalicoComboDslam();
        } else if (modelo.contains("MXK")) {
            return new ZhoneMetalicoMxkDslam();
        } else if (modelo.equalsIgnoreCase("SUVD3")) {
            return new KeymileMetalicoSuvd3();
        } else if (modelo.equalsIgnoreCase("SUVD11")) {
            return new KeymileMetalicoSuvd11();
        } else if (modelo.contains("SUAD")) {
            return new KeymileMetalicoSuadDslam();
        } else {
            throw new DslamNaoImplException();
        }
    }

}