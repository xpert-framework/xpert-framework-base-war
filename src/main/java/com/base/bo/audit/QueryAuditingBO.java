package com.base.bo.audit;

import com.base.dao.audit.QueryAuditingDAO;
import com.base.modelo.audit.QueryAuditing;
import com.base.vo.audit.ConsultaQueryAuditoria;
import com.xpert.core.exception.BusinessException;
import com.xpert.core.validation.DateValidation;
import com.xpert.core.validation.Validation;
import com.xpert.faces.primefaces.LazyDataModelImpl;
import com.xpert.persistence.query.JoinBuilder;
import com.xpert.persistence.query.Restrictions;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.joda.time.DateTime;

/**
 *
 * @author ayslanms
 */
@Stateless
public class QueryAuditingBO {

    @EJB
    private QueryAuditingDAO queryAuditingDAO;

    public LazyDataModelImpl<QueryAuditing> consultar(ConsultaQueryAuditoria consultaAuditoria) throws BusinessException {

        if (consultaAuditoria.getDataInicial() == null || consultaAuditoria.getDataFinal() == null) {
            throw new BusinessException("required.dataInicialDataFinal");
        }

        if (!DateValidation.validateDateRange(consultaAuditoria.getDataInicial(), consultaAuditoria.getDataFinal())) {
            throw new BusinessException("business.intervaloDataInvalido");
        }

        Restrictions restrictions = getRestrictions(consultaAuditoria, "q");
        LazyDataModelImpl<QueryAuditing> dataModel = new LazyDataModelImpl<>("startDate", restrictions, queryAuditingDAO);
        //joion fetch com usuario
        dataModel.setJoinBuilder(new JoinBuilder("q").leftJoinFetch("q.usuario", "u"));

        return dataModel;

    }

    /**
     * Retorna os restrictions a aprtir de uma instancia de ConsultaAuditoria
     *
     * @param consultaAuditoria
     * @return
     */
    public Restrictions getRestrictions(ConsultaQueryAuditoria consultaAuditoria) {
        return getRestrictions(consultaAuditoria, null);
    }

    /**
     * Retorna os restrictions a aprtir de uma instancia de ConsultaAuditoria
     *
     * @param consultaAuditoria
     * @param alias
     * @return
     */
    public Restrictions getRestrictions(ConsultaQueryAuditoria consultaAuditoria, String alias) {

        //quando enviar alias concatenar no String (exemplo a.usuario)
        if (alias == null) {
            alias = "";
        } else {
            alias = alias + ".";
        }

        Restrictions restrictions = new Restrictions();
        //Data
        if (consultaAuditoria.getDataInicial() != null) {
            restrictions.greaterEqualsThan(alias + "startDate", consultaAuditoria.getDataInicial());
        }
        if (consultaAuditoria.getDataFinal() != null) {
            //menor que o dia +1 para desprezar a hora/minuto/segundo
            restrictions.lessThan(alias + "startDate", new DateTime(consultaAuditoria.getDataFinal()).plusDays(1).toDate());
        }
        //Usuario
        if (consultaAuditoria.getUsuarios() != null && !consultaAuditoria.getUsuarios().isEmpty()) {
            restrictions.in(alias + "usuario", consultaAuditoria.getUsuarios());
        }
        //Tipo
        if (consultaAuditoria.getTipos() != null && !consultaAuditoria.getTipos().isEmpty()) {
            restrictions.in(alias + "auditingType", consultaAuditoria.getTipos());
        }
        //Tabela/Entidade
        //Nesse ponto pegar pelo campo "entity" ou direto no proprio SQL, ja que em alguns casos o campo "entity" nao estara preenchido
        if (consultaAuditoria.getTabelas() != null && !consultaAuditoria.getTabelas().isEmpty()) {
            //fazer o uppercase na consulta para abranger mais casos
            restrictions.in("UPPER(" + alias + "entity)", consultaAuditoria.getNomesTabelas());
        }
        //IP
        if (!Validation.isBlank(consultaAuditoria.getIp())) {
            restrictions.like(alias + "ip", consultaAuditoria.getIp());
        }
        //SQL Query
        if (!Validation.isBlank(consultaAuditoria.getSqlQuery())) {
            restrictions.like(alias + "sqlQuery", consultaAuditoria.getSqlQuery());
        }
        //SQL Parameters
        if (!Validation.isBlank(consultaAuditoria.getSqlParameters())) {
            restrictions.like(alias + "sqlParameters", consultaAuditoria.getSqlParameters());
        }
        //Tempo Consulta
        if (consultaAuditoria.getTempoConsultaInicial() != null) {
            restrictions.greaterEqualsThan(alias + "timeMilliseconds", consultaAuditoria.getTempoConsultaInicial());
        }
        if (consultaAuditoria.getTempoConsultaFinal() != null) {
            restrictions.lessEqualsThan(alias + "timeMilliseconds", consultaAuditoria.getTempoConsultaFinal());
        }
        //Total de Linhas
        if (consultaAuditoria.getTotalLinhasInicial() != null) {
            restrictions.greaterEqualsThan(alias + "rowsTotal", consultaAuditoria.getTotalLinhasInicial());
        }
        if (consultaAuditoria.getTotalLinhasFinal() != null) {
            restrictions.lessEqualsThan(alias + "rowsTotal", consultaAuditoria.getTotalLinhasFinal());
        }
        //Consulta Paginada
        if (consultaAuditoria.getConsultaPaginada() != null) {
            restrictions.add(alias + "paginatedQuery", consultaAuditoria.getConsultaPaginada());
        }
        //Consulta Com Query
        if (consultaAuditoria.getConsultaComParametros() != null) {
            restrictions.add(alias + "hasQueryParameter", consultaAuditoria.getConsultaComParametros());
        }
        //Identificador
        if (consultaAuditoria.getIdentificador() != null) {
            restrictions.add(alias + "identifier", consultaAuditoria.getIdentificador());
        }
        return restrictions;
    }

}
