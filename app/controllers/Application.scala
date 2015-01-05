package controllers

import models.Domains._
import models.QueryBuilder._

import utils.Helper._
import utils.Constants._

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._

import java.util.Date
import scala.concurrent.Future

import views.html																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																								 

object Application extends Controller {
 /*
 * userForm	
 */
  val userForm = Form(
    mapping(
      "name" -> nonEmptyText, "email" -> email, "password" -> tuple("main" -> nonEmptyText(MIN_LENGTH_OF_PASSWORD), "confirm" -> nonEmptyText).
        verifying(PASSWORD_NOT_MATCHED, passwords => passwords._1 == passwords._2), "joinDate" -> date("yyyy-MM-dd"))(UserForm.apply)(UserForm.unapply))
  /*
   * Login Form
   */
  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText)(LoginForm.apply)(LoginForm.unapply))

  /*
   * Redirect Home Page
   */
  def index = Action { request =>
    //Ok(html.index(HOME_PAGE_TITLE, findUri(request)))
    Redirect(routes.Application.registration)
  }

  /*
   * Display Registration Page
   */
  def registration = Action { implicit request =>

    val name = findFlashElementValue(request, "name")
    val email = findFlashElementValue(request, "email")
    if (email != " ") {
      val userForm = Application.userForm.fill(UserForm(name, email, (" ", " "), new Date))
    }

    Ok(html.registration(userForm, findUri(request)))
  }

  /*
   * Display Login Page	
   */
  def login = Action { implicit request =>

    val email = findFlashElementValue(request, "email")
    if (email != " ") {
      val loginForm = Application.loginForm.fill(LoginForm(email, " "))
    }

    Ok(html.login(loginForm, findUri(request)))
  }

  /*
 * Logout and clean the session.
 */
  def logout = Action { implicit request =>
    Redirect(routes.Application.login).withNewSession
  }

  /*
   * Handle UserForm Submission
   */
  def createUser = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      error => Future(BadRequest(html.registration(error, findUri(request)))),
      userForm => {
        val name = userForm.name
        val email = userForm.email
        val (password, _) = userForm.password
        val joinDate = userForm.joinDate
        val saveRecord = Json.obj("name" -> name, "email" -> email, "password" -> password, "joinDate" -> joinDate)

        val jsonuserdata = Json.obj("email" -> email)
        val futureUserdetails = getDocumentsByQuery(USER_COLLECTION_NAME, jsonuserdata)

        futureUserdetails.map { result =>
          if (result.size > 0) {
            Redirect(routes.Application.registration).flashing(
              "message" -> ENTERED_EMAIL_EXISTS, "name" -> name, "email" -> email)
          } else {
            val futuresaveUser = insertDocuments(USER_COLLECTION_NAME, saveRecord)
            Redirect(routes.Application.dashboard).withSession(
              "email" -> email)
          }

        }

      })
  }
  /*
   * Login Authentication
   */

  def loginAuthentication = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      error => Future(BadRequest(html.login(error, findUri(request)))),
      loginForm => {
        val email = loginForm.email
        val password = loginForm.password
        val jsonuserdata = Json.obj("email" -> email, "password" -> password)
        val futureUserdetails = getDocumentsByQuery(USER_COLLECTION_NAME, jsonuserdata)

        futureUserdetails.map { result =>
          if (result.size > 0) {
            Redirect(routes.Application.dashboard).withSession(
              "email" -> email)
          } else {
            Redirect(routes.Application.login).flashing(
              "message" -> WRONG_LOGIN_DETAILS, "email" -> email)
          }

        }
      })

  }

  /*
   * Redirect Dashboard
   */
  def dashboard = Action.async { implicit request =>
    if (findSessionElementValue(request, "email") == " ") {
      Future(Redirect(routes.Application.login))
    } else {
      val jsonuserdata = Json.obj("email" -> findSessionElementValue(request, "email"))
      val userfutureresult = getDocumentsByQuery(USER_COLLECTION_NAME, jsonuserdata)

      userfutureresult.map { result =>
        Ok(html.dashboard(DASHBOARD_TITLE, findUri(request), result))
      }

    }
  }

}
