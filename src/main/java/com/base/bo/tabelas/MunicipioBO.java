package com.base.bo.tabelas;

import com.xpert.core.crud.AbstractBusinessObject;
import com.base.dao.tabelas.MunicipioDAO;
import com.base.dao.tabelas.UfDAO;
import com.xpert.core.validation.UniqueField;
import com.xpert.core.exception.BusinessException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import com.base.modelo.tabelas.Municipio;
import com.base.modelo.tabelas.Uf;
import com.xpert.core.validation.UniqueFields;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author ayslanms
 */
@Stateless
public class MunicipioBO extends AbstractBusinessObject<Municipio> {

    private static final Logger LOG = Logger.getLogger(MunicipioBO.class.getName());

    @EJB
    private MunicipioDAO municipioDAO;
    @EJB
    private UfDAO ufDAO;

    @Override
    public MunicipioDAO getDAO() {
        return municipioDAO;
    }

    @Override
    public List<UniqueField> getUniqueFields() {
        return UniqueFields.from(Municipio.class);
    }

    @Override
    public void validate(Municipio municipio) throws BusinessException {
    }

    /**
     * Importa a partir de um arquivo CSV os municipio. Que devem esta no
     * formato:
     *
     * <pre>
     * CSV separado por virgula, com linha de cabecalho
     *
     * Colunas: codigo_ibge,nome,latitude,longitude,capital,codigo_uf
     * </pre>
     *
     * @param arquivo
     * @throws BusinessException
     */
    public void importar(InputStream arquivo) throws BusinessException {

        //colocar UFs no map pra otimizar a consulta
        List<Uf> ufs = ufDAO.listAll();
        Map<Long, Uf> ufsMap = new HashMap<>();
        for (Uf uf : ufs) {
            ufsMap.put(uf.getCodigoIbge(), uf);
        }

        //colocar Municipios em um map pra otimizar a consulta
        List<Municipio> municipiosBD = municipioDAO.listAll();
        Map<Long, Municipio> municipiosMap = new HashMap<>();
        for (Municipio municipio : municipiosBD) {
            municipiosMap.put(municipio.getCodigoIbge(), municipio);
        }

        //usando a commons-csv
        CSVParser csvParser;
        try {
            csvParser = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new InputStreamReader(arquivo, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex, null);
            throw new BusinessException("Erro ao carregar o arquivo (IOException) {0}", ex.getMessage());
        }
        //codigo_ibge,nome,latitude,longitude,capital,codigo_uf
        for (CSVRecord record : csvParser) {

            long linha = csvParser.getCurrentLineNumber();

            LOG.log(Level.INFO, "Linha {0}: {1}", new Object[]{linha, record});

            String codigoIbge = record.get(0);
            String nome = record.get(1);
            String latitude = record.get(2);
            String longitude = record.get(3);
            String capital = record.get(4);
            String codigoUf = record.get(5);

            //verificar se ja existe no banco de dados
            Municipio municipio = municipiosMap.get(Long.parseLong(codigoIbge.trim()));
            //caso ja exista atualizar
            if (municipio == null) {
                LOG.log(Level.INFO, "Municipio {0} nao existe no banco de dados. Criando um novo", new Object[]{codigoIbge});
                municipio = new Municipio();
            }

            municipio.setCodigoIbge(Long.parseLong(codigoIbge.trim()));
            municipio.setNome(nome);
            if (latitude != null) {
                municipio.setLatitude(new BigDecimal(latitude));
            }
            if (longitude != null) {
                municipio.setLongitude(new BigDecimal(longitude));
            }
            //1 true, 0 false
            municipio.setCapital("1".equals(capital));
            municipio.setUf(ufsMap.get(Long.parseLong(codigoUf)));

            //nao auditar por questoes de peformance
            municipioDAO.merge(municipio, false);

        }

    }

    @Override
    public boolean isAudit() {
        return true;
    }

}
