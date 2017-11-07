/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.dslam.impl.login;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import dao.dslam.impl.Conector;
import dao.dslam.impl.ConsultaDslamVivo1;
import exception.FalhaJumpAccessEsception;
import exception.SemGerenciaException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;
import model.dslam.credencial.Credencial;

/**
 *
 * @author G0042204
 */
public class LoginComJump implements LoginDslamStrategy {

    private JSch jsch;
    private Session session;
    private ConsultaDslamVivo1 cs;

    @Override
    public void conectar(Conector css) throws Exception {

        this.cs = (ConsultaDslamVivo1) css;
        
        try {
            jsch = new JSch();
            session = jsch.getSession("incid", "10.18.81.96", 22);
            session.setPassword("v!vo@incid");

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FalhaJumpAccessEsception();
        }

        try {
            String telnet = "telnet " + cs.dslam.getIpDslam();

            cs.channel = session.openChannel("shell");

            cs.out = new PrintWriter(cs.channel.getOutputStream(), false);
            cs.in = new BufferedReader(new InputStreamReader(cs.channel.getInputStream()));

            cs.channel.connect();
            Thread.sleep(1000);

            cs.out.print(telnet + "\r");
            cs.out.flush();
            Thread.sleep(3000);
            cs.out.print(Credencial.VIVO1.getLogin() + "\r");
            cs.out.flush();
            Thread.sleep(1000);
            cs.out.print(Credencial.VIVO1.getPass() + "\r");
            cs.out.flush();
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SemGerenciaException();
        }
    }

}
