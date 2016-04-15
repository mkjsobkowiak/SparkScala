package spark

import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder

object HibernateUtil {

  private[this] val sessionFactory = buildSessionFactory

  def shutdown = sessionFactory.close

  def getSessionFactory = sessionFactory

  private def buildSessionFactory: SessionFactory = {
    try {
      val standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
      val metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
      return metadata.getSessionFactoryBuilder().build();
    } catch {
      case ex: Throwable =>
        println("Initial SessionFactory creation failed." + ex);
        throw new ExceptionInInitializerError(ex);
    }
  }
}
