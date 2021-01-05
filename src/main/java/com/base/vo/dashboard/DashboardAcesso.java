package com.base.vo.dashboard;

import com.base.modelo.controleacesso.Usuario;
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
public class DashboardAcesso {

    /**
     * Filtros
     */
    private Date dataInicial;
    private Date dataFinal;
    private List<Usuario> usuarios;

    /**
     * Resultado - Indicadores
     */
    private Long quantidadeUsuariosAtivos;
    private Long quantidadePerfisAtivos;
    private Long quantidadePermissoesAtivas;
    private Long quantidadeAcessos;

    /**
     * Reultado - Tabelas
     */
    private List<Object[]> acessosDia;
    private List<Object[]> usuariosSituacao;
    private List<Object[]> usuariosSenhaCadastrada;
    private List<Object[]> usuariosPerfil;
    private List<Object[]> acessosFaixaHorario;
    private List<Object[]> acessosUsuario;

    /**
     * Resultado - Graficos
     */
    private LineChartModel graficoAcessosDia;
    private PieChartModel graficoUsuariosSituacao;
    private PieChartModel graficoUsuariosSenhaCadastrada;
    private BarChartModel graficoUsuariosPerfil;
    private BarChartModel graficoAcessosUsuario;
    private BarChartModel graficoAcessosFaixaHorario;
    private BarChartModel graficoAcessosFaixaHorarioMedia;

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

    public List<Object[]> getAcessosUsuario() {
        return acessosUsuario;
    }

    public void setAcessosUsuario(List<Object[]> acessosUsuario) {
        this.acessosUsuario = acessosUsuario;
    }

    public BarChartModel getGraficoAcessosUsuario() {
        return graficoAcessosUsuario;
    }

    public void setGraficoAcessosUsuario(BarChartModel graficoAcessosUsuario) {
        this.graficoAcessosUsuario = graficoAcessosUsuario;
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

    public Long getQuantidadeUsuariosAtivos() {
        return quantidadeUsuariosAtivos;
    }

    public void setQuantidadeUsuariosAtivos(Long quantidadeUsuariosAtivos) {
        this.quantidadeUsuariosAtivos = quantidadeUsuariosAtivos;
    }

    public Long getQuantidadePerfisAtivos() {
        return quantidadePerfisAtivos;
    }

    public void setQuantidadePerfisAtivos(Long quantidadePerfisAtivos) {
        this.quantidadePerfisAtivos = quantidadePerfisAtivos;
    }

    public Long getQuantidadePermissoesAtivas() {
        return quantidadePermissoesAtivas;
    }

    public void setQuantidadePermissoesAtivas(Long quantidadePermissoesAtivas) {
        this.quantidadePermissoesAtivas = quantidadePermissoesAtivas;
    }

    public Long getQuantidadeAcessos() {
        return quantidadeAcessos;
    }

    public void setQuantidadeAcessos(Long quantidadeAcessos) {
        this.quantidadeAcessos = quantidadeAcessos;
    }

    public LineChartModel getGraficoAcessosDia() {
        return graficoAcessosDia;
    }

    public void setGraficoAcessosDia(LineChartModel graficoAcessosDia) {
        this.graficoAcessosDia = graficoAcessosDia;
    }

    public PieChartModel getGraficoUsuariosSituacao() {
        return graficoUsuariosSituacao;
    }

    public void setGraficoUsuariosSituacao(PieChartModel graficoUsuariosSituacao) {
        this.graficoUsuariosSituacao = graficoUsuariosSituacao;
    }

    public List<Object[]> getAcessosDia() {
        return acessosDia;
    }

    public void setAcessosDia(List<Object[]> acessosDia) {
        this.acessosDia = acessosDia;
    }

    public List<Object[]> getUsuariosSituacao() {
        return usuariosSituacao;
    }

    public void setUsuariosSituacao(List<Object[]> usuariosSituacao) {
        this.usuariosSituacao = usuariosSituacao;
    }

    public List<Object[]> getUsuariosSenhaCadastrada() {
        return usuariosSenhaCadastrada;
    }

    public void setUsuariosSenhaCadastrada(List<Object[]> usuariosSenhaCadastrada) {
        this.usuariosSenhaCadastrada = usuariosSenhaCadastrada;
    }

    public List<Object[]> getUsuariosPerfil() {
        return usuariosPerfil;
    }

    public void setUsuariosPerfil(List<Object[]> usuariosPerfil) {
        this.usuariosPerfil = usuariosPerfil;
    }

    public PieChartModel getGraficoUsuariosSenhaCadastrada() {
        return graficoUsuariosSenhaCadastrada;
    }

    public void setGraficoUsuariosSenhaCadastrada(PieChartModel graficoUsuariosSenhaCadastrada) {
        this.graficoUsuariosSenhaCadastrada = graficoUsuariosSenhaCadastrada;
    }

    public BarChartModel getGraficoUsuariosPerfil() {
        return graficoUsuariosPerfil;
    }

    public void setGraficoUsuariosPerfil(BarChartModel graficoUsuariosPerfil) {
        this.graficoUsuariosPerfil = graficoUsuariosPerfil;
    }

    public List<Object[]> getAcessosFaixaHorario() {
        return acessosFaixaHorario;
    }

    public void setAcessosFaixaHorario(List<Object[]> acessosFaixaHorario) {
        this.acessosFaixaHorario = acessosFaixaHorario;
    }

    public BarChartModel getGraficoAcessosFaixaHorario() {
        return graficoAcessosFaixaHorario;
    }

    public void setGraficoAcessosFaixaHorario(BarChartModel graficoAcessosFaixaHorario) {
        this.graficoAcessosFaixaHorario = graficoAcessosFaixaHorario;
    }

    public BarChartModel getGraficoAcessosFaixaHorarioMedia() {
        return graficoAcessosFaixaHorarioMedia;
    }

    public void setGraficoAcessosFaixaHorarioMedia(BarChartModel graficoAcessosFaixaHorarioMedia) {
        this.graficoAcessosFaixaHorarioMedia = graficoAcessosFaixaHorarioMedia;
    }


}
