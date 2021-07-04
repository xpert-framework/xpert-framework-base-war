package com.base.mb.audit;

import com.base.bo.audit.AuditingBO;
import com.base.bo.audit.QueryAuditingBO;
import com.base.modelo.audit.QueryAuditing;
import com.base.vo.audit.ConsultaQueryAuditoria;
import com.base.vo.audit.TabelaAuditoria;
import com.xpert.core.exception.BusinessException;
import com.xpert.faces.primefaces.LazyDataModelImpl;
import com.xpert.faces.utils.FacesMessageUtils;
import java.io.Serializable;
import java.util.List;
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
public class QueryAuditingMB implements Serializable {

    @EJB
    private QueryAuditingBO queryAuditingBO;
    @EJB
    private AuditingBO auditingBO;

    private ConsultaQueryAuditoria consultaAuditoria = new ConsultaQueryAuditoria();
    private LazyDataModelImpl<QueryAuditing> auditings;
    private List<TabelaAuditoria> tabelas;
    private QueryAuditing auditing;

    @PostConstruct
    public void init() {
        tabelas = auditingBO.getTabelasAuditoria();
    }

    public void limpar() {
        consultaAuditoria = new ConsultaQueryAuditoria();
        auditings = null;
    }

    public void consultar() {
        auditings = null;
        try {
            auditings = queryAuditingBO.consultar(consultaAuditoria);
        } catch (BusinessException ex) {
            FacesMessageUtils.error(ex);
        }
    }

    public ConsultaQueryAuditoria getConsultaAuditoria() {
        return consultaAuditoria;
    }

    public void setConsultaAuditoria(ConsultaQueryAuditoria consultaAuditoria) {
        this.consultaAuditoria = consultaAuditoria;
    }

    public List<TabelaAuditoria> getTabelas() {
        return tabelas;
    }

    public void setTabelas(List<TabelaAuditoria> tabelas) {
        this.tabelas = tabelas;
    }

    public QueryAuditing getAuditing() {
        return auditing;
    }

    public void setAuditing(QueryAuditing auditing) {
        this.auditing = auditing;
    }

    public LazyDataModelImpl<QueryAuditing> getAuditings() {
        return auditings;
    }

    public void setAuditings(LazyDataModelImpl<QueryAuditing> auditings) {
        this.auditings = auditings;
    }
    
    

}
