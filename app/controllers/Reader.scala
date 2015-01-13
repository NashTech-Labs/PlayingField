package controllers

import scala.xml.Elem
import scala.xml.Node
import scala.collection.mutable.Queue
import scala.xml.Text
import scala.xml.PrettyPrinter

object Reader {
  /*def loadXML = {
    val parserFactory = new org.ccil.cowan.tagsoup.jaxp.SAXFactoryImpl
    val parser = parserFactory.newSAXParser()
    val source = new org.xml.sax.InputSource("http://www.google.com")
    val adapter = new scala.xml.parsing.NoBindingFactoryAdapter
    val feed = adapter.loadXML(source, parser)
    feed
  }*/

  /*def proc(node: Node): String =
    node match {
      case <body>{ txt }</body> => "Partial content: " + txt
      case _ => "grmpf"
    }

  def main(args: Array[String]): Unit = {
    val content = Reader.loadXML
    Console.println(content)
    Console.println(proc(content))

  }*/
}