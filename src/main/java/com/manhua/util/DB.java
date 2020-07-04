package com.manhua.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.manhua.entity.Link;



public class DB {
    private static EntityManagerFactory entityManagerFactory;
    static {
        entityManagerFactory = Persistence.createEntityManagerFactory( "org.hibernate.tutorial.jpa" );
	}
	public static <T> void save(T t) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( t);
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	public static <T> void save(List<T> list) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		for(T t :list){
			entityManager.persist(t);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	public static <T> List<T>  query(String sql,Class<T> resultClass){
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<T> result = (List<T>) entityManager.createQuery(sql, resultClass).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	public static <T> void update(T t) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.merge( t);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static  void updateLink(Link link) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		Link yLink = entityManager.find(Link.class, link.getId());
		if(yLink != null){
			if(link.getName()!=null){
				yLink.setName(link.getName());
			}
			entityManager.merge(yLink);
		}
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static boolean urlExists(String url){
		int len = query("from Link where url='"+url+"'",Link.class).size();
		return len>0;
	}


    public static void main(String[] args) {

		boolean bool= urlExists("zzz");
		System.out.println("-----------------------------:"+bool);

		// save(new Link( "zzz","bbb", new Date() ));
		// ArrayList list = new ArrayList<Link>();
		// list.add(new Link( "zzzss","bbbsss", new Date() ));
		// save(list);

		// List<Link> res = query("from Link",Link.class);
		// System.out.println(res);

    }
}