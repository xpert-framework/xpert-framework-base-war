package com.base.bo.audit;

import com.base.constante.Constantes;
import com.base.dao.audit.AuditingDAO;
import com.base.modelo.audit.Auditing;
import com.base.modelo.audit.Metadata;
import com.base.modelo.controleacesso.Usuario;
import com.base.vo.audit.ConsultaAuditoria;
import com.base.vo.audit.TabelaAuditoria;
import com.xpert.audit.NotAudited;
import com.xpert.core.exception.BusinessException;
import com.xpert.core.validation.DateValidation;
import com.xpert.i18n.I18N;
import com.xpert.persistence.query.Restrictions;
import com.xpert.persistence.utils.EntityUtils;
import com.xpert.utils.CollectionsUtils;
import com.xpert.utils.StringUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.joda.time.DateTime;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineGroup;
import org.primefaces.model.timeline.TimelineModel;

/**
 *
 * @author ayslanms
 */
@Stateless
public class AuditingBO {
    
    @EJB
    private AuditingDAO auditingDAO;

    /**
     * Retrona uma lista de TabelaAuditoria
     *
     * @return
     */
    public List<TabelaAuditoria> getTabelasAuditoria() {
        List<Class> classes = EntityUtils.getMappedEntities(auditingDAO.getEntityManager());
        List<TabelaAuditoria> tabelas = new ArrayList<>();
        for (Class entity : classes) {

            //excluir anotadas com NotAudited e as proprias classes de auditoria
            if (!entity.isAnnotationPresent(NotAudited.class)
                    && !entity.equals(Auditing.class)
                    && !entity.equals(Metadata.class)) {

                TabelaAuditoria tabelaAuditoria = new TabelaAuditoria();
                tabelaAuditoria.setEntity(entity);
                //pegar do messages a tradução da classe
                tabelaAuditoria.setNome(I18N.get(StringUtils.getLowerFirstLetter(entity.getSimpleName())));
                tabelas.add(tabelaAuditoria);
            }
        }

        CollectionsUtils.order(tabelas, "nome");

        return tabelas;
    }

    public TimelineModel getTimelineModel(ConsultaAuditoria consultaAuditoria, List<Auditing> auditings) {

        TimelineModel model = new TimelineModel();

        if (consultaAuditoria.isAgruparLinhaTempoUsuario()) {
            //criar agrupamento por usuario
            Set<Usuario> usersSet = new HashSet<>();
            for (Auditing auditing : auditings) {
                usersSet.add(auditing.getUsuario());
            }

            for (Usuario usuario : usersSet) {
                if (usuario != null) {
                    model.addGroup(new TimelineGroup(usuario.getId().toString(), usuario));
                } else {
                    Usuario nullUser = new Usuario();
                    nullUser.setId(-1L);
                    nullUser.setNome("NÃO INFORMADO");
                    nullUser.setUserLogin("NÃO INFORMADO");
                    model.addGroup(new TimelineGroup(nullUser.getId().toString(), nullUser));
                }
            }
        }

        for (Auditing auditing : auditings) {
            TimelineEvent timelineEvent = new TimelineEvent();
            timelineEvent.setData(auditing);
            timelineEvent.setStartDate(auditing.getEventDate());
            timelineEvent.setStyleClass(auditing.getAuditingType().name().toLowerCase());
            if (consultaAuditoria.isAgruparLinhaTempoUsuario()) {
                timelineEvent.setGroup(auditing.getUsuario().getId().toString());
            }
            model.add(timelineEvent);
        }

        return model;
    }

    public List<Auditing> consultar(ConsultaAuditoria consultaAuditoria) throws BusinessException {

        if (consultaAuditoria.getDataInicial() == null || consultaAuditoria.getDataFinal() == null) {
            throw new BusinessException("required.dataInicialDataFinal");
        }

        if (!DateValidation.validateDateRange(consultaAuditoria.getDataInicial(), consultaAuditoria.getDataFinal())) {
            throw new BusinessException("business.intervaloDataInvalido");
        }
        if (consultaAuditoria.getUsuarios() == null || consultaAuditoria.getUsuarios().isEmpty()) {
            throw new BusinessException("required.usuario");
        }

        Restrictions restrictions = getRestrictions(consultaAuditoria);
        Long total = auditingDAO.count(restrictions, true);
        if (total >= Constantes.QUANTIDADE_MAXIMA_LISTA_AUDITORIA) {
            throw new BusinessException("Foram encontrados " + total + " registros. É necessário informar mais filtros.");
        }

        return auditingDAO.list(restrictions, "eventDate", true);

    }

    /**
     * Retorna os restrictions a aprtir de uma instancia de ConsultaAuditoria
     *
     * @param consultaAuditoria
     * @return
     */
    public Restrictions getRestrictions(ConsultaAuditoria consultaAuditoria) {

        Restrictions restrictions = new Restrictions();
        if (consultaAuditoria.getDataInicial() != null) {
            restrictions.greaterEqualsThan("eventDate", consultaAuditoria.getDataInicial());
        }
        if (consultaAuditoria.getDataFinal() != null) {
            //menor que o dia +1 para desprezar a hora/minuto/segundo
            restrictions.lessThan("eventDate", new DateTime(consultaAuditoria.getDataFinal()).plusDays(1).toDate());
        }
        if (consultaAuditoria.getUsuarios() != null && !consultaAuditoria.getUsuarios().isEmpty()) {
            restrictions.in("usuario", consultaAuditoria.getUsuarios());
        }
        if (consultaAuditoria.getTipos() != null && !consultaAuditoria.getTipos().isEmpty()) {
            restrictions.in("auditingType", consultaAuditoria.getTipos());
        }
        if (consultaAuditoria.getTabelas() != null && !consultaAuditoria.getTabelas().isEmpty()) {
            restrictions.in("entity", consultaAuditoria.getNomesTabelas());
        }
        return restrictions;
    }

}
