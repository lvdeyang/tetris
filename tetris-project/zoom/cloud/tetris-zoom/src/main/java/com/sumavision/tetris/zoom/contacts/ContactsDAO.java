package com.sumavision.tetris.zoom.contacts;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ContactsPO.class, idClass = Long.class)
public interface ContactsDAO extends BaseDAO<ContactsPO>{

}
