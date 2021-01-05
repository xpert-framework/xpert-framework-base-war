package com.base.dao.audit;

import com.base.modelo.audit.Auditing;
import com.xpert.persistence.dao.BaseDAO;
import javax.ejb.Local;

/**
 *
 * @author Ayslan
 */
@Local
public interface AuditingDAO extends BaseDAO<Auditing> {
    
}
