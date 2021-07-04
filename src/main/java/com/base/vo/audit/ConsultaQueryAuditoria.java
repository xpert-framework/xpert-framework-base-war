package com.base.vo.audit;

import com.base.modelo.controleacesso.Usuario;
import com.xpert.audit.model.QueryAuditingType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ayslanms
 */
public class ConsultaQueryAuditoria {

    /**
     * Filtros
     */
    private Date dataInicial;
    private Date dataFinal;
    private List<Usuario> usuarios;
    private List<TabelaAuditoria> tabelas;
    private List<QueryAuditingType> tipos;

    private String ip;

    private Boolean consultaPaginada;
    private Boolean consultaComParametros;

    private Long totalLinhasInicial;
    private Long totalLinhasFinal;

    private Long tempoConsultaInicial;
    private Long tempoConsultaFinal;

    private String sqlQuery;
    private String sqlParameters;

    private Long identificador;

    /**
     * Retorna a lista de nomes das tabelas
     *
     * @return
     */
    public List<String> getNomesTabelas() {
        //copiar e cocnverter para uppercase
        List<String> entidades = TabelaAuditoria.getNomesTabelas(tabelas);
        List<String> entidadesUpper = new ArrayList<>();
        for (String entidade : entidades) {
            entidadesUpper.add(entidade.toUpperCase());
        }
        return entidadesUpper;
    }

    public Long getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Long identificador) {
        this.identificador = identificador;
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

    public List<QueryAuditingType> getTipos() {
        return tipos;
    }

    public void setTipos(List<QueryAuditingType> tipos) {
        this.tipos = tipos;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Boolean getConsultaPaginada() {
        return consultaPaginada;
    }

    public void setConsultaPaginada(Boolean consultaPaginada) {
        this.consultaPaginada = consultaPaginada;
    }

    public Boolean getConsultaComParametros() {
        return consultaComParametros;
    }

    public void setConsultaComParametros(Boolean consultaComParametros) {
        this.consultaComParametros = consultaComParametros;
    }

    public Long getTotalLinhasInicial() {
        return totalLinhasInicial;
    }

    public void setTotalLinhasInicial(Long totalLinhasInicial) {
        this.totalLinhasInicial = totalLinhasInicial;
    }

    public Long getTotalLinhasFinal() {
        return totalLinhasFinal;
    }

    public void setTotalLinhasFinal(Long totalLinhasFinal) {
        this.totalLinhasFinal = totalLinhasFinal;
    }

    public Long getTempoConsultaInicial() {
        return tempoConsultaInicial;
    }

    public void setTempoConsultaInicial(Long tempoConsultaInicial) {
        this.tempoConsultaInicial = tempoConsultaInicial;
    }

    public Long getTempoConsultaFinal() {
        return tempoConsultaFinal;
    }

    public void setTempoConsultaFinal(Long tempoConsultaFinal) {
        this.tempoConsultaFinal = tempoConsultaFinal;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlParameters() {
        return sqlParameters;
    }

    public void setSqlParameters(String sqlParameters) {
        this.sqlParameters = sqlParameters;
    }

}
