/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl.gpon.huawei;

import br.net.gvt.efika.efika_customer.model.customer.InventarioRede;
import br.net.gvt.efika.fulltest.exception.FalhaLoginDslamException;
import br.net.gvt.efika.fulltest.exception.FuncIndisponivelDslamException;
import br.net.gvt.efika.fulltest.model.telecom.config.ComandoDslam;
import br.net.gvt.efika.fulltest.model.telecom.properties.DeviceMAC;
import br.net.gvt.efika.fulltest.model.telecom.properties.EstadoDaPorta;
import br.net.gvt.efika.fulltest.model.telecom.properties.Porta;
import br.net.gvt.efika.fulltest.model.telecom.properties.Profile;
import br.net.gvt.efika.fulltest.model.telecom.properties.ProfileVivo1;
import br.net.gvt.efika.fulltest.model.telecom.properties.ReConexao;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanBanda;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanBandaVivo1Huawei;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanMulticast;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanVod;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanVodVivo1Huawei;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanVoip;
import br.net.gvt.efika.fulltest.model.telecom.properties.VlanVoipVivo1Huawei;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.AlarmesGpon;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.PortaPON;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.SerialOntGpon;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.ServicePort;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.TabelaParametrosGpon;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.TabelaParametrosGponBasic;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.TabelaParametrosGponBasicVivo1;
import br.net.gvt.efika.fulltest.model.telecom.properties.gpon.TabelaParametrosGponVivo1;
import br.net.gvt.efika.fulltest.model.telecom.velocidade.VelocidadeVendor;
import br.net.gvt.efika.fulltest.model.telecom.velocidade.Velocidades;
import dao.dslam.impl.gpon.DslamGponVivo1;
import dao.dslam.impl.login.LoginComJump;
import dao.dslam.impl.retorno.TratativaRetornoUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.dslam.credencial.Credencial;

/**
 * MA5600T
 *
 * @author G0041775
 */
public class HuaweiGponDslamVivo1 extends DslamGponVivo1 {

    private transient ServicePort spBanda;
    private transient ServicePort spVoip;
    private transient ServicePort spIptv;
    private transient EstadoDaPorta estadoDaPorta;
    private transient SerialOntGpon serial;
    private transient Integer gemportBanda, gemportIptv, gemportVoip;
    private transient VlanBandaVivo1Huawei vlanBanda;
    private transient VlanVoipVivo1Huawei vlanVoip;
    private transient VlanVodVivo1Huawei vlanVod;
    private transient Profile profile;

    public HuaweiGponDslamVivo1(String ipDslam) {
        super(ipDslam, Credencial.VIVO1, new LoginComJump());
    }

    @Override
    public void conectar() throws Exception {
        super.conectar();
    }

    @Override
    public void enableCommandsInDslam() throws Exception {
        ComandoDslam cmd = this.getCd().consulta(this.getComandoEnableConfig());
        if (!cmd.getBlob().contains("Huawei")) {
            throw new FalhaLoginDslamException();
        }
    }

    protected ComandoDslam getComandoEnableConfig() {
        return new ComandoDslam("enable", 500, "config", 500, "mmi-mode original-output");
    }

    protected void setServicePorts(InventarioRede i) throws Exception {
        setGemports(i);
        ComandoDslam cmd = getCd().consulta(getComandoGetServicePorts(i));
        List<String> retorno = cmd.getRetorno();
        List<List<String>> tabServs = new ArrayList<>();
        int start = 0;
        int end = retorno.size();
        for (int j = 0; j < end; j++) {
            if (retorno.get(j).contains(getComandoGetServicePorts(i).getSintax())) {
                start = j;
            }
            if (retorno.get(j).contains("Total :") && start != 0) {
                end = j;
            }
        }
        for (int n = start; n < end; n++) {
            if (retorno.get(n).contains("gpon")) {
                List<String> allMatches = new ArrayList<>();
                Matcher m = Pattern.compile("\\d+").matcher(retorno.get(n));
                while (m.find()) {
                    allMatches.add(m.group());
                }

                String[] porEspaco = retorno.get(n).split(" ");
                allMatches.add(porEspaco[porEspaco.length - 1].trim());
                tabServs.add(allMatches);

            }
        }

        tabServs.forEach((t) -> {
            if (t.get(6).equalsIgnoreCase("10")) {
                spBanda = new ServicePort();
                spBanda.setFlowPara(new Integer(t.get(6)));
                spBanda.setIndex(new Integer(t.get(0)));
                spBanda.setRx(new Integer(t.get(7)));
                spBanda.setState(t.get(9).equalsIgnoreCase("up"));
                spBanda.setTx(new Integer(t.get(8)));
                spBanda.setVlanId(new Integer(t.get(1)));
                spBanda.setVpi(new Integer(t.get(5)));
                Integer cvlan = 0;
                ComandoDslam cmdb = null;
                try {
                    cmdb = getCd().consulta(getComandoGetVlanBanda(spBanda.getIndex()));
                    cvlan = new Integer(TratativaRetornoUtil.tratHuawei(cmdb.getRetorno(), "Label"));
                } catch (Exception e) {
                }

                vlanBanda = new VlanBandaVivo1Huawei();
                vlanBanda.addInteracao(cmd);
                vlanBanda.setGemport(spBanda.getVpi());
                vlanBanda.setSvlan(spBanda.getVlanId());
                vlanBanda.setCvlan(cvlan);
                if (cmdb != null) {
                    vlanBanda.addInteracao(cmdb);
                }

                profile = new ProfileVivo1();
                profile.setDown(compare(spBanda.getRx().toString(), Boolean.TRUE));
                profile.setUp(compare(spBanda.getTx().toString(), Boolean.FALSE));
                profile.setProfileDown(spBanda.getRx().toString());
                profile.setProfileUp(spBanda.getTx().toString());
                profile.addInteracao(cmd);
            }
            if (t.get(6).equalsIgnoreCase("20")) {
                spIptv = new ServicePort();
                spIptv.setFlowPara(new Integer(t.get(6)));
                spIptv.setIndex(new Integer(t.get(0)));
                spIptv.setRx(new Integer(t.get(7)));
                spIptv.setState(t.get(9).equalsIgnoreCase("up"));
                spIptv.setTx(new Integer(t.get(8)));
                spIptv.setVlanId(new Integer(t.get(1)));
                spIptv.setVpi(new Integer(t.get(5)));
                vlanVod = new VlanVodVivo1Huawei();
                vlanVod.setGemport(spIptv.getVpi());
                vlanVod.setSvlan(spIptv.getVlanId());
                vlanVod.addInteracao(cmd);
            }
            if (t.get(6).equalsIgnoreCase("30")) {
                spVoip = new ServicePort();
                spVoip.setFlowPara(new Integer(t.get(6)));
                spVoip.setIndex(new Integer(t.get(0)));
                spVoip.setRx(new Integer(t.get(7)));
                spVoip.setState(t.get(9).equalsIgnoreCase("up"));
                spVoip.setTx(new Integer(t.get(8)));
                spVoip.setVlanId(new Integer(t.get(1)));
                spVoip.setVpi(new Integer(t.get(5)));
                vlanVoip = new VlanVoipVivo1Huawei();
                vlanVoip.setGemport(spVoip.getVpi());
                vlanVoip.setSvlan(spVoip.getVlanId());
                vlanVoip.addInteracao(cmd);
            }
        });
        if (vlanBanda == null) {
            vlanBanda = new VlanBandaVivo1Huawei();
            vlanBanda.addInteracao(cmd);
            vlanBanda.setGemport(0);
            vlanBanda.setSvlan(0);
            vlanBanda.setCvlan(0);
        }

        if (vlanVod == null) {
            vlanVod = new VlanVodVivo1Huawei();
            vlanVod.setGemport(0);
            vlanVod.setSvlan(0);
            vlanVod.addInteracao(cmd);
        }

        if (vlanVoip == null) {
            vlanVoip = new VlanVoipVivo1Huawei();
            vlanVoip.setGemport(0);
            vlanVoip.setSvlan(0);
            vlanVoip.addInteracao(cmd);
        }
    }

    protected void setGemports(InventarioRede i) {
        gemportBanda = i.getLogica() + 128;
        gemportIptv = i.getLogica() + 256;
        gemportVoip = i.getLogica() + 384;
    }

    Boolean tudoDestruido = false;

    protected void tabelaEstadoDaPorta(InventarioRede i) throws Exception {
        setGemports(i);
        ComandoDslam cmd = getCd().consulta(getComandoGetEstadoDaPorta(i));
        List<String> resp = cmd.getRetorno();
        estadoDaPorta = new EstadoDaPorta();
        serial = new SerialOntGpon();
        if (!cmd.getBlob().contains("Control flag")) {
            estadoDaPorta.setAdminState(false);
            estadoDaPorta.setOperState(false);
            serial.setIdOnt("0");
            serial.setPorta(0);
            serial.setSlot(0);
            serial.setSerial("0");
            tudoDestruido = true;
//            throw new FalhaAoConsultarException();
        } else {
            estadoDaPorta.setAdminState(TratativaRetornoUtil.tratHuawei(resp, "Control flag").equalsIgnoreCase("active"));
            estadoDaPorta.setOperState(TratativaRetornoUtil.tratHuawei(resp, "Run state").equalsIgnoreCase("online"));
            serial.setSerial(TratativaRetornoUtil.valueFromParentesis(TratativaRetornoUtil.tratHuawei(resp, "SN ")));
            serial.setIdOnt(TratativaRetornoUtil.valueFromParentesis(TratativaRetornoUtil.tratHuawei(resp, "Password")));
        }

        System.out.println("");
    }

    protected ComandoDslam getComandoGetEstadoDaPorta(InventarioRede i) {
        return new ComandoDslam("interface gpon 0/" + i.getSlot(), 3000, "display ont info " + i.getPorta() + " " + i.getLogica() + "\n\n", 3000, "quit\n");
    }

    @Override
    public EstadoDaPorta getEstadoDaPorta(InventarioRede i) throws Exception {

        if (estadoDaPorta == null) {
            tabelaEstadoDaPorta(i);
        }

        return estadoDaPorta;
    }

    protected ComandoDslam getComandoGetServicePorts(InventarioRede i) {
        return new ComandoDslam("display service-port port 0/" + i.getSlot() + "/" + i.getPorta() + " ont " + i.getLogica(), 7000, " \n");
    }

    protected ComandoDslam getComandoGetVlanBanda(Integer index) {
        return new ComandoDslam("display service-port " + index + "\n\n", 3000);
    }

    @Override
    public SerialOntGpon getSerialOnt(InventarioRede i) throws Exception {
        if (serial == null) {
            tabelaEstadoDaPorta(i);
        }
        return serial;
    }

    protected ComandoDslam getComandoPortaPON(InventarioRede i) {
        return new ComandoDslam("interface gpon 0/" + i.getSlot(), 3000, "display port state  " + i.getPorta() + "\n", 3000, "quit\n");
    }

    @Override
    public PortaPON getPortaPON(InventarioRede i) throws Exception {
        PortaPON p = new PortaPON();
        ComandoDslam cmd = getCd().consulta(getComandoPortaPON(i));
        String moduleStatus = TratativaRetornoUtil.tratHuawei(cmd.getRetorno(), "Optical Module status");
        String portStatus = TratativaRetornoUtil.tratHuawei(cmd.getRetorno(), "Port state");
        p.setOperState(moduleStatus.contains("Online") && portStatus.contains("Online"));
        return p;
    }

    protected ComandoDslam getComandoGetParametros(InventarioRede i) {
        return new ComandoDslam("interface gpon 0/" + i.getSlot(), 3000, "display ont optical-info " + i.getPorta() + " " + i.getLogica() + "\n\n", 3000, "quit\n");
    }

    @Override
    public TabelaParametrosGponBasic getTabelaParametros(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoGetParametros(i));
        List<String> retorno = cmd.getRetorno();
        String leOlt = TratativaRetornoUtil.tratHuawei(retorno, "OLT Rx");
        String leOnt = TratativaRetornoUtil.tratHuawei(retorno, "Rx optical");

        Double potOlt = leOlt.contains("Parâmetro não encontrado") || leOlt.equalsIgnoreCase("-") ? 0d : new Double(leOlt);
        Double potOnt = leOnt.contains("Parâmetro não encontrado") || leOnt.equalsIgnoreCase("-") ? 0d : new Double(leOnt);

        /**
         * Tratativa gambeta para OLT's sem medição de Pot OLT
         */
        if (potOlt.compareTo(0d) == 0 && !(potOnt.compareTo(0d) == 0)) {
            TabelaParametrosGponBasicVivo1 tab = new TabelaParametrosGponBasicVivo1();
            tab.setPotOnt(potOnt);
            tab.getInteracoes().add(cmd);
            return tab;
        } else {
            TabelaParametrosGponVivo1 tab = new TabelaParametrosGponVivo1();
            tab.setPotOlt(potOlt);
            tab.setPotOnt(potOnt);
            tab.getInteracoes().add(cmd);
            return tab;
        }
    }

    @Override
    public VlanBanda getVlanBanda(InventarioRede i) throws Exception {

        if (spBanda == null) {
            spBanda = new ServicePort(false);
            setServicePorts(i);
        }

        return vlanBanda;
    }

    @Override
    public VlanMulticast getVlanMulticast(InventarioRede i) throws Exception {
        throw new FuncIndisponivelDslamException();
    }

    @Override
    public VlanVoip getVlanVoip(InventarioRede i) throws Exception {
        if (spVoip == null) {
            spVoip = new ServicePort(false);
            setServicePorts(i);
        }

        return vlanVoip;
    }

    @Override
    public VlanVod getVlanVod(InventarioRede i) throws Exception {
        if (spIptv == null) {
            spIptv = new ServicePort(false);
            setServicePorts(i);
        }

        return vlanVod;
    }

    protected ComandoDslam getComandoGetOntsDisp(InventarioRede i) {
        return new ComandoDslam("display ont autofind all", 5000);
    }

    @Override
    public AlarmesGpon getAlarmes(InventarioRede i) throws Exception {
        throw new FuncIndisponivelDslamException();
    }

    @Override
    public List<SerialOntGpon> getSlotsAvailableOnts(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoGetOntsDisp(i));
        List<String> retorno = cmd.getRetorno();
        Integer quant = new Integer(TratativaRetornoUtil.numberFromListMember(retorno, "number of GPON autofind ONT").get(0));
        List<SerialOntGpon> l = new ArrayList<>();
        for (int j = 1; j <= quant; j++) {
            SerialOntGpon s = new SerialOntGpon();
            s.setIdOnt(TratativaRetornoUtil.valueFromParentesis(TratativaRetornoUtil.tratHuawei(retorno, "Password", j)));
            s.setSerial(TratativaRetornoUtil.tratHuawei(retorno, "VendorID", j) + "-" + TratativaRetornoUtil.tratHuawei(retorno, "Ont SN", j).substring(TratativaRetornoUtil.tratHuawei(retorno, "Ont SN", j).length() - 8));
            String[] pegaFsp = TratativaRetornoUtil.tratHuawei(retorno, "F/S/P", j).split("/");
            s.setSlot(new Integer(pegaFsp[1]));
            s.setPorta(new Integer(pegaFsp[2]));
            l.add(s);
        }
        if (l.size() > 0) {
            l.get(0).addInteracao(cmd);
        } else {
            SerialOntGpon s = new SerialOntGpon();
            s.addInteracao(cmd);
            l.add(s);
        }

        return l;
    }

    @Override
    public Profile getProfile(InventarioRede i) throws Exception {
        if (spBanda == null) {
            setServicePorts(i);
        }
        return profile;
    }

    @Override
    public DeviceMAC getDeviceMac(InventarioRede i) throws Exception {
        throw new FuncIndisponivelDslamException();
    }

    protected ComandoDslam getCmdSetOntToOlt(InventarioRede i, SerialOntGpon s) {
        return new ComandoDslam("interface gpon 0/" + i.getSlot(), 3000, "ont modify " + i.getPorta() + " " + i.getLogica() + " password " + s.getIdOnt() + "\n\n", 5000, "ont modify " + i.getPorta() + " " + i.getLogica() + " desc Term_" + i.getTerminal() + "/VlanUsu_" + i.getCvlan() + "\n\nquit\n");
    }

    protected ComandoDslam getCmdUnSetOntToOlt(InventarioRede i) {
        return new ComandoDslam("interface gpon 0/" + i.getSlot(), 3000, "ont modify " + i.getPorta() + " " + i.getLogica() + " password 000000\n", 5000, "quit\n");
    }

    @Override
    public SerialOntGpon setOntToOlt(InventarioRede i, SerialOntGpon s) throws Exception {
//        SerialOntGpon ser = getSerialOnt(i);
//        ComandoDslam cmd0 = null;
//        if (tudoDestruido) {
//            setGemports(i);
//            cmd0 = this.getCd().consulta(getComandoCreateVlanBanda(i, gemportBanda));
//        }

        this.serial = null;
        SerialOntGpon se = this.getSerialOnt(i);
        if (tudoDestruido) {
            ComandoDslam cmd1 = getCd().consulta(getComandoGetNextFreeIndex(i));
            ComandoDslam cmd2 = getCd().consulta(getComandoCreateFromGround(i, new Integer(TratativaRetornoUtil.tratHuawei(cmd1.getRetorno(), "Next valid free service virtual port ID"))));
            this.serial = null;
            se = this.getSerialOnt(i);
            se.getInteracoes().add(0, cmd2);
            se.getInteracoes().add(0, cmd1);
        } else {
            this.serial = null;
            se = this.getSerialOnt(i);
            ComandoDslam cmd = this.getCd().consulta(this.getCmdSetOntToOlt(i, s));
            se.getInteracoes().add(0, cmd);
        }

        return se;
    }

    @Override
    public SerialOntGpon unsetOntFromOlt(InventarioRede i) throws Exception {
        ComandoDslam cmd = this.getCd().consulta(this.getCmdUnSetOntToOlt(i));
        SerialOntGpon se = this.getSerialOnt(i);
        se.getInteracoes().add(0, cmd);
        return se;
    }

    protected ComandoDslam getComandoSetEstadoDaPorta(InventarioRede i, Boolean state) {
        String leState = state ? "activate" : "deactivate";
        return new ComandoDslam("interface gpon 0/" + i.getSlot(), 3000, "ont " + leState + " " + i.getPorta() + " " + i.getLogica() + "\n\nquit\n");
    }

    @Override
    public EstadoDaPorta setEstadoDaPorta(InventarioRede i, EstadoDaPorta e) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoSetEstadoDaPorta(i, e.getAdminState()));
        EstadoDaPorta es = getEstadoDaPorta(i);
        es.getInteracoes().add(0, cmd);
        return es;
    }

    @Override
    public Profile setProfileDown(InventarioRede i, Velocidades v) throws Exception {
        ComandoDslam cmd0 = getCd().consulta(getComandoDeleteVlanBanda(i));
        Integer leIndex = spBanda.getIndex() == null ? getNextFreeIndex(i) : spBanda.getIndex();
        ComandoDslam cmd1 = getCd().consulta(getComandoCreateVlanBanda(i, leIndex));
        Profile p = getProfile(i);
        p.getInteracoes().add(0, cmd1);
        p.getInteracoes().add(0, cmd0);

        return p;
    }

    @Override
    public Profile setProfileUp(InventarioRede i, Velocidades vDown, Velocidades vUp) throws Exception {
        ComandoDslam cmd0 = getCd().consulta(getComandoDeleteVlanBanda(i));
        Integer leIndex = spBanda.getIndex() == null ? getNextFreeIndex(i) : spBanda.getIndex();
        ComandoDslam cmd1 = getCd().consulta(getComandoCreateVlanBanda(i, leIndex));
        Profile p = getProfile(i);
        p.getInteracoes().add(0, cmd1);
        p.getInteracoes().add(0, cmd0);

        return p;
    }

    protected ComandoDslam getComandoGetNextFreeIndex(InventarioRede i) {
        return new ComandoDslam("display service-port next-free-index\n\n", 3000);

    }

    protected Integer getNextFreeIndex(InventarioRede i) throws Exception {
        List<String> resp = getCd().consulta(getComandoGetNextFreeIndex(i)).getRetorno();
        return new Integer(TratativaRetornoUtil.tratHuawei(resp, "Next valid free service virtual port ID"));
    }

    protected ComandoDslam getComandoCreateFromGround(InventarioRede i, Integer index) {
        if (i.getBhs()) {

            return new ComandoDslam(
                    "interface gpon 0/" + i.getSlot() + "\n\n"
                    + "ont add " + i.getPorta() + " " + i.getLogica() + " password-auth " + i.getIdOnt() + " always-on profile-id 7 desc Term_" + i.getTerminal() + "/VlanUsu_" + i.getCvlan() + " manage-mode omci\n\n"
                    + "ont native-vlan " + i.getPorta() + " " + i.getLogica() + " unconcern\n\n"
                    + "tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 4 profile-id 500\n\n"
                    + "gemport add " + i.getPorta() + " gemportid " + gemportBanda + " eth encrypt on \n\n"
                    + "ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 10 1 translation s-vlan 10\n\n"
                    + "ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " 4 gemport-car 6\n\n"
                    + "ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " vlan 10\n\n"
                    + "quit\n"
                    + "service-port  vlan " + i.getRin() + " gpon 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportBanda + " multi-service user-vlan 10 tag-transform translate-and-add inner-vlan " + i.getCvlan() + " inner-priority 0 inbound traffic-table index 6 outbound traffic-table index 500\n\n",
                    5000);
        }

        return new ComandoDslam("interface gpon 0/" + i.getSlot() + "\n\n"
                + "ont alarm-profile " + i.getPorta() + " " + i.getLogica() + " profile-id 1\n\n"
                + "ont ipconfig " + i.getPorta() + " " + i.getLogica() + " dhcp\n\n"
                + "gemport add " + i.getPorta() + " gemportid " + gemportBanda + " eth encrypt on \n\n"
                + "tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 4 profile-id 500\n\n"
                + "ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " 4 gemport-car 6\n\n"
                + "ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " vlan 10\n\n"
                + "ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 10 1 translation s-vlan 10\n\n"
                + "ont port priority-policy " + i.getPorta() + " " + i.getLogica() + " eth 1 copy-cos\n\n"
                + "ont port q-in-q " + i.getPorta() + " " + i.getLogica() + " eth 1 disable\n\n"
                + "ont port native-vlan " + i.getPorta() + " " + i.getLogica() + " eth 1 vlan 10 priority 0\n\n"
                + "quit\n\n"
                + "service-port " + index + " vlan " + i.getRin() + " gpon 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportBanda + " multi-service user-vlan 10 tag-transform translate-and-add inner-vlan " + i.getCvlan() + " inner-priority 0 inbound traffic-table index 6 outbound traffic-table index 43\n\n"
                + "stacking label service-port " + index + " " + i.getCvlan() + " \n\n", 5000);
    }

    protected ComandoDslam getComandoCreateVlanBanda(InventarioRede i, Integer index) {
        if (gemportBanda == null) {
            setGemports(i);
        }
        if (i.getBhs()) {
            return new ComandoDslam("interface gpon 0/" + i.getSlot() + "\n\n"
                    + "ont native-vlan " + i.getPorta() + " " + i.getLogica() + " unconcern\n\n"
                    + "tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 4 profile-id 500\n\n"
                    + "gemport add " + i.getPorta() + " gemportid " + gemportBanda + " eth encrypt on \n\n"
                    + "ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 10 1 translation s-vlan 10\n\n"
                    + "ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " 4 gemport-car 6\n\n"
                    + "ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " vlan 10\n\n"
                    + "quit\n\n"
                    + "service-port  vlan " + i.getRin() + " gpon 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportBanda + " multi-service user-vlan 10 tag-transform translate-and-add inner-vlan " + i.getCvlan() + " inner-priority 0 inbound traffic-table index 6 outbound traffic-table index 500\n\n", 5000);
        }

        return new ComandoDslam("interface gpon 0/" + i.getPorta() + "\n\n"
                + "ont alarm-profile " + i.getPorta() + " " + i.getLogica() + " profile-id 1\n\n"
                + "ont ipconfig " + i.getPorta() + " " + i.getLogica() + " dhcp\n\n"
                + "gemport add " + i.getPorta() + " gemportid " + gemportBanda + " eth encrypt on \n\n"
                + "tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 4 profile-id 500\n\n"
                + "ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " 4 gemport-car 6\n\n"
                + "ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + " vlan 10\n\n"
                + "ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 10 1 translation s-vlan 10\n\n"
                + "ont port priority-policy " + i.getPorta() + " " + i.getLogica() + " eth 1 copy-cos\n\n"
                + "ont port q-in-q " + i.getPorta() + " " + i.getLogica() + " eth 1 disable\n\n"
                + "ont port native-vlan " + i.getPorta() + " " + i.getLogica() + " eth 1 vlan 10 priority 0\n\n"
                + "quit\n\n"
                + "service-port " + index + " vlan " + i.getRin() + " gpon 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportBanda + " multi-service user-vlan 10 tag-transform translate-and-add inner-vlan " + i.getCvlan() + " inner-priority 0 inbound traffic-table index 6 outbound traffic-table index 43\n\n"
                + "stacking label service-port " + index + " " + i.getCvlan() + " \n\n", 5000);
    }

    @Override
    public VlanBanda createVlanBanda(InventarioRede i, Velocidades vDown, Velocidades vUp) throws Exception {
//        System.out.println(getNextFreeIndex(i));
        ComandoDslam cmd0 = getCd().consulta(getComandoGetNextFreeIndex(i));

        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanBanda(i, new Integer(TratativaRetornoUtil.tratHuawei(cmd0.getRetorno(), "Next valid free service virtual port ID"))));
        spBanda = null;
        vlanBanda = null;
        getVlanBanda(i);
        vlanBanda.getInteracoes().add(0, cmd);
        vlanBanda.getInteracoes().add(0, cmd0);

        return vlanBanda;
    }

    protected ComandoDslam getComandoCreateVlanVoip(InventarioRede i, Integer index) {
        if (gemportVoip == null) {
            setGemports(i);
        }

        return new ComandoDslam("interface gpon 0/" + i.getSlot() + "\n\n"
                + "ont native-vlan " + i.getPorta() + " " + i.getLogica() + " unconcern\n\n"
                + "tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 3 profile-id 30\n\n"
                + "gemport add " + i.getPorta() + " gemportid " + gemportVoip + " eth encrypt on \n\n"
                + "ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 30 1 translation s-vlan 30\n\n"
                + "ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportVoip + " 3 gemport-car 30\n\n"
                + "ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportVoip + " vlan 30\n\n"
                + "quit\n\n"
                + "service-port  vlan " + i.getVlanVoip() + " gpon 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportVoip + " multi-service user-vlan 30 tag-transform translate inbound traffic-table index 30 outbound traffic-table index 30\n\n", 5000);

    }

    @Override
    public VlanVoip createVlanVoip(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanVoip(i, getNextFreeIndex(i)));
        spVoip = null;
        vlanVoip = null;
        getVlanVoip(i);

        vlanVoip.getInteracoes().add(0, cmd);
        return vlanVoip;
    }

    protected ComandoDslam getComandoCreateVlanVod(InventarioRede i, Integer index) {
        if (gemportIptv == null) {
            setGemports(i);
        }
        return new ComandoDslam("interface gpon 0/" + i.getSlot() + "\n\n"
                + "ont native-vlan " + i.getPorta() + " " + i.getLogica() + " unconcern\n\n"
                + "tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 2 profile-id 20\n\n"
                + "gemport add " + i.getPorta() + " gemportid " + gemportIptv + " eth encrypt on \n\n"
                + "ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 20 1 translation s-vlan 20\n\n"
                + "ont multicast-forward " + i.getPorta() + " " + i.getLogica() + " tag translation 20\n\n"
                + "ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportIptv + " 2 gemport-car 42\n\n"
                + "ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportIptv + " vlan 20\n\n"
                + "quit\n\n"
                + "service-port " + index + " vlan 400 gpon 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportIptv + " multi-service user-vlan 20 tag-transform translate inbound traffic-table index 42 outbound traffic-table index 42\n\n"
                + "btv\n\n"
                + "igmp user add service-port " + index + " no-auth max-program 32\n\n"
                + " \n\n"
                + " \n\n"
                + "multicast-vlan " + i.getVlanMulticast() + "\n\n"
                + "igmp multicast-vlan member service-port " + index + "\n\n"
                + "quit\n", 15000);
    }

    @Override
    public VlanVod createVlanVod(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoCreateVlanVod(i, getNextFreeIndex(i)));
        spIptv = null;
        vlanVod = null;
        getVlanVod(i);
        vlanVod.getInteracoes().add(0, cmd);
        return vlanVod;
    }

    @Override
    public VlanMulticast createVlanMulticast(InventarioRede i) throws Exception {
        throw new FuncIndisponivelDslamException();
    }

    protected ComandoDslam getComandoDeleteVlanBanda(InventarioRede i) throws Exception {
        if (gemportBanda == null) {
            setGemports(i);
        }
//        String indexSpIptv = spIptv == null ? "" : spIptv.getIndex().toString();
//        String indexSpVoip = spVoip == null ? "" : spVoip.getIndex().toString();
//        String indexSpBanda = spBanda == null ? "" : spBanda.getIndex().toString();

        return new ComandoDslam("undo service-port port 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportBanda + "\n\n"
                + "y\n"
                + "interface gpon 0/" + i.getSlot() + "\n\n"
                + "undo ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + "\n\n"
                + "undo ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportBanda + "\n\n"
                + "undo ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 10 1\n\n"
                + "gemport delete " + i.getPorta() + " gemportid " + gemportBanda + "\n\n"
                + "undo tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 4\n\n"
                + "quit\n\n", 5000);
    }

    @Override
    public VlanBanda deleteVlanBanda(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoDeleteVlanBanda(i));
        spBanda = null;
        vlanBanda = null;
        getVlanBanda(i);
        vlanBanda.getInteracoes().add(0, cmd);
        return vlanBanda;
    }

    protected ComandoDslam getComandoDeleteVlanVoip(InventarioRede i) throws Exception {
        if (spVoip == null) {
            setServicePorts(i);
        }
        if (gemportVoip == null) {
            setGemports(i);
        }

        return new ComandoDslam("undo service-port port 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportVoip + "\n\n"
                + "y\n"
                + "interface gpon 0/" + i.getSlot() + "\n\n"
                + "undo ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportVoip + "\n\n"
                + "undo ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportVoip + "\n\n"
                + "undo ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 30 1\n\n"
                + "gemport delete " + i.getPorta() + " gemportid " + gemportVoip + "\n\n"
                + "undo tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 3\n\n"
                + "quit\n\n", 5000);
    }

    @Override
    public VlanVoip deleteVlanVoip(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoDeleteVlanVoip(i));
        spVoip = null;
        vlanVoip = null;
        getVlanVoip(i);
        vlanVoip.getInteracoes().add(0, cmd);
        return vlanVoip;
    }

    protected ComandoDslam getComandoDeleteVlanVod(InventarioRede i) throws Exception {
        if (spIptv == null) {
            setServicePorts(i);
        }
        if (gemportIptv == null) {
            setGemports(i);
        }

        String indexSpIptv = spIptv == null ? "" : spIptv.getIndex().toString();
        return new ComandoDslam("undo service-port port 0/" + i.getSlot() + "/" + i.getPorta() + " gemport " + gemportIptv + "\n\n"
                + "y\n"
                + "interface gpon 0/" + i.getSlot() + "\n\n"
                + "undo ont gemport mapping " + i.getPorta() + " " + i.getLogica() + " " + gemportIptv + "\n\n"
                + "undo ont gemport bind " + i.getPorta() + " " + i.getLogica() + " " + gemportIptv + "\n\n"
                + "undo ont port vlan " + i.getPorta() + " " + i.getLogica() + " eth 20 1\n\n"
                + "gemport delete " + i.getPorta() + " gemportid " + gemportIptv + "\n\n"
                + "undo tcont bind-profile " + i.getPorta() + " " + i.getLogica() + " 2\n\n"
                + "quit\n\n", 5000);
    }

    @Override
    public VlanVod deleteVlanVod(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoDeleteVlanVod(i));
        spIptv = null;
        vlanVod = null;
        getVlanVod(i);
        vlanVod.getInteracoes().add(0, cmd);
        return vlanVod;
    }

    @Override
    public VlanMulticast deleteVlanMulticast(InventarioRede i) throws Exception {
        throw new FuncIndisponivelDslamException();
    }

    @Override
    public List<VelocidadeVendor> obterVelocidadesDownVendor() {
        if (velsDown.isEmpty()) {
            Velocidades[] vels = Velocidades.values();
            Arrays.sort(vels, Collections.reverseOrder());
            for (Velocidades vel : vels) {
                if (new Double(vel.getValor()) <= 100) {
                    velsDown.add(new VelocidadeVendor(vel, "43"));
                } else {
                    velsDown.add(new VelocidadeVendor(vel, "500"));
                }
            }
        }

        return velsDown;

    }

    @Override
    public List<VelocidadeVendor> obterVelocidadesUpVendor() {
        if (velsUp.isEmpty()) {
            Velocidades[] vels = Velocidades.values();
            Arrays.sort(vels, Collections.reverseOrder());
            for (Velocidades vel : vels) {
                velsUp.add(new VelocidadeVendor(vel, "6"));
            }
        }
        return velsUp;
    }

    protected ComandoDslam getComandoGetEstadoPortasProximas(InventarioRede i) {
        return new ComandoDslam("interface gpon 0/" + i.getSlot() + "\n\n"
                + "display ont info " + i.getPorta() + " all\n\nquit\n\n", 5000);
    }

    @Override
    public List<Porta> getEstadoPortasProximas(InventarioRede i) throws Exception {
        ComandoDslam cmd = getCd().consulta(getComandoGetEstadoPortasProximas(i));
        List<String> retorno = cmd.getRetorno();
        Integer countStringOccurrence = TratativaRetornoUtil.countStringOccurrence(retorno, "0/" + i.getSlot() + "/" + i.getPorta());
        List<Porta> list = new ArrayList<>();
        for (int j = 1; j < ((countStringOccurrence - 1) / 2); j++) {
            Porta porta = new Porta();
            EstadoDaPorta estado = new EstadoDaPorta();
            List<String> linha = TratativaRetornoUtil.tratZhone(retorno, "0/" + i.getSlot() + "/" + i.getPorta(), "\\b\\w+\\b", j);

            estado.setAdminState(linha.get(5).equalsIgnoreCase("active"));
            estado.setOperState(linha.get(6).equalsIgnoreCase("online"));
            estado.addInteracao(cmd);
            porta.setEstadoPorta(estado);
            porta.setNumPorta(new Integer(linha.get(3)));
            list.add(porta);
        }

        return list;
    }

    @Override
    public ReConexao getReconexoes(InventarioRede i) throws Exception {
        throw new FuncIndisponivelDslamException();
    }

}
