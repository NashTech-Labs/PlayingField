package controllers

import utils.Helper._
import play.api._
import play.api.mvc._
import play.api.i18n.Messages
import play.api.Play.current
import play.api.libs.ws._
import play.api.libs.ws.ning.NingAsyncHttpClientConfigBuilder
import scala.concurrent._
import ExecutionContext.Implicits.global
import views.html
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import scala.collection.mutable.ListBuffer

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
  
  def currencyConverter = Action.async{ implicit request =>
    val currencyfrom=request.body.asFormUrlEncoded.get("currencyfrom")(0)
    val currencyto=request.body.asFormUrlEncoded.get("currencyto")(0)
    val amount=request.body.asFormUrlEncoded.get("amount")(0)
     val wsReq = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">" +
                      "<soap12:Body><ConversionRate xmlns=\"http://www.webserviceX.NET/\">" +
                      "<FromCurrency>"+currencyfrom+"</FromCurrency>" +
                      "<ToCurrency>"+currencyto+"</ToCurrency>"+
                      "</ConversionRate></soap12:Body></soap12:Envelope>"
    val doc = WS.url("http://www.webservicex.net/CurrencyConvertor.asmx").withHeaders("Content-Type" -> "application/soap+xml").post(wsReq)
    doc.map(result=>
      Ok(html.currencypage(result.xml.text.toDouble,amount.toDouble,(result.xml.text.toDouble)*amount.toDouble,currencyfrom:String,currencyto:String))
    )
  }
}