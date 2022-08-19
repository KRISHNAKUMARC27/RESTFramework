package com.example.krish.service;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

import com.example.krish.model.TMFException;
import com.example.krish.model.TMFExceptionInfo;
import com.example.krish.model.TMFResource;
import com.example.krish.model.attributefiltering.LogicalCondition;
import com.example.krish.model.attributefiltering.Operator;
import com.example.krish.model.attributefiltering.QueryElement;
import com.example.krish.model.attributefiltering.SearchCriteria;

public class CRUDJpaServiceImpl<T extends TMFResource, ID> extends CRUDServiceImpl<T, ID> {

	@Autowired
	JpaRepository<T, ID> jpaRepository;

	@Autowired
	EntityManager em;

	private static final Logger logger = LogManager.getLogger(CRUDJpaServiceImpl.class);

	@Override
	public T create(T t, MultiValueMap<String, String> extras, Object... obj) throws TMFException {
		// TODO Auto-generated method stub
		logger.trace("#### Entering CRUDJpaServiceImpl create ####");
		jpaRepository.saveAndFlush(t);
		logger.trace("#### Exiting CRUDJpaServiceImpl create ####");
		return t;
	}

	@Override
	public T update(T t, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		// TODO Auto-generated method stub
		logger.trace("#### Entering CRUDJpaServiceImpl update ####");
		jpaRepository.saveAndFlush(t);
		logger.trace("#### Exiting CRUDJpaServiceImpl update ####");
		return t;
	}

	@Override
	public T findById(ID id, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		logger.trace("#### Entering CRUDJpaServiceImpl findById ####");
		Optional<T> optional = jpaRepository.findById(id);
		if (optional.isPresent()) {
			logger.trace("#### Exiting CRUDJpaServiceImpl findById ####");
			return optional.get();
		} else {
			logger.error("#### Exiting CRUDJpaServiceImpl findById ####");

			throw new TMFException(HttpStatus.NOT_FOUND,
					new TMFExceptionInfo("ERR001", "Entity Not Found", "Use correct ID"));
		}

	}

	@Override
	public List<T> findAll(MultiValueMap<String, String> extras, Object... obj) throws Exception {
		logger.trace("#### Entering CRUDJpaServiceImpl findAll ####");

		return (List<T>) jpaRepository.findAll();
	}

	@Override
	public void deleteById(ID id, MultiValueMap<String, String> extras, Object... obj) throws Exception {
		logger.trace("#### Entering CRUDJpaServiceImpl deleteById ####");
		jpaRepository.deleteById(id);
		logger.trace("#### Exiting CRUDJpaServiceImpl deleteById ####");
	}

	
	@Override
	public List<T> findAll(QueryElement queryElement, MultiValueMap<String, String> extras, Object... obj)
			throws Exception {
		logger.trace("#### Entering CRUDJpaServiceImpl findAll ####");
		CriteriaBuilder cb = em.getCriteriaBuilder();
		Class<T> entityType = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
		CriteriaQuery<T> cq = (CriteriaQuery<T>) cb.createQuery(entityType);
		
		Root<T> root = cq.from(entityType);
		Predicate finalPredicate = null;
		if(queryElement instanceof SearchCriteria ) {
			finalPredicate = getPredicateFromSearchCriteria((SearchCriteria)queryElement,cb,root);
		} else if (queryElement instanceof LogicalCondition) {
			finalPredicate = getPredicateFromLogicalCondition((LogicalCondition) queryElement, cb, root);
		}
		cq.where(finalPredicate);
		logger.trace("#### Exiting CRUDJpaServiceImpl findAll ####");
		return em.createQuery(cq).getResultList();
	}

	protected Predicate getPredicateFromLogicalCondition(LogicalCondition lc, CriteriaBuilder cb,
			Root<T> root) {
		logger.trace("#### Entering CRUDJpaServiceImpl getPredicateFromLogicalCondition ####");
		
		Predicate lhs = null;
		Predicate rhs = null;
		
		if(lc.getLhs() instanceof SearchCriteria)
			lhs = getPredicateFromSearchCriteria((SearchCriteria) lc.getLhs(), cb, root);
		else if (lc.getLhs() instanceof LogicalCondition) {
			lhs = getPredicateFromLogicalCondition((LogicalCondition)lc.getLhs(), cb, root);
		}
		
		if(lc.getRhs() instanceof SearchCriteria)
			rhs = getPredicateFromSearchCriteria((SearchCriteria) lc.getRhs(), cb, root);
		else if (lc.getRhs() instanceof LogicalCondition) {
			rhs = getPredicateFromLogicalCondition((LogicalCondition)lc.getRhs(), cb, root);
		}
		
		if(lc.getOperator().equals(Operator.AND)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromLogicalCondition ####");
			return cb.and(lhs,rhs);
		} else if(lc.getOperator().equals(Operator.OR)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromLogicalCondition ####");
			return cb.or(lhs, rhs);
		}
		logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromLogicalCondition ####");
		throw new RuntimeException("Uncreachable code getPredicateFromLogicalCondition lc is " + lc);
	}

	protected Predicate getPredicateFromSearchCriteria(SearchCriteria sc, CriteriaBuilder cb, Root<T> root) {
		// TODO Auto-generated method stub
		logger.trace("#### Entering CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
		
		if(sc.getOperation().equals(Operator.EQ)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
			return cb.equal(root.get(sc.getLhs()), sc.getRhs());
		} else if(sc.getOperation().equals(Operator.GT)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
			return cb.greaterThan(root.get(sc.getLhs()), sc.getRhs());
		} else if(sc.getOperation().equals(Operator.GTE)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
			return cb.greaterThanOrEqualTo(root.get(sc.getLhs()), sc.getRhs());
		} else if(sc.getOperation().equals(Operator.LT)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
			return cb.lessThan(root.get(sc.getLhs()), sc.getRhs());
		} else if(sc.getOperation().equals(Operator.LTE)) {
			logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
			return cb.lessThanOrEqualTo(root.get(sc.getLhs()), sc.getRhs());
		} 
		logger.trace("#### Exiting CRUDJpaServiceImpl getPredicateFromSearchCriteria ####");
		return null;
	}
}
