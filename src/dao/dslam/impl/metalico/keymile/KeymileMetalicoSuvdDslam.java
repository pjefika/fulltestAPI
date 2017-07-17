/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl.metalico.keymile;

import br.net.gvt.efika.customer.InventarioRede;
import dao.dslam.impl.ComandoDslam;
import dao.dslam.impl.ConsultaDslam;
import dao.dslam.impl.retorno.TratativaRetornoUtil;
import java.util.ArrayList;
import java.util.List;
import model.dslam.consulta.Profile;
import model.dslam.consulta.VlanBanda;
import model.dslam.consulta.VlanMulticast;
import model.dslam.consulta.VlanVod;
import model.dslam.consulta.VlanVoip;
import model.dslam.consulta.metalico.Modulacao;
import model.dslam.consulta.metalico.TabelaParametrosMetalico;
import model.dslam.consulta.metalico.TabelaParametrosMetalicoVdsl;
import model.dslam.velocidade.Velocidades;

/**
 *
 * @author G0042204
 */
public abstract class KeymileMetalicoSuvdDslam extends KeymileMetalicoDslam {

    public KeymileMetalicoSuvdDslam(String ipDslam) {
        super(ipDslam);
        this.setCd(new ConsultaDslam(this));
    }

    public ComandoDslam getComandoConsultaVlan2(String srvc) {
        return new ComandoDslam("get /services/packet/" + srvc + "/cfgm/Service");
    }

    @Override
    public TabelaParametrosMetalico getTabelaParametros(InventarioRede i) throws Exception {
        List<String> velSinc = this.getCd().consulta(this.getVelSinc(i)).getRetorno();
        List<String> atnSnr = this.getCd().consulta(this.getSnrAtn(i)).getRetorno();

        try {
            TabelaParametrosMetalicoVdsl tab = new TabelaParametrosMetalicoVdsl();

            tab.setVelSincDown(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate")));
            tab.setVelSincUp(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate", 2)));
            tab.setAtnUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation")));
            tab.setSnrUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin")));
            tab.setAtnUp1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 2)));
            tab.setSnrUp1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 2)));
            tab.setAtnUp2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 3)));
            tab.setSnrUp2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 3)));
            tab.setAtnDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 4)));
            tab.setSnrDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 4)));
            tab.setAtnDown1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 5)));
            tab.setSnrDown1(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 5)));
            tab.setAtnDown2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 6)));
            tab.setSnrDown2(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 6)));

            return tab;

        } catch (Exception e) {

            TabelaParametrosMetalico tab = new TabelaParametrosMetalico();

            try {
                tab.setVelSincDown(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate")));
                tab.setVelSincUp(new Double(TratativaRetornoUtil.tratKeymile(velSinc, "CurrentRate", 2)));
                tab.setAtnUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation")));
                tab.setSnrUp(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin")));
                tab.setAtnDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrAttenuation", 2)));
                tab.setSnrDown(new Double(TratativaRetornoUtil.tratKeymile(atnSnr, "CurrSnrMargin", 2)));

                return tab;
            } catch (Exception ex) {
                tab.setVelSincDown(new Double("0"));
                tab.setVelSincUp(new Double("0"));
                tab.setAtnUp(new Double("0"));
                tab.setSnrUp(new Double("0"));
                tab.setAtnDown(new Double("0"));
                tab.setSnrDown(new Double("0"));

                return tab;
            }
        }

    }

    @Override
    public VlanBanda getVlanBanda(InventarioRede i) throws Exception {
        List<String> pegaSrvc = this.getCd().consulta(this.getComandoGetSrvc(i, "1")).getRetorno();

        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");
        Integer cvlan = new Integer("0");
        Integer p100 = new Integer("0");
        if (!leSrvc.contentEquals("no service connected")) {
            List<String> pegaVlan = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc)).getRetorno();
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "Svid"));
            p100 = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "CVID"));
        }
        VlanBanda vlanBanda = new VlanBanda(cvlan, p100);

        return vlanBanda;
    }

    @Override
    public VlanVoip getVlanVoip(InventarioRede i) throws Exception {
        List<String> pegaSrvc = this.getCd().consulta(this.getComandoGetSrvc(i, "2")).getRetorno();

        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");
        Integer cvlan = new Integer("0");
        Integer p100 = new Integer("0");
        if (!leSrvc.contentEquals("no service connected")) {
            List<String> pegaVlan = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc)).getRetorno();
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "Svid"));
            p100 = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "CVID"));
        }
        VlanVoip vlanVoip = new VlanVoip(cvlan, p100);

        return vlanVoip;
    }

    @Override
    public VlanVod getVlanVod(InventarioRede i) throws Exception {
        List<String> pegaSrvc = this.getCd().consulta(this.getComandoGetSrvc(i, "3")).getRetorno();

        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");
        Integer cvlan = new Integer("0");
        Integer p100 = new Integer("0");
        if (!leSrvc.contentEquals("no service connected")) {
            List<String> pegaVlan = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc)).getRetorno();
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "Svid"));
            p100 = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "CVID"));
        }
        VlanVod vlanVod = new VlanVod(cvlan, p100);

        return vlanVod;
    }

    @Override
    public VlanMulticast getVlanMulticast(InventarioRede i) throws Exception {
        List<String> pegaSrvc = this.getCd().consulta(this.getComandoGetSrvc(i, "4")).getRetorno();
        String leSrvc = TratativaRetornoUtil.tratKeymile(pegaSrvc, "ServicesCurrentConnected").replace("\"", "").replace(";", "");

        VlanMulticast vlanMult = new VlanMulticast();
        Integer cvlan = new Integer("0");
        if (!leSrvc.contentEquals("no service connected")) {
            List<String> pegaVlan = this.getCd().consulta(this.getComandoConsultaVlan(leSrvc)).getRetorno();
            cvlan = new Integer(TratativaRetornoUtil.tratKeymile(pegaVlan, "McastVID"));
        }

        vlanMult.setCvlan(cvlan);

        return vlanMult;
    }

    @Override
    public Profile getProfile(InventarioRede i) throws Exception {
        List<String> pegaProfile = this.getCd().consulta(this.getProf(i)).getRetorno();
        String first = TratativaRetornoUtil.tratKeymile(pegaProfile, "Name");
        List<String> leProf = TratativaRetornoUtil.numberFromString(first);

        Profile prof = new Profile();
        prof.setProfileDown(leProf.get(0));
        prof.setProfileUp(leProf.get(1));

        return prof;
    }

    @Override
    public Modulacao getModulacao(InventarioRede ir) throws Exception {
        List<String> pegaModul = this.getCd().consulta(this.getModul(ir)).getRetorno();
        String modul = null;
        Integer i;
        for (i = 0; i < pegaModul.size(); i++) {
            if (pegaModul.get(i).contains("true")) {
                modul = pegaModul.get(i + 1).replaceAll("\\ # Name", "").replaceAll("\\\\", "").trim();
            }
        }

        Modulacao m = new Modulacao();
        m.setModulacao(modul);

        return m;
    }

    @Override
    public void setProfileDown(InventarioRede i, Velocidades v) throws Exception {
        String leSet = getCd().consulta(getComandoSetProfileDefault(i, v)).getBlob();
        List<String> leResp = new ArrayList<>();
        if (leSet.contains("previously") || leSet.contains("is not compatible")) {
            leSet = getCd().consulta(getComandoSetProfileSeco(i, v)).getBlob();
            if (leSet.contains("previously") || leSet.contains("is not compatible")) {
                leResp = getCd().consulta(getComandoSetProfileSUVD1(i, v)).getRetorno();
            } else {
                String[] parser = leSet.split("\\n");
                for (String string : parser) {
                    leResp.add(string);
                }
            }
        } else {
            String[] parser = leSet.split("\\n");
            for (String string : parser) {
                leResp.add(string);
            }
        }
        for (String string : leResp) {
            System.out.println(string);
        }
    }

    @Override
    public void setProfileUp(InventarioRede i, Velocidades vDown, Velocidades vUp) throws Exception {
        setProfileDown(i, vUp);
    }

    @Override
    public VlanBanda createVlanBanda(InventarioRede i, Velocidades vDown, Velocidades vUp) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VlanVoip createVlanVoip(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VlanVod createVlanVod(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public VlanMulticast createVlanMulticast(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVlanBanda(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVlanVoip(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVlanVod(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteVlanMulticast(InventarioRede i) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Modulacao setModulacao(InventarioRede i, Velocidades v) throws Exception {
        String leResp = "";
        Boolean isAdsl = new Double(v.getVel()).compareTo(20d) <= 0;
        if (isAdsl) {
            leResp = getCd().consulta(getComandoSetModulacaoAdsl(i, v)).getBlob();
            if (leResp.contains("previously") || leResp.contains("is not compatible")) {
                leResp = getCd().consulta(getComandoSetModulacaoAdsl1(i, v)).getBlob();
            }
        } else {
            leResp = getCd().consulta(getComandoSetModulacaoVdsl(i, v)).getBlob();
            if (leResp.contains("previously") || leResp.contains("is not compatible")) {
                leResp = getCd().consulta(getComandoSetModulacaoVdsl1(i, v)).getBlob();
                if (leResp.contains("previously") || leResp.contains("is not compatible")) {
                    leResp = getCd().consulta(getComandoSetModulacaoVdsl11(i, v)).getBlob();
                }
            }
        }

        System.out.println(leResp);

        return getModulacao(i);
    }

    protected ComandoDslam getComandoSetModulacaoAdsl(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "false default 0 false default 0 false default 0 true " + castModulacao(v).getModulacao() + " priority");
    }

    protected ComandoDslam getComandoSetModulacaoAdsl1(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "false default 0 false default 0 false default 0 true ADSL2PLUS_SUVD11 priority");
    }

    protected ComandoDslam getComandoSetModulacaoVdsl(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "true " + castModulacao(v).getModulacao() + " 0 false default 0 false default 0 false default priority");
    }

    protected ComandoDslam getComandoSetModulacaoVdsl1(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "true " + castModulacao(v).getModulacao() + "D1 0 false default 0 false default 0 false default priority");
    }

    protected ComandoDslam getComandoSetModulacaoVdsl11(InventarioRede i, Velocidades v) {
        return new ComandoDslam("set unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles "
                + "true " + castModulacao(v).getModulacao() + "D11 0 false default 0 false default 0 false default priority");
    }

    protected ComandoDslam getComandoSetProfileDefault(InventarioRede i, Velocidades vDown) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile " + castProfile(vDown).getProfileDown());
    }

    protected ComandoDslam getComandoSetProfileSeco(InventarioRede i, Velocidades vDown) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile " + castProfile(vDown).getProfileUp());
    }

    protected ComandoDslam getComandoSetProfileSUVD1(InventarioRede i, Velocidades vDown) {
        return new ComandoDslam("set /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile " + castProfile(vDown).getProfileDown() + "D1");
    }

    protected ComandoDslam getModul(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/cfgm/portprofiles");
    }

    protected ComandoDslam getVelSinc(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/status/status");
    }

    protected ComandoDslam getSnrAtn(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/status/bandstatus");
    }

    protected ComandoDslam getComandoGetSrvc(InventarioRede i, String intrf) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/interface-" + intrf + "/status/ServiceStatus");
    }

    protected ComandoDslam getProf(InventarioRede i) {
        return new ComandoDslam("get /unit-" + i.getSlot() + "/port-" + i.getPorta() + "/chan-1/cfgm/chanprofile");
    }

    @Override
    public Profile castProfile(Velocidades v) {
        Profile p = new Profile();
        Double leVel = new Double(v.getVel());
        Double umDeUp = 20d;
        Double doisDeUp = 30d;
        Double tresDeUp = 40d;

        Boolean isUmDeUp = leVel.compareTo(umDeUp) <= 0;
        Boolean isDoisDeUp = leVel.compareTo(doisDeUp) <= 0;
        Boolean isTresDeUp = leVel.compareTo(tresDeUp) <= 0;

        if (isUmDeUp) {
            p.setProfileDown("HSI_" + v.getVel() + "Mb_1Mb_SUV");
            p.setProfileUp("HSI_" + v.getVel() + "Mb_1Mb");
        } else {
            if (isDoisDeUp) {
                p.setProfileDown("HSI_" + v.getVel() + "Mb_2Mb_SUV");
                p.setProfileUp("HSI_" + v.getVel() + "Mb_2Mb");
            } else {
                if (isTresDeUp) {
                    p.setProfileDown("HSI_" + v.getVel() + "Mb_3Mb_SUV");
                    p.setProfileUp("HSI_" + v.getVel() + "Mb_3Mb");
                } else {
                    p.setProfileDown("HSI_" + v.getVel() + "Mb_5Mb_SUV");
                    p.setProfileUp("HSI_" + v.getVel() + "Mb_5Mb");
                }
            }
        }

        return p;
    }

    @Override
    public Modulacao castModulacao(Velocidades v) {
        Modulacao m = new Modulacao();

        Double leVel = new Double(v.getVel());
//        Double autoLimit = 5d;
        Double adslLimit = 20d;
//        Boolean isAuto = leVel.compareTo(autoLimit) <= 0;
        Boolean isAdsl = leVel.compareTo(adslLimit) <= 0;
        String leModul = isAdsl ? "ADSL2PLUS_ONLY_SUV" : "VDSL_17A_B8_12_SUV";
        m.setModulacao(leModul);

        return m;
    }
}
