package com.base.bo.dashboard;

import com.base.dao.DAO;
import com.base.modelo.audit.Auditing;
import com.base.modelo.audit.Metadata;
import com.base.vo.dashboard.DashboardAuditoria;
import com.base.vo.audit.TabelaAuditoria;
import com.xpert.audit.NotAudited;
import static com.xpert.persistence.query.Sql.*;
import com.xpert.audit.model.AuditingType;
import com.xpert.core.exception.BusinessException;
import com.xpert.core.validation.DateValidation;
import com.xpert.i18n.I18N;
import com.xpert.persistence.query.QueryBuilder;
import com.xpert.persistence.query.Restrictions;
import com.xpert.persistence.utils.EntityUtils;
import com.xpert.utils.CollectionsUtils;
import com.xpert.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.joda.time.DateTime;
import org.primefaces.model.charts.ChartModel;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 *
 * @author ayslanms
 */
@Stateless
public class DashboardAuditoriaBO {

    @EJB
    private DAO dao;

    /**
     * Retorna o total de eventos por tipo
     *
     * @param dashboardAuditoria
     * @param auditingType
     * @return
     */
    public Long getQuantidadeEventos(DashboardAuditoria dashboardAuditoria, AuditingType auditingType) {
        return dao.getQueryBuilder()
                .from(Auditing.class)
                .add(getRestrictions(dashboardAuditoria))
                .add("auditingType", auditingType)
                .count();
    }

    /**
     * COnsulta generica agrupada que suma INSERT, UPDATE, DELETE e TOTAL
     *
     * @param groupBy
     * @param dashboardAuditoria
     * @param ordernarPorMaior
     * @return
     */
    public List<Object[]> getResultadoAgrupado(String groupBy, DashboardAuditoria dashboardAuditoria, boolean ordernarPorMaior) {
        QueryBuilder queryBuilder = dao.getQueryBuilder()
                .by(groupBy)
                .aggregate(
                        sum("CASE WHEN auditingType = 'INSERT' THEN 1 ELSE 0 END"),
                        sum("CASE WHEN auditingType = 'UPDATE' THEN 1 ELSE 0 END"),
                        sum("CASE WHEN auditingType = 'DELETE' THEN 1 ELSE 0 END"),
                        count("*"))
                .from(Auditing.class)
                .add(getRestrictions(dashboardAuditoria));

        //ordernar pela posicao 5 (total)
        if (ordernarPorMaior) {
            queryBuilder.orderBy("5");
        }
        return queryBuilder.getResultList();
    }

    /**
     * Retorna a lista de quantidade de erros por dia
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getEventosDia(DashboardAuditoria dashboardAuditoria) {

        //calculo do tipo de intervalo
        String field = Charts.getGroupByTempo("eventDate", dashboardAuditoria.getDataInicial(), dashboardAuditoria.getDataFinal());

        return getResultadoAgrupado(field, dashboardAuditoria, false);
    }

    /**
     * Retorna a lista de quantidade de erros por usuario
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getEventosUsuario(DashboardAuditoria dashboardAuditoria) {
        QueryBuilder queryBuilder = dao.getQueryBuilder()
                .by("COALESCE(usuario.userLogin, 'NÃO INFORMADO')")
                .aggregate(
                        sum("CASE WHEN auditingType = 'INSERT' THEN 1 ELSE 0 END"),
                        sum("CASE WHEN auditingType = 'UPDATE' THEN 1 ELSE 0 END"),
                        sum("CASE WHEN auditingType = 'DELETE' THEN 1 ELSE 0 END"),
                        count("a"))
                .from(Auditing.class, "a")
                .leftJoin("a.usuario", "usuario")
                .orderBy("5")
                .add(getRestrictions(dashboardAuditoria, "a"));

        return queryBuilder.getResultList();
    }

    /**
     * Retorna a lista de quantidade de erros por usuario
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getEventosTabela(DashboardAuditoria dashboardAuditoria) {
        List<Object[]> result = getResultadoAgrupado("entity", dashboardAuditoria, true);

        //tratar nome da classe
        for (Object[] linha : result) {
            linha[0] = I18N.get(StringUtils.getLowerFirstLetter((String) linha[0]));
        }
        return result;
    }

    /**
     * Retorna a lista de quantidade de erros por usuario
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getEventosFaixaHorario(DashboardAuditoria dashboardAuditoria) {
        /**
         * Aqui tem que ordenar pela hora, pois ordenar por String vai aparecer
         * 1h-2h, 10h-11h Passo 1 - Trazer a hora do banco de dados
         */
        List<Object[]> result = getResultadoAgrupado("HOUR(eventDate)", dashboardAuditoria, false);

        /**
         * Passo 2 - Formatar resultado para fica no padrao 01h-02h
         */
        for (Object[] linha : result) {
            Number hora = (Number) linha[0];
            linha[0] = Charts.getFaixaHorario(hora);
        }
        return result;
    }

    /**
     * Retorna o grafico generico pois os graficos por dia, usuario e tabela tem
     * o mesmo retorno
     *
     * @param <T>
     * @param titulo
     * @param result
     * @param chartType
     * @return
     */
    public <T extends ChartModel> T getGraficoGenerico(String titulo, List<Object[]> result, Class chartType) {

        return getGraficoGenerico(titulo, result, chartType, null);
    }

    /**
     * Retorna o grafico generico pois os graficos por dia, usuario e tabela tem
     * o mesmo retorno: Insercao, Alteracao, Exclusao e Total
     *
     * @param <T>
     * @param titulo
     * @param result
     * @param chartType
     * @param limite Indica a quantidade limite de dados para exibir no grafico
     * @return
     */
    public <T extends ChartModel> T getGraficoGenerico(String titulo, List<Object[]> result, Class chartType, Integer limite) {

        //o grafico de linhas vai possuir uma serie pra cada tipo
        List<Number> valuesInsert = new ArrayList<>();
        List<Number> valuesUpdate = new ArrayList<>();
        List<Number> valuesDetele = new ArrayList<>();
        List<Number> valuesTotal = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        //caso exista limite pegar os ultimos
        if (limite != null && result.size() > limite) {
            result = result.subList(result.size() - limite, result.size());
        }

        for (Object[] linha : result) {
            if (linha[0] instanceof Date) {
                labels.add(new SimpleDateFormat("dd/MM/yyyy").format((Date) linha[0]));
            } else {
                labels.add(linha[0].toString());
            }
            valuesInsert.add(((Number) linha[1]).longValue());
            valuesUpdate.add(((Number) linha[2]).longValue());
            valuesDetele.add(((Number) linha[3]).longValue());
            valuesTotal.add(((Number) linha[4]).longValue());
        }

        if (chartType.equals(LineChartModel.class)) {
            List<LineChartDataSet> dataSets = new ArrayList<>();
            dataSets.add(Charts.getLineChartDataSet("Inclusão", Charts.COLOR_SERIE_1, valuesInsert));
            dataSets.add(Charts.getLineChartDataSet("Alteração", Charts.COLOR_SERIE_2, valuesUpdate));
            dataSets.add(Charts.getLineChartDataSet("Exclusão", Charts.COLOR_SERIE_3, valuesDetele));
            dataSets.add(Charts.getLineChartDataSet("Total", Charts.COLOR_SERIE_4, valuesTotal));
            return (T) Charts.getLineChartModel(dataSets, labels);
        } else if (chartType.equals(BarChartModel.class)) {
            List<BarChartDataSet> dataSets = new ArrayList<>();
            dataSets.add(Charts.getBarChartDataSet("Inclusão", Charts.COLOR_SERIE_1, valuesInsert));
            dataSets.add(Charts.getBarChartDataSet("Alteração", Charts.COLOR_SERIE_2, valuesUpdate));
            dataSets.add(Charts.getBarChartDataSet("Exclusão", Charts.COLOR_SERIE_3, valuesDetele));
//            dataSets.add(Charts.getBarChartDataSet("Total", Charts.COLOR_SERIE_4, valuesTotal));
            return (T) Charts.getBarChartModel(dataSets, titulo, labels);
        } else {
            throw new IllegalArgumentException("Tipo " + chartType + " nao foi implementado");
        }

    }

    /**
     * Retorna o grafico de erros por dia
     *
     * @param dashboardAuditoria
     * @return
     */
    public LineChartModel getGraficoEventosDia(DashboardAuditoria dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getEventosDia(), LineChartModel.class);
    }

    /**
     * Retorna o grafico de erros por usuario
     *
     * @param dashboardAuditoria
     * @param limite
     * @return
     */
    public BarChartModel getGraficoEventosUsuario(DashboardAuditoria dashboardAuditoria, Integer limite) {
        String titulo = limite + " usuário(s) com mais eventos no sistema";
        return getGraficoGenerico(titulo, dashboardAuditoria.getEventosUsuario(), BarChartModel.class, limite);
    }

    /**
     * Retorna o grafico de erros por tabela
     *
     * @param dashboardAuditoria
     * @param limite
     * @return
     */
    public BarChartModel getGraficoEventosTabela(DashboardAuditoria dashboardAuditoria, Integer limite) {
        String titulo = limite + " tabela(s) com mais eventos no sistema";
        return getGraficoGenerico(titulo, dashboardAuditoria.getEventosTabela(), BarChartModel.class, limite);
    }

    /**
     * Retorna o grafico de erros por tabela
     *
     * @param dashboardAuditoria
     * @return
     */
    public BarChartModel getGraficoEventosFaixaHorario(DashboardAuditoria dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getEventosFaixaHorario(), BarChartModel.class);
    }

    /**
     * Retorna o grafico de Eventos por Tipo. Como ja se tem os indicadores
     * individuais, entao nao consultar o banco novamente
     *
     * @param dashboardAuditoria
     * @return
     */
    public PieChartModel getGraficoEventosTipo(DashboardAuditoria dashboardAuditoria) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        labels.add("Inclusão");
        labels.add("Alteração");
        labels.add("Exclusão");

        values.add(dashboardAuditoria.getQuantidadeInsert());
        values.add(dashboardAuditoria.getQuantidadeUpdate());
        values.add(dashboardAuditoria.getQuantidadeDelete());

        return Charts.getPieChartModel(values, labels);
    }

    /**
     * Cria o objeto DashboardAuditoria com dataInicial (data autal -1 mês) e
     * dataFinal (data atual) ja setados
     *
     * @return
     * @throws BusinessException
     */
    public DashboardAuditoria getDashboardAuditoria() throws BusinessException {

        DashboardAuditoria dashboardAuditoria = new DashboardAuditoria();
        //por padrao ele vem com o ultimo mes carregado
        dashboardAuditoria.setDataInicial(new DateTime().plusMonths(-1).toDate());
        dashboardAuditoria.setDataFinal(new Date());

        //montar indicadores
        carregarDashboardAuditoria(dashboardAuditoria);

        return dashboardAuditoria;
    }

    /**
     * Carrega as informações do dashboard
     *
     * @param dashboardAuditoria
     * @throws BusinessException
     */
    public void carregarDashboardAuditoria(DashboardAuditoria dashboardAuditoria) throws BusinessException {

        if (dashboardAuditoria.getDataInicial() == null || dashboardAuditoria.getDataFinal() == null) {
            throw new BusinessException("required.dataInicialDataFinal");
        }

        if (!DateValidation.validateDateRange(dashboardAuditoria.getDataInicial(), dashboardAuditoria.getDataFinal())) {
            throw new BusinessException("business.intervaloDataInvalido");
        }

        /**
         * Carregar indicadores
         */
        dashboardAuditoria.setQuantidadeInsert(getQuantidadeEventos(dashboardAuditoria, AuditingType.INSERT));
        dashboardAuditoria.setQuantidadeUpdate(getQuantidadeEventos(dashboardAuditoria, AuditingType.UPDATE));
        dashboardAuditoria.setQuantidadeDelete(getQuantidadeEventos(dashboardAuditoria, AuditingType.DELETE));
        //total é a soma (para nao fazer outra consulta)
        dashboardAuditoria.setQuantidadeEventos(dashboardAuditoria.getQuantidadeInsert() + dashboardAuditoria.getQuantidadeUpdate() + dashboardAuditoria.getQuantidadeDelete());

        /**
         * Carregar tabelas que serao usadas nos graficos
         */
        dashboardAuditoria.setEventosDia(getEventosDia(dashboardAuditoria));
        dashboardAuditoria.setEventosFaixaHorario(getEventosFaixaHorario(dashboardAuditoria));
        dashboardAuditoria.setEventosTabela(getEventosTabela(dashboardAuditoria));
        dashboardAuditoria.setEventosUsuario(getEventosUsuario(dashboardAuditoria));

        /**
         * Carregar graficos
         */
        dashboardAuditoria.setGraficoEventosDia(getGraficoEventosDia(dashboardAuditoria));
        dashboardAuditoria.setGraficoEventosFaixaHorario(getGraficoEventosFaixaHorario(dashboardAuditoria));
        dashboardAuditoria.setGraficoEventosTabela(getGraficoEventosTabela(dashboardAuditoria, 20));
        dashboardAuditoria.setGraficoEventosUsuario(getGraficoEventosUsuario(dashboardAuditoria, 20));
        dashboardAuditoria.setGraficoEventosTipo(getGraficoEventosTipo(dashboardAuditoria));

    }

    /**
     * Retorna as restricoes do objeto
     *
     * @param dashboardAuditoria
     * @return
     */
    public Restrictions getRestrictions(DashboardAuditoria dashboardAuditoria) {
        return getRestrictions(dashboardAuditoria, null);
    }

    /**
     * Retorna as restricoes do objeto (nesse metodo pode-se passar o alias)
     *
     * @param dashboardAuditoria
     * @param alias
     * @return
     */
    public Restrictions getRestrictions(DashboardAuditoria dashboardAuditoria, String alias) {

        //quando enviar alias concatenar no String (exemplo a.usuario)
        if (alias == null) {
            alias = "";
        } else {
            alias = alias + ".";
        }

        Restrictions restrictions = new Restrictions();
        if (dashboardAuditoria.getDataInicial() != null) {
            restrictions.greaterEqualsThan(alias + "eventDate", dashboardAuditoria.getDataInicial());
        }
        if (dashboardAuditoria.getDataFinal() != null) {
            //menor que o dia +1 para desprezar a hora/minuto/segundo
            restrictions.lessThan(alias + "eventDate", new DateTime(dashboardAuditoria.getDataFinal()).plusDays(1).toDate());
        }
        if (dashboardAuditoria.getUsuarios() != null && !dashboardAuditoria.getUsuarios().isEmpty()) {
            restrictions.in(alias + "usuario", dashboardAuditoria.getUsuarios());
        }
        if (dashboardAuditoria.getTipos() != null && !dashboardAuditoria.getTipos().isEmpty()) {
            restrictions.in(alias + "auditingType", dashboardAuditoria.getTipos());
        }
        if (dashboardAuditoria.getTabelas() != null && !dashboardAuditoria.getTabelas().isEmpty()) {
            restrictions.in(alias + "entity", dashboardAuditoria.getNomesTabelas());
        }
        return restrictions;
    }

}
