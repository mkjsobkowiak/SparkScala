package spark

import javax.persistence._

@Entity
@Table(name = "TWEET")
class TweetEntity(hashTag_ : String) {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "my_gen")
  @SequenceGenerator(name = "my_gen", sequenceName = "tweet_seq")
  private var id: Int = _

  @Column(name = "CREATE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private var createDate: java.util.Date = new java.util.Date()

  @Column(name = "HASH_TAG")
  private var hashTag: String = hashTag_
}
