/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.tdd.consultas.metalico.keymile.SUAD;

import java.math.BigInteger;
import model.dslam.consulta.VlanBanda;
import dao.dslam.impl.metalico.keymile.KeymileMetalicoDslam;
import dao.dslam.impl.metalico.keymile.KeymileMetalicoSuadDslam;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author G0041775
 */
public class VlanBandaJUnitTest {

    public VlanBandaJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void consultarVlanBanda() {
        try {

            KeymileMetalicoDslam d = new KeymileMetalicoSuadDslam();
            d.setIpDslam("10.141.228.1");
            d.setSlot(new BigInteger("3"));
            d.setPorta(new BigInteger("20"));
            VlanBanda vb = d.getVlanBanda();

            System.out.println("Cvlan " + vb.getCvlan());
            System.out.println("P100 " + vb.getP100());
           

            d.desconectar();

            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
