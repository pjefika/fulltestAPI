/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dslam.factory.exception;

/**
 *
 * @author G0042204
 */
public class FuncIndisponivelDslamException extends Exception {

    public FuncIndisponivelDslamException() {
        super("Funcionalidade insdiponível para este modelo de Dslam.");
    }

}