package com.base;

import com.base.dao.DAO;
import com.base.modelo.tabelas.Uf;
import com.base.modelo.controleacesso.*;
import com.xpert.persistence.dao.BaseDAO;
import com.xpert.utils.Encryption;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Classe para geracao inicial dos dados do sistema, como perfil ADMINISTRADOR
 * usuario ADMIN, e outros
 *
 * @author Ayslan
 */
@Stateless
public class GeracaoDadosSistema {

    private static final Logger logger = Logger.getLogger(GeracaoDadosSistema.class.getName());
    @EJB
    private DAO dao;
    @EJB
    private GeracaoPermissao geracaoPermissao;
    @EJB
    private GeracaoModeloEmail geracaoModeloEmail;

    public <T> BaseDAO<T> getDAO(Class<T> entity) {
        return dao.getDAO(entity);
    }

    public void generate() {

        try {
            //gerar modelos de email
            geracaoModeloEmail.generate();
            //gerar permissões
            geracaoPermissao.generate();

            //gerar perfil ADMIN
            Perfil perfil = getDAO(Perfil.class).unique("descricao", "ADMINISTRADOR");
            if (perfil == null) {
                perfil = new Perfil();
                perfil.setDescricao("ADMINISTRADOR");
                perfil.setAtivo(true);
            }

            //adicionar todas as permissoes para o admin
            perfil.setPermissoes(getDAO(Permissao.class).listAll());
            if (perfil.getId() == null) {
                BaseDAO<Permissao> permissaoDAO = getDAO(Permissao.class);
                List<Permissao> atalhos = new ArrayList<Permissao>();
                //permissoes padrao para o perfil ADMIN
                atalhos.add(permissaoDAO.unique("key", "usuario.list"));
                atalhos.add(permissaoDAO.unique("key", "usuario.create"));
                atalhos.add(permissaoDAO.unique("key", "acessoSistema.list"));
                atalhos.add(permissaoDAO.unique("key", "usuario.alterarSenha"));
                atalhos.add(permissaoDAO.unique("key", "erroSistema.list"));
                atalhos.add(permissaoDAO.unique("key", "perfil.list"));
                perfil.setPermissoesAtalho(atalhos);
            }
            getDAO(Perfil.class).saveOrMerge(perfil, false);

            //criar usuario ADMIN
            Usuario usuario = getDAO(Usuario.class).unique("userLogin", "ADMIN");

            //se nao encontrou, criar um novo
            if (usuario == null) {
                //usuario
                usuario = new Usuario();
                usuario.setSituacaoUsuario(SituacaoUsuario.ATIVO);
                usuario.setMatricula("123");
                usuario.setRg("123");
                usuario.setSenhaCadastrada(true);
                usuario.setCpf("12345678909");
                usuario.setEmail("admin@xpertsistemas.com.br");
                usuario.setNome("ADMINISTRADOR DO SISTEMA");
                List<Perfil> perfis = new ArrayList<Perfil>();
                perfis.add(perfil);
                usuario.setPerfis(perfis);
                usuario.setUserLogin("ADMIN");

                //definir senha "1" para o usuario
                try {
                    usuario.setUserPassword(Encryption.getSHA256("1"));
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }
                usuario.setSuperUsuario(true);
                getDAO(Usuario.class).saveOrMerge(usuario, false);

                //historico como ativo
                HistoricoSituacaoUsuario historicoSituacaoUsuario = new HistoricoSituacaoUsuario();
                historicoSituacaoUsuario.setDataSituacao(new Date());
                historicoSituacaoUsuario.setSituacaoUsuario(SituacaoUsuario.ATIVO);
                historicoSituacaoUsuario.setUsuario(usuario);
                getDAO(HistoricoSituacaoUsuario.class).saveOrMerge(historicoSituacaoUsuario, false);

            }

            createUfs();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public void createUfs() {

        /**
         * Norte
         */
        createUf(new Uf(11L, "Rondônia", "RO", 1L, "Norte", -10.83, -63.34));
        createUf(new Uf(12L, "Acre", "AC", 1L, "Norte", -8.77, -70.55));
        createUf(new Uf(13L, "Amazonas", "AM", 1L, "Norte", -3.47, -65.1));
        createUf(new Uf(14L, "Roraima", "RR", 1L, "Norte", 1.99, -61.33));
        createUf(new Uf(15L, "Pará", "PA", 1L, "Norte", -3.79, -52.48));
        createUf(new Uf(16L, "Amapá", "AP", 1L, "Norte", 1.41, -51.77));
        createUf(new Uf(17L, "Tocantins", "TO", 1L, "Norte", -9.46, -48.26));

        /**
         * Nordeste
         */
        createUf(new Uf(21L, "Maranhão", "MA", 2L, "Nordeste", -5.42, -45.44));
        createUf(new Uf(22L, "Piauí", "PI", 2L, "Nordeste", -6.6, -42.28));
        createUf(new Uf(23L, "Ceará", "CE", 2L, "Nordeste", -5.2, -39.53));
        createUf(new Uf(24L, "Rio Grande do Norte", "RN", 2L, "Nordeste", -5.81, -36.59));
        createUf(new Uf(25L, "Paraíba", "PB", 2L, "Nordeste", -7.28, -36.72));
        createUf(new Uf(26L, "Pernambuco", "PE", 2L, "Nordeste", -8.38, -37.86));
        createUf(new Uf(27L, "Alagoas", "AL", 2L, "Nordeste", -9.62, -36.82));
        createUf(new Uf(28L, "Sergipe", "SE", 2L, "Nordeste", -10.57, -37.45));
        createUf(new Uf(29L, "Bahia", "BA", 2L, "Nordeste", -13.29, -41.71));

        /**
         * Sudeste
         */
        createUf(new Uf(31L, "Minas Gerais", "MG", 3L, "Sudeste", -18.1, -44.38));
        createUf(new Uf(32L, "Espírito Santo", "ES", 3L, "Sudeste", -19.19, -40.34));
        createUf(new Uf(33L, "Rio de Janeiro", "RJ", 3L, "Sudeste", -22.25, -42.66));
        createUf(new Uf(35L, "São Paulo", "SP", 3L, "Sudeste", -22.19, -48.79));

        /**
         * Sul
         */
        createUf(new Uf(41L, "Paraná", "PR", 4L, "Sul", -24.89, -51.55));
        createUf(new Uf(42L, "Santa Catarina", "SC", 4L, "Sul", -27.45, -50.95));
        createUf(new Uf(43L, "Rio Grande do Sul", "RS", 4L, "Sul", -30.17, -53.5));

        /**
         * Centro Oeste
         */
        createUf(new Uf(50L, "Mato Grosso do Sul", "MS", 5L, "Centro-Oeste", -20.51, -54.54));
        createUf(new Uf(51L, "Mato Grosso", "MT", 5L, "Centro-Oeste", -12.64, -55.42));
        createUf(new Uf(52L, "Goiás", "GO", 5L, "Centro-Oeste", -15.98, -49.86));
        createUf(new Uf(53L, "Distrito Federal", "DF", 5L, "Centro-Oeste", -15.83, -47.86));
    }

    public void createUf(Uf uf) {

        BaseDAO<Uf> dao = getDAO(Uf.class);
        //sigla deve ser unica
        Uf ufBD = dao.unique("sigla", uf.getSigla());
        if (ufBD == null) {
            dao.save(uf);
        } else {
            ufBD.setSigla(uf.getSigla());
            ufBD.setNome(uf.getNome());
            ufBD.setNomeRegiao(uf.getNomeRegiao());
            ufBD.setCodigoIbge(uf.getCodigoIbge());
            ufBD.setCodigoIbgeRegiao(uf.getCodigoIbgeRegiao());
            ufBD.setLatitude(uf.getLatitude());
            ufBD.setLongitude(uf.getLongitude());
            dao.merge(ufBD, false);
        }

    }

}
