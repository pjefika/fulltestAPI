/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.service;

import br.net.gvt.efika.efika_customer.model.customer.EfikaCustomer;
import br.net.gvt.efika.fulltest.model.fulltest.ValidacaoResult;
import br.net.gvt.efika.fulltest.model.telecom.config.ConfiguracaoPorta;
import br.net.gvt.efika.fulltest.model.telecom.properties.EstadoDaPorta;
import br.net.gvt.efika.util.json.JacksonMapper;
import dao.dslam.impl.AlteracaoClienteInter;
import dao.dslam.impl.ConsultaClienteInter;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author G0041775
 */
public class ConfigPortaServiceImplIT {

    public ConfigPortaServiceImplIT() {
    }
    private static ConfigPortaServiceImpl instance;
    private static EfikaCustomer cust;

    @BeforeClass
    public static void setUpClass() {
        try {
            cust = (EfikaCustomer) new JacksonMapper(EfikaCustomer.class).deserialize("{"
                    + "   \"designador\":\"SPO-813XR2ENAY-013\","
                    + "   \"instancia\":\"110007861679801\","
                    + "   \"designadorAcesso\":\"SPO-26398039-069\","
                    + "   \"designadorTv\":\"TV-SPO-813XR2ENB3-050\","
                    + "   \"rede\":{"
                    + "      \"tipo\":\"GPON\","
                    + "      \"origem\":\"ONLINE\","
                    + "      \"planta\":\"VIVO1\","
                    + "      \"ipDslam\":\"10.58.237.22\","
                    + "      \"vendorDslam\":\"ALCATEL\","
                    + "      \"modeloDslam\":\"7302 ISAM FTTU\","
                    + "      \"idOnt\":\"0002971349\","
                    + "      \"terminal\":\"1178616798\","
                    + "      \"ipMulticast\":null,"
                    + "      \"nrc\":null,"
                    + "      \"slot\":6,"
                    + "      \"porta\":7,"
                    + "      \"sequencial\":41,"
                    + "      \"logica\":41,"
                    + "      \"rin\":64,"
                    + "      \"vlanVoip\":3016,"
                    + "      \"vlanVod\":3013,"
                    + "      \"vlanMulticast\":3013,"
                    + "      \"cvlan\":1837,"
                    + "      \"bhs\":true"
                    + "   },"
                    + "   \"redeExterna\":{"
                    + "      \"tipo\":null,"
                    + "      \"origem\":null,"
                    + "      \"planta\":null,"
                    + "      \"splitter1n\":null,"
                    + "      \"splitter2n\":null,"
                    + "      \"caboAlim\":null,"
                    + "      \"fibra1n\":null,"
                    + "      \"fibra2n\":null"
                    + "   },"
                    + "   \"servicos\":{"
                    + "      \"origem\":null,"
                    + "      \"velDown\":102400,"
                    + "      \"velUp\":51200,"
                    + "      \"tipoTv\":\"IPTV\","
                    + "      \"tipoLinha\":\"SIP\""
                    + "   },"
                    + "   \"linha\":{"
                    + "      \"tipo\":null,"
                    + "      \"dn\":null,"
                    + "      \"central\":null"
                    + "   },"
                    + "   \"radius\":{"
                    + "      \"status\":null,"
                    + "      \"armario\":null,"
                    + "      \"rin\":null,"
                    + "      \"velocidade\":null,"
                    + "      \"ipFixo\":null,"
                    + "      \"profile\":null,"
                    + "      \"porta\":null,"
                    + "      \"isIpFixo\":null"
                    + "   },"
                    + "   \"asserts\":["
                    + "      {"
                    + "         \"asserts\":\"CIRCUITO_ATIVO\","
                    + "         \"value\":true,"
                    + "         \"creationDate\":1540323959747"
                    + "      },"
                    + "      {"
                    + "         \"asserts\":\"DIVERGENCIA_TBS_RADIUS\","
                    + "         \"value\":false,"
                    + "         \"creationDate\":1540323959747"
                    + "      },"
                    + "      {"
                    + "         \"asserts\":\"HAS_BLOQUEIO_RADIUS\","
                    + "         \"value\":false,"
                    + "         \"creationDate\":1540323959747"
                    + "      }"
                    + "   ],"
                    + "   \"eventos\":["
                    + ""
                    + "   ]"
                    + "}");
        } catch (Exception e) {

        }
        instance = new ConfigPortaServiceImpl(cust);

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
//        try {
//            cust = (EfikaCustomer) new JacksonMapper(EfikaCustomer.class).deserialize("{\"designador\":\"FNS-8148UP9JME-013\",\"instancia\":\"4830309808\",\"designadorAcesso\":\"FNS-17997319-069\",\"designadorTv\":null,\"rede\":{\"tipo\":\"GPON\",\"origem\":\"ONLINE\",\"planta\":\"VIVO2\",\"ipDslam\":\"10.141.76.150\",\"vendorDslam\":\"ALCATEL\",\"modeloDslam\":\"GPON_CARD\",\"idOnt\":null,\"terminal\":null,\"ipMulticast\":null,\"nrc\":null,\"slot\":7,\"porta\":3,\"sequencial\":2225,\"logica\":49,\"rin\":242,\"vlanVoip\":1242,\"vlanVod\":3242,\"vlanMulticast\":4000,\"cvlan\":2325,\"bhs\":null},\"redeExterna\":{\"tipo\":null,\"origem\":null,\"planta\":null,\"splitter1n\":null,\"splitter2n\":null,\"caboAlim\":null,\"fibra1n\":null,\"fibra2n\":null},\"servicos\":{\"origem\":null,\"velDown\":102400,\"velUp\":51200,\"tipoTv\":null,\"tipoLinha\":\"SIP\"},\"linha\":{\"tipo\":\"IMS\",\"dn\":\"4830309808\",\"central\":\"PRCTA_VMS02\"},\"radius\":{\"status\":\"ATIVO\",\"armario\":\"SCFNS_G1I13\",\"rin\":\"242\",\"velocidade\":\"102400 - 51200\",\"ipFixo\":\"NAO ENCONTROU\",\"profile\":\"r51200b102400 op:FNS-8148UP9JME-013\",\"porta\":\"2225\",\"isIpFixo\":false},\"asserts\":[{\"asserts\":\"DIVERGENCIA_TBS_RADIUS\",\"value\":false,\"creationDate\":1525097434471},{\"asserts\":\"CIRCUITO_ATIVO\",\"value\":true,\"creationDate\":1525097434471},{\"asserts\":\"HAS_BLOQUEIO_RADIUS\",\"value\":false,\"creationDate\":1525097434471}],\"eventos\":[]}");
//        } catch (Exception e) {
//        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of consulta method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testConsulta() throws Exception {
        System.out.println("consulta");
        ConfigPortaServiceImpl instance = null;
        ConsultaClienteInter expResult = null;
        ConsultaClienteInter result = instance.consulta();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of alteracao method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testAlteracao() throws Exception {
        System.out.println("alteracao");
        ConfigPortaServiceImpl instance = null;
        AlteracaoClienteInter expResult = null;
        AlteracaoClienteInter result = instance.alteracao();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of consultar method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testConsultar() throws Exception {
        System.out.println("consultar");
        ConfigPortaServiceImpl instance = null;
        ConfiguracaoPorta expResult = null;
        ConfiguracaoPorta result = instance.consultar();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setterEstadoDaPorta method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testSetterEstadoDaPorta() throws Exception {
        System.out.println("setterEstadoDaPorta");
        EstadoDaPorta est = new EstadoDaPorta(true);

        ValidacaoResult result = instance.setterEstadoDaPorta(est);
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));

    }

    /**
     * Test of setterVlanBanda method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testSetterVlanBanda() throws Exception {
        System.out.println("setterVlanBanda");
        ConfigPortaServiceImpl instance = null;
        ValidacaoResult expResult = null;
        ValidacaoResult result = instance.setterVlanBanda();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setterVlanVoip method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testSetterVlanVoip() throws Exception {
        System.out.println("setterVlanVoip");
        ValidacaoResult result = instance.setterVlanVoip();
        System.out.println("");
    }

    /**
     * Test of setterVlanVod method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testSetterVlanVod() throws Exception {
        System.out.println("setterVlanVod");

        ValidacaoResult result = instance.setterVlanVod();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of setterVlanMulticast method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testSetterVlanMulticast() throws Exception {
        System.out.println("setterVlanMulticast");
        ConfigPortaServiceImpl instance = null;
        ValidacaoResult expResult = null;
        ValidacaoResult result = instance.setterVlanMulticast();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resetIptvStatistics method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testResetIptvStatistics() throws Exception {
        System.out.println("resetIptvStatistics");
        ConfigPortaServiceImpl instance = null;
        instance.resetIptvStatistics();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIptvVlans method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testGetIptvVlans() throws Exception {
        System.out.println("getIptvVlans");
        ConfigPortaServiceImpl instance = null;
        List<ValidacaoResult> expResult = null;
        List<ValidacaoResult> result = instance.getIptvVlans();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isManageable method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testIsManageable() throws Exception {
        System.out.println("isManageable");
        ConfigPortaServiceImpl instance = new ConfigPortaServiceImpl(cust);
        Boolean result = instance.isManageable();
        System.out.println(new JacksonMapper(Boolean.class).serialize(result));
    }

    /**
     * Test of corretorEstadoDaPorta method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testCorretorEstadoDaPorta() throws Exception {
        System.out.println("corretorEstadoDaPorta");
        ValidacaoResult result = instance.corretorEstadoDaPorta();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of corretorVlanBanda method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testCorretorVlanBanda() throws Exception {
        System.out.println("corretorVlanBanda");
        ValidacaoResult result = instance.corretorVlanBanda();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of corretorProfile method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testCorretorProfile() throws Exception {
        System.out.println("corretorProfile");
        ValidacaoResult result = instance.corretorProfile();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of corretorVlansVideo method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testCorretorVlansVideo() throws Exception {
        System.out.println("corretorVlansVideo");
        ValidacaoResult result = instance.corretorVlansVideo();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of corretorVlanVoIP method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testCorretorVlanVoIP() throws Exception {
        System.out.println("corretorVlanVoIP");
        ValidacaoResult result = instance.corretorVlanVoIP();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of validadorParametros method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testValidadorParametros() throws Exception {
        System.out.println("validadorParametros");
        ValidacaoResult result = instance.validadorParametros();
        System.out.println(new JacksonMapper(ValidacaoResult.class).serialize(result));
    }

    /**
     * Test of setterVlans method, of class ConfigPortaServiceImpl.
     */
    @Test
    public void testSetterVlans() throws Exception {
        System.out.println("setterVlans");

        List<ValidacaoResult> result = instance.setterVlans();
        System.out.println(new JacksonMapper(List.class).serialize(result));

    }

}
