package com.base.mb.dashboard;

import com.base.bo.dashboard.DashboardErroSistemaBO;
import com.base.vo.dashboard.DashboardErroSistema;
import com.xpert.core.exception.BusinessException;
import com.xpert.faces.utils.FacesMessageUtils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author ayslanms
 */
@Named
@ViewScoped
public class DashboardErroSistemaMB implements Serializable {

    @EJB
    private DashboardErroSistemaBO dashboardErroSistemaBO;

    private DashboardErroSistema dashboardErroSistema;

    @PostConstruct
    public void init() {
        try {
            dashboardErroSistema = dashboardErroSistemaBO.getDashboardErroSistema();
        } catch (BusinessException ex) {
            FacesMessageUtils.error(ex);
        }
    }

    public void carregarDashboard() {
        try {
            dashboardErroSistemaBO.carregarDashboardErroSistema(dashboardErroSistema);
        } catch (BusinessException ex) {
            FacesMessageUtils.error(ex);
        }
    }

    public DashboardErroSistema getDashboardErroSistema() {
        return dashboardErroSistema;
    }

    public void setDashboardErroSistema(DashboardErroSistema dashboardErroSistema) {
        this.dashboardErroSistema = dashboardErroSistema;
    }

}
