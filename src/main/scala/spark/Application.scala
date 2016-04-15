package spark

import javax.persistence.{EntityManager, EntityManagerFactory, Persistence}

object Application extends App {
  
  GenericDAO.begin
  val sim = new SimpleFun("Natalino", "Busa")
  println(sim.toString)
  GenericDAO.save(new SimpleFun("Natalino", "Busa"))
  GenericDAO.save(new SimpleFun("Angelina", "Jolie"))
  GenericDAO.save(new SimpleFun("Kate", "Moss"))
  GenericDAO.end
}
