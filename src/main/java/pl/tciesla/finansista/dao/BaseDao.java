package pl.tciesla.finansista.dao;

import java.util.List;

public interface BaseDao<E, K> {
	
	void persist(E entity);
	
	void update(E entity);
	
	void delete(K id);
	
	E fetch(K id);
	
	List<E> fetchAll();
}
