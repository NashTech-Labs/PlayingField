package models

import play.api._
import play.api.mvc._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection

object QueryBuilder extends Controller with MongoController {
  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection(collectionname: String): JSONCollection = db.collection[JSONCollection](collectionname)

  def getDocumentsByQuery(collectionname: String, query: JsObject): Future[List[JsObject]] = {
    val cursor: Cursor[JsObject] = collection(collectionname).find(query).cursor[JsObject]
    val futureresult: Future[List[JsObject]] = cursor.collect[List]()
    futureresult
  }

  def insertDocuments(collectionname: String, documents: JsObject) {
    collection(collectionname).insert(documents)
  }

}
