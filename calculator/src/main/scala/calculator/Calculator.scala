package calculator

import scala.scalajs.js
import org.scalajs.dom

object Calculator extends js.JSApp {
  def main(): Unit = {
    val paragraph = dom.document.createElement("p")
    paragraph.innerHTML = "<strong>It works!</strong>"
    dom.document.getElementById("result-list").appendChild(paragraph)
  }
}
