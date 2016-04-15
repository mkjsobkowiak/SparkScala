package spark

import javax.persistence._

@Entity
@Table(name = "USER")
class SimpleFun(first: String, last: String) {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private var id: Int = _
  @Column(name = "FIRST_NAME")
  private var firstName: String = first
  @Column(name = "LAST_NAME")
  private var lastName: String = last

  def this() {
    this(null, null)
  }

  override def toString = id + " = " + firstName + " " + lastName
}
