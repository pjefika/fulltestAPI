/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.factory;

import dao.dslam.impl.AbstractDslam;
import dao.dslam.factory.exception.DslamNaoImplException;

/**
 *
 * @author G0042204
 */
public class DslamDAOFactory {

    public static AbstractDslam getInstance(String modelo, String ip) throws DslamNaoImplException {
        try {
            return DslamGponDAOFactory.getInstance(modelo, ip);
        } catch (DslamNaoImplException e) {
            return DslamMetalicoDAOFactory.getInstance(modelo, ip);
        }
    }

}