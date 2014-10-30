package models

import java.util.Date

object Domains {
  /*
   * Case class for UserForm
   */
  case class UserForm(name:String,email:String,password:(String,String),joinDate: Date)
  /*
   * Case class for LoginForm
   */
  case class LoginForm(email:String,password: String)
}
