/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.base.mb;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author ayslanms
 */
@ManagedBean
@ViewScoped
public class TesteSecurityMB implements Serializable{
    
    private TesteSecurity testeSecurity = new TesteSecurity();
    

    @PostConstruct
    public void init(){
        testeSecurity.setName1("ATIVO");
    }
    
    public void submit(){
        System.out.println(testeSecurity);
    }

    public TesteSecurity getTesteSecurity() {
        return testeSecurity;
    }

    public void setTesteSecurity(TesteSecurity testeSecurity) {
        this.testeSecurity = testeSecurity;
    }   
       
}
