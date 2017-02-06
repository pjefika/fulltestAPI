/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import model.dslam.AbstractDslam;
import model.dslam.consulta.ConsultaGponDefault;
import model.fulltest.FullTestFacade;
import model.fulltest.validacao.ValidacaoFacade;
import dao.cadastro.CadastroDAO;

/**
 *
 * @author G0042204
 */
public class testRefactoringSocketClass {

    /**
     * Alcatel: 7530301249 | Zhone: 7130520294 - 1630143618 - 8531030639(hib) |
     * Keymile: 7930272843 - 3125714804
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        CadastroDAO dao = new CadastroDAO();

        try {

            AbstractDslam ds = dao.getDslam("8531030639");
            FullTestFacade f = new FullTestFacade(ds);

//            f.estadoPorta();
//            f.serialOnt();
//            f.consultaParametros();
//            f.consultaVlanBanda();
//            f.consultaVlanVoip();
//            f.consultaVlanVod();
//            f.consultaVlanMulticast();
//            f.consultaAlarmes();
//            f.consultaProfile();
            ValidacaoFacade v = new ValidacaoFacade((AbstractDslam) ds);

            v.validar();

            f.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
