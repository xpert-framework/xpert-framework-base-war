package com.base.bo.dashboard;

import com.base.constante.Constantes;
import com.base.dao.DAO;
import com.base.modelo.controleacesso.AcessoSistema;
import com.base.modelo.controleacesso.Perfil;
import com.base.modelo.controleacesso.Permissao;
import com.base.modelo.controleacesso.SituacaoUsuario;
import com.base.modelo.controleacesso.Usuario;
import com.base.util.Dashboards;
import com.base.vo.dashboard.DashboardAcesso;
import com.xpert.core.exception.BusinessException;
import com.xpert.core.validation.DateValidation;
import com.xpert.persistence.query.Restrictions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.joda.time.DateTime;
import org.primefaces.model.charts.line.LineChartModel;
import static com.xpert.persistence.query.Sql.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 * Objeto de Negocio para o Dashboard de acessos
 *
 * @author ayslanms
 */
@Stateless
public class DashboardAcessoBO {

    @EJB
    private DAO dao;

    /**
     * Retorna a quantidade de usuarios ativos
     *
     * @return
     */
    public Long getQuantidadeUsuariosAtivos() {
        return dao.getQueryBuilder()
                .from(Usuario.class)
                .add("situacaoUsuario", SituacaoUsuario.ATIVO)
                .count();
    }

    /**
     * Retorna a quantidade de perfis ativos
     *
     * @return
     */
    public Long getQuantidadePerfisAtivos() {
        return dao.getQueryBuilder()
                .from(Perfil.class)
                .add("ativo", true)
                .count();
    }

    /**
     * Retorna a quantidade de permissoes ativos
     *
     * @return
     */
    public Long getQuantidadePermissoesAtivas() {
        return dao.getQueryBuilder()
                .from(Permissao.class)
                .add("ativo", true)
                .count();
    }

    /**
     * Retorna a quantidade de acessos
     *
     * @param dashboardAcesso
     * @return
     */
    public Long getQuantidadeAcessos(DashboardAcesso dashboardAcesso) {
        return dao.getQueryBuilder()
                .from(AcessoSistema.class)
                .add(getRestrictions(dashboardAcesso))
                .count();
    }

    /**
     * Retorna a lista de quantidade de acessos por dia
     *
     * @param dashboardAcesso
     * @return
     */
    public List<Object[]> getAcessosDia(DashboardAcesso dashboardAcesso) {

        //calculo do tipo de intervalo
        String field = Charts.getGroupByTempo("dataHora", dashboardAcesso.getDataInicial(), dashboardAcesso.getDataFinal());

        return dao.getQueryBuilder()
                .by(field)
                .aggregate(count("*"))
                .from(AcessoSistema.class)
                .add(getRestrictions(dashboardAcesso))
                .getResultList();
    }

    /**
     * Retorna a quantidade de usuarios por situacao
     *
     * @param dashboardAcesso
     * @return
     */
    public List<Object[]> getUsuariosSituacao(DashboardAcesso dashboardAcesso) {
        return dao.getQueryBuilder()
                .by("situacaoUsuario")
                .aggregate(count("*"))
                .from(Usuario.class)
                .getResultList();
    }

    /**
     * Retorna a lista de quantidade de usuarios por senha cadastrada
     *
     * @param dashboardAcesso
     * @return
     */
    public List<Object[]> getUsuariosSenhaCadastrada(DashboardAcesso dashboardAcesso) {
        return dao.getQueryBuilder()
                .by("senhaCadastrada")
                .aggregate(count("*"))
                .from(Usuario.class)
                .getResultList();
    }

    /**
     * Retorna oa lista de Quantidade de Usuarios por perfil
     *
     * @param dashboardAcesso
     * @return
     */
    public List<Object[]> getUsuariosPerfil(DashboardAcesso dashboardAcesso) {
        return dao.getQueryBuilder()
                .by("pe.descricao")
                .aggregate(countDistinct("u.id"))
                .from(Usuario.class, "u")
                .innerJoin("u.perfis", "pe")
                .orderBy("2")
                .getResultList();
    }

    /**
     * Retorna a lista de quantidade de acessos por faixa de horario (01h-02h,
     * 02h-03h, etc..)
     *
     * @param dashboardAcesso
     * @return
     */
    public List<Object[]> getAcessosFaixaHorario(DashboardAcesso dashboardAcesso) {

        /**
         * Aqui tem que ordenar pela hora, pois ordenar por String vai aparecer
         * 1h-2h, 10h-11h Passo 1 - Trazer a hora do banco de dados.
         *
         * A media eh o count/total de dias do intervalo
         *
         */
        List<Object[]> result = dao.getQueryBuilder()
                .by("HOUR(dataHora)")
                .aggregate(
                        count("*"),
                        count("*") + "/" + dashboardAcesso.getIntervaloDias()
                )
                .from(AcessoSistema.class)
                .add(getRestrictions(dashboardAcesso))
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
     * Retorna a lista de quantidade de acessos por usuario
     *
     * @param dashboardAcesso
     * @return
     */
    public List<Object[]> getAcessosUsuario(DashboardAcesso dashboardAcesso) {
        return dao.getQueryBuilder()
                .by("usuario.userLogin")
                .aggregate(count("*"), min("dataHora"), max("dataHora"))
                .from(AcessoSistema.class)
                .orderBy("2")
                .add(getRestrictions(dashboardAcesso))
                .getResultList();
    }

    /**
     * Retorna o grafico de acessos por dia
     *
     * @param dashboardAcesso
     * @return
     */
    public LineChartModel getGraficoAcessosDia(DashboardAcesso dashboardAcesso) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAcesso.getAcessosDia()) {
            if (linha[0] instanceof Date) {
                labels.add(new SimpleDateFormat("dd/MM/yyyy").format((Date) linha[0]));
            } else {
                labels.add(linha[0].toString());
            }
            values.add(((Number) linha[1]).longValue());
        }

        return Charts.getLineChartModel("Quantidade de Acessos", values, labels);
    }

    /**
     * Retorna o grafico de acessos por faixa de horario
     *
     * @param dashboardAcesso
     * @return
     */
    public BarChartModel getGraficoAcessosFaixaHorario(DashboardAcesso dashboardAcesso) {
        List<Number> valuesQuantidade = new ArrayList<>();
        List<Number> valuesMedia = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAcesso.getAcessosFaixaHorario()) {
            labels.add(linha[0].toString());
            valuesQuantidade.add(((Number) linha[1]).longValue());
            valuesMedia.add(((Number) linha[2]).longValue());
        }

        return Charts.getGraficoQuantidadeMedia(valuesQuantidade, valuesMedia, labels, dashboardAcesso.getIntervaloDias());
    }

    /**
     * Retorna o grafico de media acessos por faixa de horario
     *
     * @param dashboardAcesso
     * @return
     */
    public BarChartModel getGraficoAcessosFaixaHorarioMedia(DashboardAcesso dashboardAcesso) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAcesso.getAcessosFaixaHorario()) {
            labels.add(linha[0].toString());
            values.add(((Number) linha[2]).longValue());
        }

        return Charts.getBarChartModel(null, values, labels);
    }

    /**
     * Retorna o grafico de Usuarios por Situacao
     *
     * @param dashboardAcesso
     * @return
     */
    public PieChartModel getGraficoUsuariosSituacao(DashboardAcesso dashboardAcesso) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAcesso.getUsuariosSituacao()) {
            labels.add(((SituacaoUsuario) linha[0]).getDescricao());
            values.add(((Number) linha[1]).longValue());
        }

        return Charts.getPieChartModel(values, labels);
    }

    /**
     * Reotorna o grafico de usuarios por senha cadastrada
     *
     * @param dashboardAcesso
     * @return
     */
    public PieChartModel getGraficoUsuariosSenhaCadastrada(DashboardAcesso dashboardAcesso) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAcesso.getUsuariosSenhaCadastrada()) {
            labels.add((boolean) linha[0] == true ? "Senha Cadastrada" : "Senha Não Cadastrada");
            values.add(((Number) linha[1]).longValue());
        }

        return Charts.getPieChartModel(values, labels);
    }

    /**
     * Retorna o grafico de quantidade de usuarios por perfil
     *
     * @param dashboardAcesso
     * @return
     */
    public BarChartModel getGraficoUsuariosPerfil(DashboardAcesso dashboardAcesso) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] linha : dashboardAcesso.getUsuariosPerfil()) {
            labels.add(linha[0].toString());
            values.add(((Number) linha[1]).longValue());
        }

        return Charts.getBarChartModel(null, values, labels);
    }

    /**
     * Retorna o grafico de quantidade de usuarios por perfil
     *
     * @param dashboardAcesso
     * @param limite
     * @return
     */
    public BarChartModel getGraficoAcessosUsuario(DashboardAcesso dashboardAcesso, int limite) {
        List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        Collections.reverse(dashboardAcesso.getAcessosUsuario());

        int count = 1;
        for (Object[] linha : dashboardAcesso.getAcessosUsuario()) {
            if (count <= limite) {
                labels.add(linha[0].toString());
                values.add(((Number) linha[1]).longValue());
            }
            count++;
        }

        Collections.reverse(labels);
        Collections.reverse(values);

        String titulo = limite + " usuário(s) com mais acessos no sistema";

        return Charts.getBarChartModel(titulo, values, labels);
    }

    /**
     * Cria o objeto DashboardAcesso com dataInicial (data autal -1 mês) e
     * dataFinal (data atual) ja setados
     *
     * @return
     * @throws BusinessException
     */
    public DashboardAcesso getDashboardAcesso() throws BusinessException {

        DashboardAcesso dashboardAcesso = new DashboardAcesso();
        //por padrao ele vem com o ultimo mes carregado
        dashboardAcesso.setDataInicial(Dashboards.getDataAtualMenosUmMes());
        dashboardAcesso.setDataFinal(Dashboards.getDataAtual());

        //montar indicadores
        carregarDashboardAcesso(dashboardAcesso);

        return dashboardAcesso;
    }

    /**
     * Carrega as informações do dashboard
     *
     * @param dashboardAcesso
     * @throws BusinessException
     */
    public void carregarDashboardAcesso(DashboardAcesso dashboardAcesso) throws BusinessException {

        if (dashboardAcesso.getDataInicial() == null || dashboardAcesso.getDataFinal() == null) {
            throw new BusinessException("required.dataInicialDataFinal");
        }

        if (!DateValidation.validateDateRange(dashboardAcesso.getDataInicial(), dashboardAcesso.getDataFinal())) {
            throw new BusinessException("business.intervaloDataInvalido");
        }

        /**
         * Carregar indicadores
         */
        dashboardAcesso.setQuantidadeUsuariosAtivos(getQuantidadeUsuariosAtivos());
        dashboardAcesso.setQuantidadePerfisAtivos(getQuantidadePerfisAtivos());
        dashboardAcesso.setQuantidadePermissoesAtivas(getQuantidadePermissoesAtivas());
        dashboardAcesso.setQuantidadeAcessos(getQuantidadeAcessos(dashboardAcesso));

        /**
         * Carregar tabelas que serao usadas nos graficos
         */
        dashboardAcesso.setAcessosDia(getAcessosDia(dashboardAcesso));
        dashboardAcesso.setUsuariosSituacao(getUsuariosSituacao(dashboardAcesso));
        dashboardAcesso.setUsuariosSenhaCadastrada(getUsuariosSenhaCadastrada(dashboardAcesso));
        dashboardAcesso.setUsuariosPerfil(getUsuariosPerfil(dashboardAcesso));
        dashboardAcesso.setAcessosFaixaHorario(getAcessosFaixaHorario(dashboardAcesso));
        dashboardAcesso.setAcessosUsuario(getAcessosUsuario(dashboardAcesso));

        /**
         * Carregar graficos
         */
        dashboardAcesso.setGraficoAcessosDia(getGraficoAcessosDia(dashboardAcesso));
        dashboardAcesso.setGraficoUsuariosSituacao(getGraficoUsuariosSituacao(dashboardAcesso));
        dashboardAcesso.setGraficoUsuariosSenhaCadastrada(getGraficoUsuariosSenhaCadastrada(dashboardAcesso));
        dashboardAcesso.setGraficoUsuariosPerfil(getGraficoUsuariosPerfil(dashboardAcesso));
        dashboardAcesso.setGraficoAcessosFaixaHorario(getGraficoAcessosFaixaHorario(dashboardAcesso));
        dashboardAcesso.setGraficoAcessosFaixaHorarioMedia(getGraficoAcessosFaixaHorarioMedia(dashboardAcesso));
        dashboardAcesso.setGraficoAcessosUsuario(getGraficoAcessosUsuario(dashboardAcesso, Constantes.QUANTIDADE_LIMITE_REGISTROS_GRAFICOS_DASHBOARD));

    }

    public Restrictions getRestrictions(DashboardAcesso dashboardAcesso) {

        Restrictions restrictions = new Restrictions();
        if (dashboardAcesso.getDataInicial() != null) {
            restrictions.greaterEqualsThan("dataHora", dashboardAcesso.getDataInicial());
        }
        if (dashboardAcesso.getDataFinal() != null) {
            //menor que o dia +1 para desprezar a hora/minuto/segundo
            restrictions.lessThan("dataHora", new DateTime(dashboardAcesso.getDataFinal()).plusDays(1).toDate());
        }
        if (dashboardAcesso.getUsuarios() != null && !dashboardAcesso.getUsuarios().isEmpty()) {
            restrictions.in("usuario", dashboardAcesso.getUsuarios());
        }
        return restrictions;
    }

}
