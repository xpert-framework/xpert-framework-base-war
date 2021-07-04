package com.base.application;

import com.xpert.audit.QueryAuditPersister;
import com.xpert.audit.QueryAuditPersisterFactory;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.CDI;

/**
 * Implementacao padrao do QueryAuditPersisterFactory do xpert-frameowrk
 *
 * @author ayslanms
 */
public class QueryAuditPersisterFactoryImpl implements QueryAuditPersisterFactory {

    private static final Logger logger = Logger.getLogger(QueryAuditPersisterFactoryImpl.class.getName());

    @Override
    public QueryAuditPersister getPersister() {
        CDI cdi = CDI.current();
        return (QueryAuditPersister) cdi.select(QueryAuditPersister.class).get();
    }

}
