package spark

import javax.persistence.{EntityManager, EntityManagerFactory, Persistence}

object Application extends App {

  //  val allBuddies = entityManager.createQuery("From Buddy", classOf[SimpleFun]).getResultList.toArray
  var entityManagerFactory: EntityManagerFactory = Persistence.createEntityManagerFactory("sparkJPAUnit")
  var entityManager: EntityManager = entityManagerFactory.createEntityManager()
  
  entityManager.getTransaction().begin()
  entityManager.persist(new SimpleFun("Natalino", "Busa"))
  entityManager.persist(new SimpleFun("Angelina", "Jolie"))
  entityManager.persist(new SimpleFun("Kate", "Moss"))
  entityManager.getTransaction().commit()

  entityManager.getTransaction().begin();
  entityManager.getTransaction().commit();

  // allBuddies foreach println

  entityManager.close();
}
