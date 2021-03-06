/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.fulltest.operacional;

import dao.FactoryDAO;
import dao.log.LogEntityDAO;
import model.entity.LogEntity;

/**
 *
 * @author G0041775
 */
public abstract class FulltestRunnable implements Runnable {

    protected LogEntity logzin;

    private LogEntityDAO logDao;

    public FulltestRunnable(LogEntity logzin) {
        this.logzin = logzin;
    }

    public LogEntity getLogzin() {
        return logzin;
    }

    public void setLogzin(LogEntity logzin) {
        this.logzin = logzin;
    }

    public LogEntityDAO getLogDao() {
        if (logDao == null) {
            logDao = FactoryDAO.createLogEntityDAO();
        }
        return logDao;
    }

}
