package com.base.mb.audit;

import com.base.bo.audit.AuditingBO;
import com.base.modelo.audit.Auditing;
import com.base.vo.audit.ConsultaAuditoria;
import com.base.vo.audit.TabelaAuditoria;
import com.xpert.core.exception.BusinessException;
import com.xpert.faces.utils.FacesMessageUtils;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;

/**
 *
 * @author ayslanms
 */
@Named
@ViewScoped
public class AuditingMB implements Serializable {

    @EJB
    private AuditingBO auditingBO;

    private ConsultaAuditoria consultaAuditoria = new ConsultaAuditoria();
    private List<Auditing> auditings;
    private TimelineModel timelineModel;
    private List<TabelaAuditoria> tabelas;
    private Auditing auditing;

    @PostConstruct
    public void init() {
        tabelas = auditingBO.getTabelasAuditoria();
    }

    public void onSelect(TimelineSelectEvent e) {
        TimelineEvent timelineEvent = e.getTimelineEvent();
        auditing = (Auditing) timelineEvent.getData();
    }

    public void limpar() {
        consultaAuditoria = new ConsultaAuditoria();
        timelineModel = null;
        auditings = null;
    }

    public void consultar() {
        timelineModel = null;
        auditings = null;
        try {
            auditings = auditingBO.consultar(consultaAuditoria);
            if (auditings == null || auditings.isEmpty()) {
                FacesMessageUtils.error("nenhumRegistro");
                return;
            }
            timelineModel = auditingBO.getTimelineModel(consultaAuditoria, auditings);
        } catch (BusinessException ex) {
            FacesMessageUtils.error(ex);
        }
    }

    public ConsultaAuditoria getConsultaAuditoria() {
        return consultaAuditoria;
    }

    public void setConsultaAuditoria(ConsultaAuditoria consultaAuditoria) {
        this.consultaAuditoria = consultaAuditoria;
    }

    public List<Auditing> getAuditings() {
        return auditings;
    }

    public void setAuditings(List<Auditing> auditings) {
        this.auditings = auditings;
    }

    public TimelineModel getTimelineModel() {
        return timelineModel;
    }

    public void setTimelineModel(TimelineModel timelineModel) {
        this.timelineModel = timelineModel;
    }

    public List<TabelaAuditoria> getTabelas() {
        return tabelas;
    }

    public void setTabelas(List<TabelaAuditoria> tabelas) {
        this.tabelas = tabelas;
    }

    public Auditing getAuditing() {
        return auditing;
    }

    public void setAuditing(Auditing auditing) {
        this.auditing = auditing;
    }

}
