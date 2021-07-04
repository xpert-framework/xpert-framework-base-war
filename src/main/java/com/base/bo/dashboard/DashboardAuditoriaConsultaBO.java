package com.base.bo.dashboard;

import com.base.bo.audit.QueryAuditingBO;
import com.base.constante.Constantes;
import com.base.dao.DAO;
import com.base.modelo.audit.QueryAuditing;
import com.base.util.Dashboards;
import com.base.vo.dashboard.DashboardAuditoriaConsulta;
import com.xpert.core.exception.BusinessException;
import com.xpert.core.validation.DateValidation;
import com.xpert.i18n.I18N;
import com.xpert.persistence.query.QueryBuilder;
import static com.xpert.persistence.query.Sql.*;
import com.xpert.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.primefaces.model.charts.ChartModel;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 * Objeto de Negocio para o Dashboard de auditoria de consultas
 *
 * @author ayslanms
 */
@Stateless
public class DashboardAuditoriaConsultaBO {

    @EJB
    private DAO dao;

    @EJB
    private QueryAuditingBO queryAuditingBO;

    /**
     * Retorna o total de consultas por tipo
     *
     * @param dashboardAuditoria
     * @return
     */
    public Long getQuantidadeConsultas(DashboardAuditoriaConsulta dashboardAuditoria) {
        return dao.getQueryBuilder()
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .count();
    }

    /**
     * Retorna o total de tabelas consutadas
     *
     * @param dashboardAuditoria
     * @return
     */
    public Long getQuantidadeTabelas(DashboardAuditoriaConsulta dashboardAuditoria) {
        return dao.getQueryBuilder()
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .countDistinct("entity");
    }

    /**
     * Retorna a maior quantidade de linhas nas consultas conforme os parametros
     * informados
     *
     * @param dashboardAuditoria
     * @return
     */
    public Long getConsultaComMaisLinhas(DashboardAuditoriaConsulta dashboardAuditoria) {
        return (Long) dao.getQueryBuilder()
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .max("rowsTotal");
    }

    /**
     * Retorna a maior tempo nas consultas conforme os parametros informados
     *
     * @param dashboardAuditoria
     * @return
     */
    public Long getConsultaComMaiorTempo(DashboardAuditoriaConsulta dashboardAuditoria) {
        return (Long) dao.getQueryBuilder()
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .max("timeMilliseconds");
    }

    /**
     * Retorna a lista de quantidade de consultas paginadas ou nao
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasPaginadas(DashboardAuditoriaConsulta dashboardAuditoria) {
        /**
         * Passo 1 - pegar o resultado da consulta
         */
        List<Object[]> result = getResultadoAgrupado("paginatedQuery", dashboardAuditoria, false);

        /**
         * Passo 2 - Formatar resultado para fica no padrao
         */
        for (Object[] linha : result) {
            //pegar o boolean e converter a descrição
            Boolean paginatedQuery = (Boolean) linha[0];
            linha[0] = (paginatedQuery == true ? "Paginada" : "Não Paginada");
        }
        return result;
    }

    /**
     * Retorna a lista de consultas que possuem ou nao parametros
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasParametro(DashboardAuditoriaConsulta dashboardAuditoria) {
        /**
         * Passo 1 - pegar o resultado da consulta
         */
        List<Object[]> result = getResultadoAgrupado("hasQueryParameter", dashboardAuditoria, false);

        /**
         * Passo 2 - Formatar resultado para fica no padrao
         */
        for (Object[] linha : result) {
            //pegar o boolean e converter a descrição
            Boolean hasQueryParameter = (Boolean) linha[0];
            linha[0] = (hasQueryParameter == true ? "Com Parâmetros" : "Sem Parâmetros");
        }
        return result;
    }

    /**
     * Retorna a lista de quantidade de consultas por usuario
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasFaixaHorario(DashboardAuditoriaConsulta dashboardAuditoria) {
        /**
         * Aqui tem que ordenar pela hora, pois ordenar por String vai aparecer
         * 1h-2h, 10h-11h Passo 1 - Trazer a hora do banco de dados.
         *
         * A media eh o count/total de dias do intervalo
         *
         */
        List<Object[]> result = dao.getQueryBuilder()
                .by("HOUR(startDate)")
                .aggregate(
                        count("*"),
                        count("*") + "/" + dashboardAuditoria.getIntervaloDias()
                )
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .getResultList();

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
     * Retorna a lista de quantidade de consultas por dia
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasDia(DashboardAuditoriaConsulta dashboardAuditoria) {

        //calculo do tipo de intervalo
        String field = Charts.getGroupByTempo("startDate", dashboardAuditoria.getDataInicial(), dashboardAuditoria.getDataFinal());

        QueryBuilder queryBuilder = dao.getQueryBuilder()
                .by(field)
                .aggregate(
                        count("*", "total"),
                        max("rowsTotal"),
                        avg("rowsTotal"),
                        max("timeMilliseconds"),
                        avg("timeMilliseconds"))
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria));

        return queryBuilder.getResultList();
    }

    /**
     * Retorna a lista de quantidade de consultas por usuario
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasUsuario(DashboardAuditoriaConsulta dashboardAuditoria) {
        QueryBuilder queryBuilder = dao.getQueryBuilder()
                .by("COALESCE(usuario.userLogin, 'NÃO INFORMADO')")
                .aggregate(
                        count("a", "total"),
                        max("a.rowsTotal"),
                        avg("a.rowsTotal"),
                        max("a.timeMilliseconds"),
                        avg("a.timeMilliseconds")
                )
                .from(QueryAuditing.class, "a")
                .leftJoin("a.usuario", "usuario")
                .orderBy("total")
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria, "a"));

        return queryBuilder.getResultList();
    }

    /**
     * Retorna a lista de quantidade de erros por usuario
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasTabela(DashboardAuditoriaConsulta dashboardAuditoria) {

        QueryBuilder queryBuilder = dao.getQueryBuilder()
                .by("entity")
                .aggregate(
                        count("*", "total"),
                        max("rowsTotal"),
                        avg("rowsTotal"),
                        max("timeMilliseconds"),
                        avg("timeMilliseconds")
                )
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .orderBy("total");

        List<Object[]> result = queryBuilder.getResultList();

        //tratar nome da classe
        for (Object[] linha : result) {
            linha[0] = (String) linha[0] + " (" + I18N.get(StringUtils.getLowerFirstLetter((String) linha[0])) + ")";
        }
        return result;
    }

    /**
     * Retorna a lista de quantidade de consultas por faixa de linhas retornadas
     * na consulta
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasFaixaLinhas(DashboardAuditoriaConsulta dashboardAuditoria) {
        List<Object[]> result = dao.getQueryBuilder()
                .by("CASE "
                        + " WHEN COALESCE(rowsTotal,0) = 0 THEN '1. Nenhum (0 linhas)' "
                        + " WHEN rowsTotal <= 100    THEN '2. Entre 1 e 100 linhas' "
                        + " WHEN rowsTotal <= 1000   THEN '3. Entre 101 e 1.000 linhas' "
                        + " WHEN rowsTotal <= 10000  THEN '4. Entre 1.001 e 10.000 linhas' "
                        + " WHEN rowsTotal <= 50000  THEN '5. Entre 10.001 e 50.000 linhas' "
                        + " WHEN rowsTotal <= 100000 THEN '6. Entre 50.001 e 100.000 linhas' "
                        + " ELSE '7. Acima de 100.001 linhas' END "
                )
                .aggregate(
                        count("*"),
                        max("timeMilliseconds"),
                        avg("timeMilliseconds")
                )
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .getResultList();

        return result;
    }

    /**
     * Retorna a lista de quantidade de consultas por faixa de linhas retornadas
     * na consulta
     *
     * @param dashboardAuditoria
     * @return
     */
    public List<Object[]> getConsultasFaixaTempo(DashboardAuditoriaConsulta dashboardAuditoria) {
        List<Object[]> result = dao.getQueryBuilder()
                .by("CASE "
                        + " WHEN timeMilliseconds <= 10     THEN '1. Até 10 miliseg.' "
                        + " WHEN timeMilliseconds <= 100    THEN '2. Entre 11 e 100 miliseg.' "
                        + " WHEN timeMilliseconds <= 1000   THEN '3. Entre 101 e 1.000 miliseg.' "
                        + " WHEN timeMilliseconds <= 10000  THEN '4. Entre 1 e 10 segundos' "
                        + " WHEN timeMilliseconds <= 60000  THEN '5. Entre 11 e 60 segundos' "
                        + " WHEN timeMilliseconds <= 120000 THEN '6. Entre 1 e 2 minutos' "
                        + " WHEN timeMilliseconds <= 300000 THEN '7. Entre 2 e 5 minutos' "
                        + " ELSE '8. Acima de 5 minutos' END "
                )
                .aggregate(
                        count("*"),
                        max("rowsTotal"),
                        avg("rowsTotal")
                )
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria))
                .getResultList();

        return result;
    }

    /**
     * Retorna o grafico de erros por tabela
     *
     * @param dashboardAuditoria
     * @return
     */
    public BarChartModel getGraficoConsultasFaixaHorario(DashboardAuditoriaConsulta dashboardAuditoria) {
        List<Number> valuesQuantidade = new ArrayList<>();
        List<Number> valuesMedia = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAuditoria.getConsultasFaixaHorario()) {
            labels.add(linha[0].toString());
            valuesQuantidade.add(((Number) linha[1]).longValue());
            valuesMedia.add(((Number) linha[2]).longValue());
        }

        return Charts.getGraficoQuantidadeMedia(valuesQuantidade, valuesMedia, labels, dashboardAuditoria.getIntervaloDias());
    }

    /**
     * Retorna o grafico de consultas por dia
     *
     * @param dashboardAuditoria
     * @return
     */
    public LineChartModel getGraficoConsultasDia(DashboardAuditoriaConsulta dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getConsultasDia(), LineChartModel.class);
    }

    /**
     * Retorna o grafico de consultas de auditoria por usuario
     *
     * @param dashboardAuditoria
     * @param limite
     * @return
     */
    public BarChartModel getGraficoConsultasUsuario(DashboardAuditoriaConsulta dashboardAuditoria, Integer limite) {
        String titulo = limite + " usuário(s) com mais consultas no sistema";
        return getGraficoGenerico(titulo, dashboardAuditoria.getConsultasUsuario(), BarChartModel.class, limite);
    }

    /**
     * Retorna o grafico de consultas por tabela
     *
     * @param dashboardAuditoria
     * @param limite
     * @return
     */
    public BarChartModel getGraficoConsultasTabela(DashboardAuditoriaConsulta dashboardAuditoria, Integer limite) {
        String titulo = limite + " tabela(s) com mais consultas no sistema";
        return getGraficoGenerico(titulo, dashboardAuditoria.getConsultasTabela(), BarChartModel.class, limite);
    }

    /**
     * Retorna o grafico de consultas pagaindas ou nao
     *
     * @param dashboardAuditoria
     * @return
     */
    public PieChartModel getGraficoConsultasPaginadas(DashboardAuditoriaConsulta dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getConsultasPaginadas(), PieChartModel.class);
    }

    /**
     * Retorna o grafico de consultas pagaindas ou nao
     *
     * @param dashboardAuditoria
     * @return
     */
    public PieChartModel getGraficoConsultasParametros(DashboardAuditoriaConsulta dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getConsultasParametros(), PieChartModel.class);
    }

    /**
     * Retorna o grafico de consultas por faixa de linhas retornadas
     *
     * @param dashboardAuditoria
     * @return
     */
    public BarChartModel getGraficoConsultasFaixaLinhas(DashboardAuditoriaConsulta dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getConsultasFaixaLinhas(), BarChartModel.class);
    }

    /**
     * Retorna o grafico de consultas por faixa de linhas retornadas
     *
     * @param dashboardAuditoria
     * @return
     */
    public BarChartModel getGraficoConsultasFaixaTempo(DashboardAuditoriaConsulta dashboardAuditoria) {
        return getGraficoGenerico(null, dashboardAuditoria.getConsultasFaixaTempo(), BarChartModel.class);
    }

    /**
     * Retorna o grafico generico pois os graficos por dia, usuario e tabela tem
     * o mesmo retorno: contagem de consultas
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
     * o mesmo retorno: contagem de consultas
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
            valuesTotal.add(((Number) linha[1]).longValue());
        }

        if (chartType.equals(LineChartModel.class)) {
            List<LineChartDataSet> dataSets = new ArrayList<>();
            dataSets.add(Charts.getLineChartDataSet("Total", Charts.COLOR_SERIE_1, valuesTotal));
            return (T) Charts.getLineChartModel(dataSets, labels);
        } else if (chartType.equals(BarChartModel.class)) {
            List<BarChartDataSet> dataSets = new ArrayList<>();
            dataSets.add(Charts.getBarChartDataSet("Total", Charts.COLOR_SERIE_1, valuesTotal));
            return (T) Charts.getBarChartModel(dataSets, titulo, labels);
        } else if (chartType.equals(PieChartModel.class)) {
            return (T) Charts.getPieChartModel(valuesTotal, labels);
        } else {
            throw new IllegalArgumentException("Tipo " + chartType + " nao foi implementado");
        }

    }

    /**
     * COnsulta generica agrupada que conta a quantidade de consultas agrupada
     * por um determinado campo
     *
     * @param groupBy
     * @param dashboardAuditoria
     * @param ordernarPorMaior
     * @return
     */
    public List<Object[]> getResultadoAgrupado(String groupBy, DashboardAuditoriaConsulta dashboardAuditoria, boolean ordernarPorMaior) {
        QueryBuilder queryBuilder = dao.getQueryBuilder()
                .by(groupBy)
                .aggregate(
                        count("*", "total"))
                .from(QueryAuditing.class)
                .add(queryAuditingBO.getRestrictions(dashboardAuditoria));

        //ordernar pelo (total)
        if (ordernarPorMaior) {
            queryBuilder.orderBy("total");
        }
        return queryBuilder.getResultList();
    }

    /**
     * Cria o objeto DashboardAuditoria com dataInicial (data autal -1 mês) e
     * dataFinal (data atual) ja setados
     *
     * @return
     * @throws BusinessException
     */
    public DashboardAuditoriaConsulta getDashboardAuditoria() throws BusinessException {

        DashboardAuditoriaConsulta dashboardAuditoria = new DashboardAuditoriaConsulta();
        //por padrao ele vem com o ultimo mes carregado
        dashboardAuditoria.setDataInicial(Dashboards.getDataAtualMenosUmMes());
        dashboardAuditoria.setDataFinal(Dashboards.getDataAtual());

        //montar indicadores
        carregarDashboardAuditoria(dashboardAuditoria);

        return dashboardAuditoria;
    }

    public void carregarDashboardAuditoria(DashboardAuditoriaConsulta dashboardAuditoria) throws BusinessException {

        if (dashboardAuditoria.getDataInicial() == null || dashboardAuditoria.getDataFinal() == null) {
            throw new BusinessException("required.dataInicialDataFinal");
        }

        if (!DateValidation.validateDateRange(dashboardAuditoria.getDataInicial(), dashboardAuditoria.getDataFinal())) {
            throw new BusinessException("business.intervaloDataInvalido");
        }

        /**
         * Carregar indicadores
         */
        dashboardAuditoria.setQuantidadeConsultas(getQuantidadeConsultas(dashboardAuditoria));
        dashboardAuditoria.setQuantidadeTabelas(getQuantidadeTabelas(dashboardAuditoria));
        dashboardAuditoria.setConsultaComMaiorTempo(getConsultaComMaiorTempo(dashboardAuditoria));
        dashboardAuditoria.setConsultaComMaisLinhas(getConsultaComMaisLinhas(dashboardAuditoria));
        /**
         * Carregar tabelas que serao usadas nos graficos
         */
        dashboardAuditoria.setConsultasFaixaHorario(getConsultasFaixaHorario(dashboardAuditoria));
        dashboardAuditoria.setConsultasUsuario(getConsultasUsuario(dashboardAuditoria));
        dashboardAuditoria.setConsultasTabela(getConsultasTabela(dashboardAuditoria));
        dashboardAuditoria.setConsultasDia(getConsultasDia(dashboardAuditoria));
        dashboardAuditoria.setConsultasPaginadas(getConsultasPaginadas(dashboardAuditoria));
        dashboardAuditoria.setConsultasParametros(getConsultasParametro(dashboardAuditoria));
        dashboardAuditoria.setConsultasFaixaLinhas(getConsultasFaixaLinhas(dashboardAuditoria));
        dashboardAuditoria.setConsultasFaixaTempo(getConsultasFaixaTempo(dashboardAuditoria));

        /**
         * Carregar graficos
         */
        dashboardAuditoria.setGraficoConsultasFaixaHorario(getGraficoConsultasFaixaHorario(dashboardAuditoria));
        dashboardAuditoria.setGraficoConsultasUsuario(getGraficoConsultasUsuario(dashboardAuditoria, Constantes.QUANTIDADE_LIMITE_REGISTROS_GRAFICOS_DASHBOARD));
        dashboardAuditoria.setGraficoConsultasTabela(getGraficoConsultasTabela(dashboardAuditoria, Constantes.QUANTIDADE_LIMITE_REGISTROS_GRAFICOS_DASHBOARD));
        dashboardAuditoria.setGraficoConsultasDia(getGraficoConsultasDia(dashboardAuditoria));
        dashboardAuditoria.setGraficoConsultasPaginadas(getGraficoConsultasPaginadas(dashboardAuditoria));
        dashboardAuditoria.setGraficoConsultasParametros(getGraficoConsultasParametros(dashboardAuditoria));
        dashboardAuditoria.setGraficoConsultasFaixaLinhas(getGraficoConsultasFaixaLinhas(dashboardAuditoria));
        dashboardAuditoria.setGraficoConsultasFaixaTempo(getGraficoConsultasFaixaTempo(dashboardAuditoria));

    }

}
