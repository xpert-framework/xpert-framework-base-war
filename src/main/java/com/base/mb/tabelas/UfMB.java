package com.base.mb.tabelas;


import java.io.Serializable;
import com.xpert.core.crud.AbstractBaseBean;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named; 
import com.base.bo.tabelas.UfBO;
import com.base.modelo.tabelas.Uf;

/**
 *
 * @author ayslanms
 */
@Named
@ViewScoped
public class UfMB extends AbstractBaseBean<Uf> implements Serializable {

    @EJB
    private UfBO ufBO;

    @Override
    public UfBO getBO() {
        return ufBO;
    }

    @Override
    public String getDataModelOrder() {
        return "nome";
    }
}
