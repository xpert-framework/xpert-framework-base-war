package com.base.vo.audit;

import com.xpert.persistence.utils.EntityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author ayslanms
 */
public class TabelaAuditoria {

    private String nome;
    private Class entity;

    /**
     * Retorna a lista de nomes das tabelas
     *
     * @param tabelas
     * @return
     */
    public static List<String> getNomesTabelas(List<TabelaAuditoria> tabelas) {
        List<String> nomesTabelas = new ArrayList<>();
        for (TabelaAuditoria tabela : tabelas) {
            nomesTabelas.add(EntityUtils.getEntityTableName(tabela.getEntity(), false));
        }
        return nomesTabelas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Class getEntity() {
        return entity;
    }

    public void setEntity(Class entity) {
        this.entity = entity;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.entity);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TabelaAuditoria other = (TabelaAuditoria) obj;
        if (!Objects.equals(this.entity, other.entity)) {
            return false;
        }
        return true;
    }

}
