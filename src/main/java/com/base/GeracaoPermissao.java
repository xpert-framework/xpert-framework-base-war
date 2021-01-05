package com.base;

import com.base.dao.controleacesso.PermissaoDAO;
import com.base.modelo.controleacesso.Permissao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Classe para geracao das permissoes iniciais do sistema
 *
 * @author Ayslan
 */
@Stateless
public class GeracaoPermissao {

    private static final Logger logger = Logger.getLogger(GeracaoPermissao.class.getName());
    @EJB
    private PermissaoDAO permissaoDAO;

    /**
     * map para nao ter que ir ao banco sempre, pois com o crescimento das
     * permissoes fica muito pesado fazer varios selects
     */
    private Map<String, Permissao> permissoes = new HashMap<>();

    public void generate() {

        permissoes = new HashMap<>();
        List<Permissao> listPermissoes = permissaoDAO.listAll();
        for (Permissao permissao : listPermissoes) {
            permissoes.put(permissao.getKey(), permissao);
        }

        /**
         * Administracao Sistema
         */
        create(new Permissao("administracaoSistema", "Sistema", true, "fas fa-cog"), null);


        /*
         * Controle de Acesso
         */
        create(new Permissao("controleAcesso", "Controle de Acesso", true, "fas fa-shield-alt"), "administracaoSistema");

        //Permissao
        create(new Permissao("permissao", "Permissão", true, "fas fa-user-shield"), "controleAcesso");
        create(new Permissao("permissao.create", "Cadastro de Permissão", "/view/controleAcesso/permissao/createPermissao.jsf", true), "permissao");
        create(new Permissao("permissao.list", "Consulta de Permissão", "/view/controleAcesso/permissao/listPermissao.jsf", true), "permissao");
        create(new Permissao("permissao.audit", "Auditoria de Permissão"), "permissao");
        create(new Permissao("permissao.delete", "Exclusão de Permissão"), "permissao");
        create(new Permissao("permissao.ativacao", "Ativação de Permissão", false, "fas fa-check"), "permissao");
        create(new Permissao("permissao.inativacao", "Inativação de Permissão", false, "fas fa-times"), "permissao");

        //Usuario
        create(new Permissao("usuario", "Usuário", true, "fas fa-user"), "controleAcesso");
        create(new Permissao("usuario.create", "Cadastro de Usuário", "/view/controleAcesso/usuario/createUsuario.jsf", true), "usuario");
        create(new Permissao("usuario.list", "Consulta de Usuário", "/view/controleAcesso/usuario/listUsuario.jsf", true), "usuario");
        create(new Permissao("usuario.audit", "Auditoria de Usuário"), "usuario");
        create(new Permissao("usuario.delete", "Exclusão de Usuário"), "usuario");

        //Perfil
        create(new Permissao("perfil", "Perfil", true, "fas fa-users"), "controleAcesso");
        create(new Permissao("perfil.create", "Cadastro de Perfil", "/view/controleAcesso/perfil/createPerfil.jsf", true), "perfil");
        create(new Permissao("perfil.list", "Consulta de Perfil", "/view/controleAcesso/perfil/listPerfil.jsf", true), "perfil");
        create(new Permissao("perfil.audit", "Auditoria de Perfil"), "perfil");
        create(new Permissao("perfil.delete", "Exclusão de Perfil"), "perfil");

        //Acessos do Sistema
        create(new Permissao("acessoSistema.list", "Relatório de Acessos", "/view/controleAcesso/acessoSistema/listAcessoSistema.jsf", true, "fas fa-file-signature"), "controleAcesso");

        //Solicitacao recuperacao senha
        create(new Permissao("solicitacaoRecuperacaoSenha", "Recuperação de Senha", true, "fas fa-key"), "controleAcesso");
        create(new Permissao("solicitacaoRecuperacaoSenha.list", "Consulta Recuperação de Senha", "/view/controleAcesso/solicitacaoRecuperacaoSenha/listSolicitacaoRecuperacaoSenha.jsf", true), "solicitacaoRecuperacaoSenha");
        create(new Permissao("solicitacaoRecuperacaoSenha.audit", "Auditoria de Recuperação de Senha"), "solicitacaoRecuperacaoSenha");

        //Auditoria
        create(new Permissao("auditoria.list", "Relatório de Auditoria", "/view/controleAcesso/auditoria/listAuditoria.jsf", true, "fas fa-history"), "controleAcesso");
        
        /*
         * Email
         */
        create(new Permissao("email", "Email", true, "fas fa-envelope"), "administracaoSistema");

        //Modelo email
        create(new Permissao("modeloEmail", "Modelo de Email", true, "far fa-envelope"), "email");
        create(new Permissao("modeloEmail.create", "Cadastro de Modelo de Email", "/view/email/modeloEmail/createModeloEmail.jsf", true), "modeloEmail");
        create(new Permissao("modeloEmail.list", "Consulta de Modelo de Email", "/view/email/modeloEmail/listModeloEmail.jsf", true), "modeloEmail");
        create(new Permissao("modeloEmail.audit", "Auditoria de Modelo de Email"), "modeloEmail");
        create(new Permissao("modeloEmail.delete", "Exclusão de Modelo de Email"), "modeloEmail");

        //Configuracao email
        create(new Permissao("configuracaoEmail", "Configuração de Email", true, "fas fa-at"), "email");
        create(new Permissao("configuracaoEmail.create", "Cadastro de Configuração de Email", "/view/email/configuracaoEmail/createConfiguracaoEmail.jsf", true), "configuracaoEmail");
        create(new Permissao("configuracaoEmail.list", "Consulta de Configuração de Email", "/view/email/configuracaoEmail/listConfiguracaoEmail.jsf", true), "configuracaoEmail");
        create(new Permissao("configuracaoEmail.audit", "Auditoria de Configuração de Email"), "configuracaoEmail");
        create(new Permissao("configuracaoEmail.delete", "Exclusão de Configuração de Email"), "configuracaoEmail");


        /*
         * Configuracao
         */
        create(new Permissao("configuracaoSistema", "Configuração", true, "fas fa-wrench"), "administracaoSistema");

        //Erro sistema
        create(new Permissao("erroSistema.list", "Relatório de Erros", "/view/configuracao/erroSistema/listErroSistema.jsf", true, "fas fa-exclamation-circle"), "configuracaoSistema");

        /**
         * Cadastro
         */
        create(new Permissao("tabelas", "Tabelas", true, "fas fa-table"), null);

        //Uf
        create(new Permissao("uf", "UF", true, "fas fa-map-marked-alt"), "tabelas");
        create(new Permissao("uf.create", "Cadastro de UF", "/view/tabelas/uf/createUf.jsf", true), "uf");
        create(new Permissao("uf.list", "Consulta de UF", "/view/tabelas/uf/listUf.jsf", true), "uf");
        create(new Permissao("uf.audit", "Auditoria de UF"), "uf");
        create(new Permissao("uf.delete", "Exclusão de UF"), "uf");

        //Municipio
        create(new Permissao("municipio", "Municipio", true, "fas fa-map-marker-alt"), "tabelas");
        create(new Permissao("municipio.create", "Cadastro de Municipio", "/view/tabelas/municipio/createMunicipio.jsf", true), "municipio");
        create(new Permissao("municipio.import", "Importação de Municipio", "/view/tabelas/municipio/importMunicipio.jsf", true), "municipio");
        create(new Permissao("municipio.list", "Consulta de Municipio", "/view/tabelas/municipio/listMunicipio.jsf", true), "municipio");
        create(new Permissao("municipio.audit", "Auditoria de Municipio"), "municipio");
        create(new Permissao("municipio.delete", "Exclusão de Municipio"), "municipio");

        /**
         * Dashboards
         */
        create(new Permissao("dashboards", "Dashboards", true, "fas fa-chart-line"), null);
        create(new Permissao("dashboards.acessos", "Usuários/Acessos", "/view/dahboards/acessosUsuario/dashboardAcessos.jsf", true, "fas fa-users"), "dashboards");
        create(new Permissao("dashboards.erroSistema", "Erros do Sistema", "/view/dahboards/erroSistema/dashboardErroSistema.jsf", true, "fas fa-exclamation-circle"), "dashboards");
        create(new Permissao("dashboards.auditoria", "Auditoria", "/view/dahboards/auditoria/dashboardAuditoria.jsf", true, "fas fa-history"), "dashboards");

        /**
         * Permissoes Globais (essas permissoes todos terao acessos)
         */
        //Alterar Senha
        createGlobal(new Permissao("usuario.alterarSenha", "Alterar Senha", "/view/controleAcesso/password/alterPassword.jsf", true, "fas fa-unlock-alt"), "controleAcesso");
        createGlobal(new Permissao("usuario.ultimosAcessos", "Meus Últimos Acessos", "/view/controleAcesso/acessoSistema/ultimosAcessoSistema.jsf", true, "fas fa-user-lock"), "controleAcesso");

    }

    private void createGlobal(Permissao permissao, String pai) {
        permissao.setGlobal(true);
        create(permissao, pai);
    }

    private void create(Permissao permissao, String pai) {

        if (pai != null && pai.equals(permissao.getKey())) {
            logger.log(Level.WARNING, "Permissao ''{0}'' pai dela mesmo. Esta permissao nao sera salva.", permissao.getKey());
            return;
        }

        //nova permissao no banco setar os icones
        if (permissao.getIcone() == null || permissao.getIcone().isEmpty()) {
            if (permissao.getKey().contains(".create")) {
                permissao.setIcone("fas fa-plus");
            } else if (permissao.getKey().contains(".list")) {
                permissao.setIcone("fas fa-search");
            } else if (permissao.getKey().contains(".audit")) {
                permissao.setIcone("fas fa-history");
            } else if (permissao.getKey().contains(".delete")) {
                permissao.setIcone("fas fa-trash");
            } else if (permissao.getKey().contains(".import")) {
                permissao.setIcone("fas fa-upload");
            }
        }

        Permissao permissaoDB = permissoes.get(permissao.getKey());

        if (pai != null && !pai.isEmpty()) {
            Permissao permissaoPai = permissoes.get(pai);
            if (permissaoPai == null) {
                logger.log(Level.WARNING, "Permissao ''{0}'' pai nao encontrada.", pai);
            }
            permissao.setPermissaoPai(permissaoPai);
        }

        if (permissaoDB != null) {
            permissaoDB.setUrl(permissao.getUrl());
            permissaoDB.setIcone(permissao.getIcone());
            permissaoDB.setDescricao(permissao.getDescricao());
            permissaoDB.setPermissaoPai(permissao.getPermissaoPai());
            permissaoDB.setPossuiMenu(permissao.isPossuiMenu());
            permissaoDB.setGlobal(permissao.isGlobal());
            permissao = permissaoDB;
        } else {

        }

        permissao = permissaoDAO.merge(permissao, false);
        //atualizar o Map
        permissoes.put(permissao.getKey(), permissao);
    }
}
