package spark

import javax.persistence._

@Entity
@Table(name = "SIMPLE_FUN")
class SimpleFun(first: String, last: String) {

  @Column(name = "FIRST_NAME")
  private val firstName: String = first
  @Column(name = "LAST_NAME")
  private val lastName: String = last
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Int = _

  override def toString = id + " = " + firstName + " " + lastName
}
