package spark

object CheckDatabase extends App {

  val session = HibernateUtil.getSessionFactory.openSession()
  session.beginTransaction()

  val tweet1 = new TweetEntity("Natalino")
  val tweet2 = new TweetEntity("Angelina")
  val tweet3 = new TweetEntity("Kate")

  session.save(tweet1)
  session.save(tweet2)
  session.save(tweet3)
  session.getTransaction.commit()
}
