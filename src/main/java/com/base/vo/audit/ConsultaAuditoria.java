package com.base.vo.audit;

import com.base.modelo.controleacesso.Usuario;
import com.xpert.audit.model.AuditingType;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ayslanms
 */
public class ConsultaAuditoria {

    /**
     * Filtros
     */
    private Date dataInicial;
    private Date dataFinal;
    private List<Usuario> usuarios;
    private List<TabelaAuditoria> tabelas;
    private List<AuditingType> tipos;
    private boolean agruparLinhaTempoUsuario;

    /**
     * Retorna a lista de nomes das tabelas
     *
     * @return
     */
    public List<String> getNomesTabelas() {
        return TabelaAuditoria.getNomesTabelas(tabelas);
    }

    public boolean isAgruparLinhaTempoUsuario() {
        return agruparLinhaTempoUsuario;
    }

    public void setAgruparLinhaTempoUsuario(boolean agruparLinhaTempoUsuario) {
        this.agruparLinhaTempoUsuario = agruparLinhaTempoUsuario;
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

}
