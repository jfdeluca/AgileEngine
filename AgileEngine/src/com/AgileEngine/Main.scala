package com.AgileEngine

import java.io.File

import com.sun.tools.doclint.HtmlTag.Attr
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import scala.util.{Failure, Success, Try}
import scala.collection.JavaConversions._
import scala.swing.{Button, FileChooser}

object Main extends App{


  private val CHARSET_NAME = "utf8"


  println("Kindly Select a file...")

  //Creating new filechooser as target for Id or Css search
  val chooser = new FileChooser(new File("."))
  val file = chooser.showOpenDialog(null)
  val resourcePath = chooser.selectedFile.getAbsolutePath

  val targetElementId: String = "make-everything-ok-button"
  val cssQuery = "[class*=\"btn btn-success\"]"
  val href = "#check-and-ok"



  //In the case that the file has not been found by its ID name it will be searched by css Class btn-success
  //Sorry for printing and not logging, been having some issues in this pc :S
  getAll()


  def findElementById(htmlFile: File, targetElementId: String): Try[Element] = Try {
    Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath)
  }.map(_.getElementById(targetElementId))



  def findElementsByQuery(htmlFile: File, cssQuery: String): Try[Elements] = Try {
    Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath)
  }.map(_.select(cssQuery))


  def getAll() = {
    findElementById(new File(resourcePath), targetElementId).map { button =>
      button.attributes().asList().map(attr => s"${attr.getKey}=${attr.getValue}").mkString(", ")
    } match {
      case Success(attrs) => println("Target element attrs: [{}]", attrs)
      case Failure(ex) => {
        println("Not Found by Id")
        getAllByClass()
      }
    }
  }

  def getAllByClass()= {
    findElementsByQuery(new File(resourcePath), cssQuery).map { buttons =>
      buttons.iterator().toList.map { button =>
        button.attributes().toList.map(attr => s"${attr.getKey}=${attr.getValue}").mkString(", ")
      }
    } match {
      case Success(attrsList) => attrsList.foreach { attrs => println("Target element attrs: [{}]", attrs) }
      case Failure(ex) => println("Error occurred.", ex)
    }

  }



}

