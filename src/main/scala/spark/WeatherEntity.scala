package spark

import javax.persistence._

@Entity
@Table(name = "WEATHER")
class WeatherEntity(deg: Double) {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "my_gen")
  @SequenceGenerator(name = "my_gen", sequenceName = "weather_seq")
  private var id: Int = _

  @Column(name = "CREATE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private var createDate: java.util.Date = new java.util.Date()

  @Column(name = "DEGREE")
  private var degree: Double = deg
}
