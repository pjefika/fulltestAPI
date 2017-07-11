/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl.gpon.zhone;

import br.net.gvt.efika.customer.EfikaCustomer;
import br.net.gvt.efika.customer.InventarioRede;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import model.dslam.consulta.EstadoDaPorta;
import model.dslam.consulta.Profile;
import model.dslam.consulta.VlanBanda;
import model.dslam.consulta.VlanMulticast;
import model.dslam.consulta.VlanVod;
import model.dslam.consulta.VlanVoip;
import model.dslam.consulta.gpon.AlarmesGpon;
import model.dslam.consulta.gpon.SerialOntGpon;
import model.dslam.consulta.gpon.TabelaParametrosGpon;
import model.dslam.velocidade.Velocidades;
import model.fulltest.operacional.CustomerMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author G0042204
 */
public class ZhoneGponDslamTest {

    public ZhoneGponDslamTest() {

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
        instance.desconectar();
    }
    private EfikaCustomer cl = CustomerMock.getCustomer("1630107429");
    ZhoneGponDslam instance = new ZhoneGponDslam(cl.getRede().getIpDslam());
    InventarioRede i = cl.getRede();

    /**
     * Test of getTabelaParametros method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetTabelaParametros() {
        try {
            TabelaParametrosGpon result = instance.getTabelaParametros(cl.getRede());
            assertTrue(result.getPotOlt() != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getSerialOnt method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetSerialOnt() {
        System.out.println("getSerialOnt");
        try {
            SerialOntGpon result = instance.getSerialOnt(i);
            assertTrue(!result.getSerial().isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Test of getEstadoDaPorta method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetEstadoDaPorta() throws Exception {
        System.out.println("getEstadoDaPorta");
        try {
            EstadoDaPorta result = instance.getEstadoDaPorta(i);
            assertTrue(result.getAdminState() != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getVlanBanda method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetVlanBanda() throws Exception {
        System.out.println("getVlanBanda");
        try {
            VlanBanda result = instance.getVlanBanda(i);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getVlanVoip method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetVlanVoip() throws Exception {
        System.out.println("getVlanVoip");
        try {
            VlanVoip result = instance.getVlanVoip(i);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getVlanVod method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetVlanVod() throws Exception {
        System.out.println("getVlanVod");
        try {
            VlanVod result = instance.getVlanVod(i);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getVlanMulticast method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetVlanMulticast() throws Exception {
        System.out.println("getVlanMulticast");
        try {
            VlanMulticast result = instance.getVlanMulticast(i);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getAlarmes method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetAlarmes() throws Exception {
        System.out.println("getAlarmes");
        try {
            AlarmesGpon result = instance.getAlarmes(i);
            assertTrue(result.getListAlarmes() != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getProfile method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetProfile() throws Exception {
        System.out.println("getProfile");
        try {
            Profile result = instance.getProfile(i);
            assertTrue(result.getProfileDown() != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of setOntToOlt method, of class ZhoneGponDslam.
     */
    @Test
    public void testSetOntToOlt() {
        System.out.println("setOntToOlt");
        try {
            SerialOntGpon s = new SerialOntGpon();
            s.setSerial("PACED8A7ED87");
            SerialOntGpon result = instance.setOntToOlt(i, s);
            assertTrue(result.getSerial().equalsIgnoreCase("PACED8A7ED87"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of setEstadoDaPorta method, of class ZhoneGponDslam.
     */
    @Test
    public void testSetEstadoDaPorta() {
        System.out.println("setEstadoDaPorta");
        try {
            EstadoDaPorta es = new EstadoDaPorta();
            es.setAdminState("up");
            EstadoDaPorta result = instance.setEstadoDaPorta(i, es);
            assertTrue(result.getAdminState().equalsIgnoreCase("up"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of createVlanBanda method, of class ZhoneGponDslam.
     */
    @Test
    public void testCreateVlanBanda() {
        System.out.println("createVlanBanda");
        try {
            VlanBanda result = instance.createVlanBanda(i, Velocidades.VEL_51200, Velocidades.VEL_25600);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of deleteVlanBanda method, of class ZhoneGponDslam.
     */
    @Test
    public void testDeleteVlanBanda() {
        System.out.println("deleteVlanBanda");
        try {
            instance.deleteVlanBanda(i);
            assertTrue(instance.getVlanBanda(i).getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of createVlanVoip method, of class ZhoneGponDslam.
     */
    @Test
    public void testCreateVlanVoip() {
        System.out.println("createVlanVoip");
        try {
            VlanVoip result = instance.createVlanVoip(i);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of deleteVlanVoip method, of class ZhoneGponDslam.
     */
    @Test
    public void testDeleteVlanVoip() {
        System.out.println("deleteVlanVoip");
        try {
            instance.deleteVlanVoip(i);
            assertTrue(instance.getVlanVoip(i).getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of createVlanVod method, of class ZhoneGponDslam.
     */
    @Test
    public void testCreateVlanVod() {
        System.out.println("createVlanVod");
        try {
            VlanVod result = instance.createVlanVod(i);
            assertTrue(!result.getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of deleteVlanVod method, of class ZhoneGponDslam.
     */
    @Test
    public void testDeleteVlanVod() {
        System.out.println("deleteVlanVod");
        try {
            instance.deleteVlanVod(i);
            assertTrue(instance.getVlanVod(i).getSvlan().equals(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of setProfileDown method, of class ZhoneGponDslam.
     */
    @Test
    public void testSetProfileDown() {
        System.out.println("setProfileDown");
        try {
//            Profile p = instance.setProfileDown(i, Velocidades.VEL_51200);
//            assertTrue(p!=null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of setProfileUp method, of class ZhoneGponDslam.
     */
    @Test
    public void testSetProfileUp() {
        System.out.println("setProfileUp");
        try {
            instance.setProfileUp(i, Velocidades.VEL_25600, Velocidades.VEL_12800);
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test of getSlotsAvailableOnts method, of class ZhoneGponDslam.
     */
    @Test
    public void testGetSlotsAvailableOnts() {
        System.out.println("getSlotsAvailableOnts");
        try {
            List<SerialOntGpon> ls = instance.getSlotsAvailableOnts(i);
            for (SerialOntGpon l : ls) {
                System.out.println(l.getSerial());
            }
            assertTrue(ls != null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testC() {
        //1630144309
        EfikaCustomer ec = CustomerMock.getCustomer("2135563108");
        System.out.println(ec.getRede().getVendorDslam());
        System.out.println(ec.getRede().getModeloDslam());
        System.out.println(ec.getRede().getIpDslam());
        System.out.println(ec.getRede().getSlot());
        System.out.println(ec.getRede().getPorta());
        System.out.println(ec.getRede().getLogica());
        System.out.println(ec.getRede().getCvLan());
        System.out.println(ec.getServicos().getVelDown());
        System.out.println(ec.getServicos().getVelUp());
        Gson g = new Gson();
        System.out.println(g.toJson(ec));
        assertTrue(ec != null);

    }
    
    /**
     * Test of castProfile method, of class KeymileGponDslam.
     */
    @Test
    public void testCastProfile() {
        System.out.println("castProfile");
        try {
            List<Profile> profiles = new ArrayList<>();
            for (Velocidades v : Velocidades.values()) {
                Profile p = instance.castProfile(v);
                profiles.add(p);
                System.out.println(v.name() + " Down ->" + p.getProfileDown());
                System.out.println(v.name() + " Up ->" + p.getProfileUp());
            }
            assertEquals(profiles.size(), Velocidades.values().length);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
