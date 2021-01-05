package com.base.mb.tema;

import com.base.bo.controleacesso.UsuarioBO;
import com.base.constante.Constantes;
import com.base.mb.controleacesso.SessaoUsuarioMB;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * ManagedBean para definir o tema atual do usuario e fazer o controle no caso
 * de atualizacao nas preferencias do usuario
 *
 * @author ayslanms
 */
@Named
@SessionScoped
public class UserThemeMB implements Serializable {

    @Inject
    private SessaoUsuarioMB sessaoUsuarioMB;

    @EJB
    private UsuarioBO usuarioBO;

    private String theme;

    /**
     * Retorna o tema para a sessao atual do usuario. Caso nao existe usuario na
     * sessao ou ele nao possua tema, assumir o tema padrao que esta definido
     * nas constantes
     *
     */
    @PostConstruct
    public void carregarTema() {

        //se o usuario estiver logado e possuir tema
        if (sessaoUsuarioMB != null && sessaoUsuarioMB.getUser() != null
                && sessaoUsuarioMB.getUser().getTema() != null && !sessaoUsuarioMB.getUser().getTema().trim().isEmpty()) {
            theme = sessaoUsuarioMB.getUser().getTema().trim();
        } else {
            theme = Constantes.TEMA_PADRAO;
        }
    }

    public void mudarTema() {
        if (theme != null) {
            usuarioBO.mudarTema(theme);
            sessaoUsuarioMB.getUser().setTema(theme);
        }
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public SessaoUsuarioMB getSessaoUsuarioMB() {
        return sessaoUsuarioMB;
    }

    public void setSessaoUsuarioMB(SessaoUsuarioMB sessaoUsuarioMB) {
        this.sessaoUsuarioMB = sessaoUsuarioMB;
    }

}
