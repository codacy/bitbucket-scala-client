import org.scalatest._
import org.scalatest.Matchers

class HelloSpec extends FlatSpec with Matchers {
  "Hello" should "have tests" in {
    true shouldBe true
  }
}
