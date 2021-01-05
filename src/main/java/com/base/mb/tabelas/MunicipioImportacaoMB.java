package com.base.mb.tabelas;

import com.base.bo.tabelas.MunicipioBO;
import com.xpert.core.exception.BusinessException;
import com.xpert.faces.utils.FacesMessageUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ayslanms
 */
@Named
@ViewScoped
public class MunicipioImportacaoMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(MunicipioBO.class.getName());

    @EJB
    private MunicipioBO municipioBO;

    private UploadedFile uploadedFile;

    public void upload(FileUploadEvent event) {
        uploadedFile = event.getFile();
    }

    public void importar() {
        if (uploadedFile == null) {
            FacesMessageUtils.error("É necessário fazer o upload do arquivo");
            return;
        }

        try {
            municipioBO.importar(uploadedFile.getInputstream());
            FacesMessageUtils.sucess();
        } catch (BusinessException ex) {
            FacesMessageUtils.error(ex);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex, null);
            FacesMessageUtils.error("Erro ao carregar o arquivo (IOException) {0}", ex.getMessage());
        }

    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
    
    

}
