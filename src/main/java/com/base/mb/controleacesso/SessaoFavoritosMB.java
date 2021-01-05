package com.base.mb.controleacesso;

import com.base.bo.controleacesso.PermissaoBO;
import com.base.bo.controleacesso.UsuarioBO;
import com.base.dao.controleacesso.PermissaoDAO;
import com.base.modelo.controleacesso.Permissao;
import com.base.util.SessaoUtils;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author ayslanms
 */
@Named
@SessionScoped
public class SessaoFavoritosMB implements Serializable {

    private List<Permissao> favoritos;

    @EJB
    private PermissaoBO permissaoBO;
    @EJB
    private UsuarioBO usuarioBO;
    @EJB
    private PermissaoDAO permissaoDAO;

    /**
     * Carrega os favoritos do usuario atual na sessao
     */
    public void carregarFavoritos() {
        favoritos = permissaoDAO.getPermissoesFavoritas(SessaoUtils.getUser());
        permissaoBO.criarCaminhoPermissao(favoritos, false);
    }

    /**
     * Adiciona a pagina atual aos favoritos do usuario
     */
    public void adicionarFavorito() {
        Permissao permissao = usuarioBO.adicionarFavorito();
        if (permissao != null) {
            favoritos.add(permissao);
            permissaoBO.criarCaminhoPermissao(favoritos, false);
        }
    }

    /**
     * Remove a pagina atual dos favoritos do usuario
     */
    public void removerFavorito() {
        Permissao permissao = usuarioBO.removerFavorito();
        if (permissao != null) {
            favoritos.remove(permissao);
        }
    }

    public void removerFavoritoSelecionado(Permissao permissao) {
        usuarioBO.removerFavorito(permissao);
        if (permissao != null) {
            favoritos.remove(permissao);
        }
    }

    /**
     * retorna true se a pagina atual possui permissao vinculada para ser
     * adicionada aos favoritos
     *
     * @return
     */
    public boolean paginaPossuiPermissao() {
        return permissaoBO.getPermissaoViewAtual() != null;
    }

    public boolean isFavorito() {
        Permissao permissaoAtual = permissaoBO.getPermissaoViewAtual();
        if (permissaoAtual != null && favoritos != null) {
            return favoritos.contains(permissaoAtual);
        }
        return false;
    }

    public List<Permissao> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Permissao> favoritos) {
        this.favoritos = favoritos;
    }

}
