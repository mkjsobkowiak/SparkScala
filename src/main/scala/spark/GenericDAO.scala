package spark

import javax.persistence.{EntityManager, Persistence, EntityManagerFactory}

trait GenericDAO[T] {

  private var emFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("sparkJPAUnit")
  private var em: EntityManager = emFactory.createEntityManager()

  def findOne: T = {
    em.getTransaction().begin()
    em.find()
    em.getTransaction().commit()
  }

  def findAll: List[T]

  def save(obj: T)

  def delete(obj: T)
}
