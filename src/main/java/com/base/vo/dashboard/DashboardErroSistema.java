package com.base.vo.dashboard;

import com.base.modelo.controleacesso.Usuario;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.line.LineChartModel;

/**
 *
 * @author ayslanms
 */
public class DashboardErroSistema {

    /**
     * Filtros
     */
    private Date dataInicial;
    private Date dataFinal;
    private List<Usuario> usuarios;

    /**
     * Resultado - Indicadores
     */
    private Long quantidadaTotalErros;
    private Long quantidadeErros;
    private Date dataUltimoErro;

    /**
     * Reultado - Tabelas
     */
    private List<Object[]> errosDia;
    private List<Object[]> errosFaixaHorario;
    private List<Object[]> errosUsuario;
    private List<Object[]> errosFuncionalidade;

    /**
     * Resultado - Graficos
     */
    private BarChartModel graficoErrosDia;
    private BarChartModel graficoErrosUsuario;
    private BarChartModel graficoErrosFaixaHorario;
    private BarChartModel graficoErrosFuncionalidade;

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

    public Long getQuantidadaTotalErros() {
        return quantidadaTotalErros;
    }

    public void setQuantidadaTotalErros(Long quantidadaTotalErros) {
        this.quantidadaTotalErros = quantidadaTotalErros;
    }

    public Long getQuantidadeErros() {
        return quantidadeErros;
    }

    public void setQuantidadeErros(Long quantidadeErros) {
        this.quantidadeErros = quantidadeErros;
    }

    public Date getDataUltimoErro() {
        return dataUltimoErro;
    }

    public void setDataUltimoErro(Date dataUltimoErro) {
        this.dataUltimoErro = dataUltimoErro;
    }

    public List<Object[]> getErrosDia() {
        return errosDia;
    }

    public void setErrosDia(List<Object[]> errosDia) {
        this.errosDia = errosDia;
    }

    public List<Object[]> getErrosFaixaHorario() {
        return errosFaixaHorario;
    }

    public void setErrosFaixaHorario(List<Object[]> errosFaixaHorario) {
        this.errosFaixaHorario = errosFaixaHorario;
    }

    public List<Object[]> getErrosUsuario() {
        return errosUsuario;
    }

    public void setErrosUsuario(List<Object[]> errosUsuario) {
        this.errosUsuario = errosUsuario;
    }

    public List<Object[]> getErrosFuncionalidade() {
        return errosFuncionalidade;
    }

    public void setErrosFuncionalidade(List<Object[]> errosFuncionalidade) {
        this.errosFuncionalidade = errosFuncionalidade;
    }

    public BarChartModel getGraficoErrosDia() {
        return graficoErrosDia;
    }

    public void setGraficoErrosDia(BarChartModel graficoErrosDia) {
        this.graficoErrosDia = graficoErrosDia;
    }

    public BarChartModel getGraficoErrosUsuario() {
        return graficoErrosUsuario;
    }

    public void setGraficoErrosUsuario(BarChartModel graficoErrosUsuario) {
        this.graficoErrosUsuario = graficoErrosUsuario;
    }

    public BarChartModel getGraficoErrosFaixaHorario() {
        return graficoErrosFaixaHorario;
    }

    public void setGraficoErrosFaixaHorario(BarChartModel graficoErrosFaixaHorario) {
        this.graficoErrosFaixaHorario = graficoErrosFaixaHorario;
    }

    public BarChartModel getGraficoErrosFuncionalidade() {
        return graficoErrosFuncionalidade;
    }

    public void setGraficoErrosFuncionalidade(BarChartModel graficoErrosFuncionalidade) {
        this.graficoErrosFuncionalidade = graficoErrosFuncionalidade;
    }

}
