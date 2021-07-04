package com.base.constante;

import org.jsoup.safety.Whitelist;

/**
 *
 * @author Ayslan
 */
public class Constantes {

    /**
     * define o nome da persistence-unit padrao
     */
    public static final String PERSISTENCE_UNIT_NAME = "xpert-framework-basePU";
    /**
     * Tipo para String sem tamanho definido.
     *
     * Postgres: text Oracle: clob MySQL: longtext (ou text)
     *
     */
    public static final String TIPO_TEXTO_BANCO = "text";
    /**
     * Define o tempo em minutos de validade da recuperacao de senha
     */
    public static final int MINUTOS_VALIDADE_RECUPERACAO_SENHA = 30;

    public static final String[] WHITE_LIST_HTML_ATTRIBUTES = {"href", "class", "style", "title"};
    /**
     * Para quando utilizar espape="false" no XHTML tentar "limpar" o HTML para
     * previnir XSS
     */
    public static final Whitelist WHITE_LIST_HTML = Whitelist.relaxed()
            .addAttributes("a", WHITE_LIST_HTML_ATTRIBUTES)
            .addAttributes("i", WHITE_LIST_HTML_ATTRIBUTES)
            .addAttributes("b", WHITE_LIST_HTML_ATTRIBUTES)
            .addAttributes("div", WHITE_LIST_HTML_ATTRIBUTES)
            .addAttributes("span", WHITE_LIST_HTML_ATTRIBUTES);

    public static final String TEMA_PADRAO = "nova-dark";
    /**
     * Quantidade maxima a ser exbida na consulta de auditoria
     */
    public static final int QUANTIDADE_MAXIMA_LISTA_AUDITORIA = 2000;
    public static final int QUANTIDADE_LIMITE_REGISTROS_GRAFICOS_DASHBOARD = 20;

    private Constantes() {
    }
}
