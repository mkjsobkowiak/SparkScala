package spark

import javax.persistence.{EntityManager, Persistence, EntityManagerFactory}

import scala.collection.JavaConversions._

object GenericDAO {

  private val emFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("sparkJPAUnit")
  private val em: EntityManager = emFactory.createEntityManager()

  def begin: Unit = em.getTransaction.begin

  def end: Unit = em.getTransaction.commit

  def findOne[T](id: Long, entityClass: Class[T]): T = {
      em.find(entityClass, id)
  }

  def findAll[T](entityClass: Class[T]): List[T] = {
    em.createQuery("FROM " + entityClass.getName, entityClass).getResultList.toList;
  }

  def save[T](obj: T): Unit = {
    em.persist(obj)
  }

  def delete[T](obj: T): Unit = {
    em.remove(obj)
  }
}
