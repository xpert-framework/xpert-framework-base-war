package com.base.vo.dashboard;

import com.base.util.Dashboards;
import com.base.vo.audit.ConsultaQueryAuditoria;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.pie.PieChartModel;

/**
 * Essa classe herda ConsultaQueryAuditoria que ja possui os atributos
 * necessarios para pegar os valores da tela e montar a consulta
 *
 * @author ayslanms
 */
public class DashboardAuditoriaConsulta extends ConsultaQueryAuditoria {

    /**
     * Resultado - Indicadores
     */
    private Long quantidadeConsultas;
    private Long quantidadeTabelas;
    private Long consultaComMaisLinhas;
    private Long consultaComMaiorTempo;
    /**
     * Reultado - Tabelas
     */
    private List<Object[]> consultasDia;
    private List<Object[]> consultasPaginadas;
    private List<Object[]> consultasParametros;
    private List<Object[]> consultasFaixaTempo;
    private List<Object[]> consultasFaixaLinhas;
    private List<Object[]> consultasFaixaHorario;
    private List<Object[]> consultasTabela;
    private List<Object[]> consultasUsuario;
    /**
     * Resultado - Graficos
     */
    private LineChartModel graficoConsultasDia;
    private PieChartModel graficoConsultasPaginadas;
    private PieChartModel graficoConsultasParametros;
    private BarChartModel graficoConsultasFaixaTempo;
    private BarChartModel graficoConsultasTabela;
    private BarChartModel graficoConsultasFaixaLinhas;
    private BarChartModel graficoConsultasFaixaHorario;
    private BarChartModel graficoConsultasUsuario;
    
    public String getConsultaComMaiorTempoFormatada(){
        
        if(consultaComMaiorTempo != null){
            return Dashboards.getDuracao(consultaComMaiorTempo);
        }
        return "-";
    }

    /**
     * Retorna a quantidade de dias entre a dataInicio e a dataFim
     *
     * @return
     */
    public Integer getIntervaloDias() {

        DateTime inicio = new DateTime(getDataInicial());
        DateTime fim = new DateTime(getDataFinal());

        return Days.daysBetween(inicio, fim).getDays();
    }

    public List<Object[]> getConsultasFaixaLinhas() {
        return consultasFaixaLinhas;
    }

    public void setConsultasFaixaLinhas(List<Object[]> consultasFaixaLinhas) {
        this.consultasFaixaLinhas = consultasFaixaLinhas;
    }

    public BarChartModel getGraficoConsultasFaixaLinhas() {
        return graficoConsultasFaixaLinhas;
    }

    public void setGraficoConsultasFaixaLinhas(BarChartModel graficoConsultasFaixaLinhas) {
        this.graficoConsultasFaixaLinhas = graficoConsultasFaixaLinhas;
    }
    
    

    public List<Object[]> getConsultasDia() {
        return consultasDia;
    }

    public void setConsultasDia(List<Object[]> consultasDia) {
        this.consultasDia = consultasDia;
    }

    public LineChartModel getGraficoConsultasDia() {
        return graficoConsultasDia;
    }

    public void setGraficoConsultasDia(LineChartModel graficoConsultasDia) {
        this.graficoConsultasDia = graficoConsultasDia;
    }

    public Long getQuantidadeConsultas() {
        return quantidadeConsultas;
    }

    public void setQuantidadeConsultas(Long quantidadeConsultas) {
        this.quantidadeConsultas = quantidadeConsultas;
    }

    public Long getQuantidadeTabelas() {
        return quantidadeTabelas;
    }

    public void setQuantidadeTabelas(Long quantidadeTabelas) {
        this.quantidadeTabelas = quantidadeTabelas;
    }

    public Long getConsultaComMaisLinhas() {
        return consultaComMaisLinhas;
    }

    public void setConsultaComMaisLinhas(Long consultaComMaisLinhas) {
        this.consultaComMaisLinhas = consultaComMaisLinhas;
    }

    public Long getConsultaComMaiorTempo() {
        return consultaComMaiorTempo;
    }

    public void setConsultaComMaiorTempo(Long consultaComMaiorTempo) {
        this.consultaComMaiorTempo = consultaComMaiorTempo;
    }

    public List<Object[]> getConsultasPaginadas() {
        return consultasPaginadas;
    }

    public void setConsultasPaginadas(List<Object[]> consultasPaginadas) {
        this.consultasPaginadas = consultasPaginadas;
    }

    public List<Object[]> getConsultasParametros() {
        return consultasParametros;
    }

    public void setConsultasParametros(List<Object[]> consultasParametros) {
        this.consultasParametros = consultasParametros;
    }

    public List<Object[]> getConsultasFaixaTempo() {
        return consultasFaixaTempo;
    }

    public void setConsultasFaixaTempo(List<Object[]> consultasFaixaTempo) {
        this.consultasFaixaTempo = consultasFaixaTempo;
    }

    public List<Object[]> getConsultasFaixaHorario() {
        return consultasFaixaHorario;
    }

    public void setConsultasFaixaHorario(List<Object[]> consultasFaixaHorario) {
        this.consultasFaixaHorario = consultasFaixaHorario;
    }

    public List<Object[]> getConsultasTabela() {
        return consultasTabela;
    }

    public void setConsultasTabela(List<Object[]> consultasTabela) {
        this.consultasTabela = consultasTabela;
    }

    public List<Object[]> getConsultasUsuario() {
        return consultasUsuario;
    }

    public void setConsultasUsuario(List<Object[]> consultasUsuario) {
        this.consultasUsuario = consultasUsuario;
    }

    public PieChartModel getGraficoConsultasPaginadas() {
        return graficoConsultasPaginadas;
    }

    public void setGraficoConsultasPaginadas(PieChartModel graficoConsultasPaginadas) {
        this.graficoConsultasPaginadas = graficoConsultasPaginadas;
    }

    public PieChartModel getGraficoConsultasParametros() {
        return graficoConsultasParametros;
    }

    public void setGraficoConsultasParametros(PieChartModel graficoConsultasParametros) {
        this.graficoConsultasParametros = graficoConsultasParametros;
    }

    public BarChartModel getGraficoConsultasFaixaTempo() {
        return graficoConsultasFaixaTempo;
    }

    public void setGraficoConsultasFaixaTempo(BarChartModel graficoConsultasFaixaTempo) {
        this.graficoConsultasFaixaTempo = graficoConsultasFaixaTempo;
    }

    public BarChartModel getGraficoConsultasTabela() {
        return graficoConsultasTabela;
    }

    public void setGraficoConsultasTabela(BarChartModel graficoConsultasTabela) {
        this.graficoConsultasTabela = graficoConsultasTabela;
    }

    public BarChartModel getGraficoConsultasFaixaHorario() {
        return graficoConsultasFaixaHorario;
    }

    public void setGraficoConsultasFaixaHorario(BarChartModel graficoConsultasFaixaHorario) {
        this.graficoConsultasFaixaHorario = graficoConsultasFaixaHorario;
    }

    public BarChartModel getGraficoConsultasUsuario() {
        return graficoConsultasUsuario;
    }

    public void setGraficoConsultasUsuario(BarChartModel graficoConsultasUsuario) {
        this.graficoConsultasUsuario = graficoConsultasUsuario;
    }

}
