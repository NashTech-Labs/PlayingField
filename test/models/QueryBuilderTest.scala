package models
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json
import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class QueryBuilderTest extends Specification {
 val data = Future(List(Json.obj("email" -> "a@gmail.com", "password" -> "12345","joinDate" ->"2014-10-10")))
 
  "Application" should {

    "Insert Document Method" in new WithApplication{
      val jsondata = Json.obj("Status" -> "success")
      val result = QueryBuilder.insertDocuments("test",jsondata)
      assert(result === true)
    }
  }
  
  "Application" should {

    "Fetch Document Method" in new WithApplication{
      val jsonuserdata = Json.obj("email" -> "xyz@gmail.com", "password" -> "123456")
      val result = QueryBuilder.getDocumentsByQuery("test",jsonuserdata)
      assert(result != data)
    }
  }
  
}
