package com.base.vo.dashboard;

import com.base.vo.audit.TabelaAuditoria;
import com.base.modelo.controleacesso.Usuario;
import com.xpert.audit.model.AuditingType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 *
 * @author ayslanms
 */
public class DashboardAuditoria {

    /**
     * Filtros
     */
    private Date dataInicial;
    private Date dataFinal;
    private List<Usuario> usuarios;
    private List<TabelaAuditoria> tabelas;
    private List<AuditingType> tipos;
    /**
     * Resultado - Indicadores
     */
    private Long quantidadeEventos;
    private Long quantidadeInsert;
    private Long quantidadeUpdate;
    private Long quantidadeDelete;
    /**
     * Reultado - Tabelas
     */
    private List<Object[]> eventosDia;
    private List<Object[]> eventosUsuario;
    private List<Object[]> eventosTabela;
    private List<Object[]> eventosFaixaHorario;
    /**
     * Resultado - Graficos
     */
    private PieChartModel graficoEventosTipo;
    private LineChartModel graficoEventosDia;
    private BarChartModel graficoEventosUsuario;
    private BarChartModel graficoEventosTabela;
    private BarChartModel graficoEventosFaixaHorario;

    /**
     * Retorna a quantidade de dias entre a dataInicio e a dataFim
     *
     * @return
     */
    public Integer getIntervaloDias() {

        DateTime inicio = new DateTime(dataInicial);
        DateTime fim = new DateTime(dataFinal);

        return Days.daysBetween(inicio, fim).getDays();
    }

    /**
     * Retorna a lista de nomes das tabelas
     *
     * @return
     */
    public List<String> getNomesTabelas() {
        return TabelaAuditoria.getNomesTabelas(tabelas);
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<TabelaAuditoria> getTabelas() {
        return tabelas;
    }

    public void setTabelas(List<TabelaAuditoria> tabelas) {
        this.tabelas = tabelas;
    }

    public List<AuditingType> getTipos() {
        return tipos;
    }

    public void setTipos(List<AuditingType> tipos) {
        this.tipos = tipos;
    }

    public Long getQuantidadeEventos() {
        return quantidadeEventos;
    }

    public void setQuantidadeEventos(Long quantidadeEventos) {
        this.quantidadeEventos = quantidadeEventos;
    }

    public Long getQuantidadeInsert() {
        return quantidadeInsert;
    }

    public void setQuantidadeInsert(Long quantidadeInsert) {
        this.quantidadeInsert = quantidadeInsert;
    }

    public Long getQuantidadeUpdate() {
        return quantidadeUpdate;
    }

    public void setQuantidadeUpdate(Long quantidadeUpdate) {
        this.quantidadeUpdate = quantidadeUpdate;
    }

    public Long getQuantidadeDelete() {
        return quantidadeDelete;
    }

    public void setQuantidadeDelete(Long quantidadeDelete) {
        this.quantidadeDelete = quantidadeDelete;
    }

    public List<Object[]> getEventosDia() {
        return eventosDia;
    }

    public void setEventosDia(List<Object[]> eventosDia) {
        this.eventosDia = eventosDia;
    }

    public List<Object[]> getEventosUsuario() {
        return eventosUsuario;
    }

    public void setEventosUsuario(List<Object[]> eventosUsuario) {
        this.eventosUsuario = eventosUsuario;
    }

    public List<Object[]> getEventosTabela() {
        return eventosTabela;
    }

    public void setEventosTabela(List<Object[]> eventosTabela) {
        this.eventosTabela = eventosTabela;
    }

    public List<Object[]> getEventosFaixaHorario() {
        return eventosFaixaHorario;
    }

    public void setEventosFaixaHorario(List<Object[]> eventosFaixaHorario) {
        this.eventosFaixaHorario = eventosFaixaHorario;
    }

    public LineChartModel getGraficoEventosDia() {
        return graficoEventosDia;
    }

    public void setGraficoEventosDia(LineChartModel graficoEventosDia) {
        this.graficoEventosDia = graficoEventosDia;
    }

    public BarChartModel getGraficoEventosUsuario() {
        return graficoEventosUsuario;
    }

    public void setGraficoEventosUsuario(BarChartModel graficoEventosUsuario) {
        this.graficoEventosUsuario = graficoEventosUsuario;
    }

    public BarChartModel getGraficoEventosTabela() {
        return graficoEventosTabela;
    }

    public void setGraficoEventosTabela(BarChartModel graficoEventosTabela) {
        this.graficoEventosTabela = graficoEventosTabela;
    }

    public BarChartModel getGraficoEventosFaixaHorario() {
        return graficoEventosFaixaHorario;
    }

    public void setGraficoEventosFaixaHorario(BarChartModel graficoEventosFaixaHorario) {
        this.graficoEventosFaixaHorario = graficoEventosFaixaHorario;
    }

    public PieChartModel getGraficoEventosTipo() {
        return graficoEventosTipo;
    }

    public void setGraficoEventosTipo(PieChartModel graficoEventosTipo) {
        this.graficoEventosTipo = graficoEventosTipo;
    }

}
