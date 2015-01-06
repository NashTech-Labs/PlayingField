package controllers

import utils.Helper._

import play.api._
import play.api.mvc._

import play.api.i18n.Messages

import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent.Future

import views.html

object CurrencyConverter extends Controller{
  
  val CURRENCYCONVERTER_TITLE = Messages("CURRENCYCONVERTER.TITLE")
  /*
   * Render currency convert form
   */
  
  def currencyConverterForm = Action { implicit request =>
    Ok(html.currencyconverter(CURRENCYCONVERTER_TITLE,findUri(request)))
  }
  /*
   * Currency Converter method
   */
  
  def currencyConverter = Action{ implicit request =>
    println("result~~~~~~"+request.body)
    val holder: WSRequestHolder = WS.url("http://www.webservicex.net/CurrencyConvertor.asmx")
    val complexHolder: WSRequestHolder =holder.withHeaders("Accept" -> "application/json")
    .withRequestTimeout(10000)
    .withQueryString("search" -> "play")
    println("complexHolder~~~~~~~~"+complexHolder)
    Ok
  }
  
}