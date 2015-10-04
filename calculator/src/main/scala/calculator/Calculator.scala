package calculator

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.html

object Calculator extends js.JSApp with Grammar {
  def main(): Unit = {
    val document = dom.document
    val exprElem = document.getElementById("expr").asInstanceOf[html.Input]
    val calcElem = document.getElementById("calculate")
    val resultList = document.getElementById("result-list")
    calcElem.addEventListener("click", { (e: dom.MouseEvent) =>
      additive.parse(exprElem.value.toSeq) match {
        case (None, _) =>
          dom.window.alert("Error parsing expression")

        case (Some(x), Nil) =>
          val resultElem = document.createElement("div")
          val exprSpan = document.createElement("span")
          exprSpan.setAttribute("class", "plan")
          exprSpan.appendChild(document.createTextNode(print(x) + " = "))
          val resultSpan = document.createElement("span")
          resultSpan.setAttribute("class", "result")
          resultSpan.appendChild(document.createTextNode(eval(x).toString()))
          resultElem.appendChild(exprSpan)
          resultElem.appendChild(resultSpan)
          resultList.insertBefore(resultElem, resultList.firstChild)

        case (_, err) =>
          dom.window.alert("Error parsing expression: " + err)
      }
    })
  }

  def eval(expr: Expr): BigInt = expr match {
    case Add(a, b) => eval(a) + eval(b)
    case Subtract(a, b) => eval(a) - eval(b)
    case Multiply(a, b) => eval(a) * eval(b)
    case Divide(a, b) => eval(a) * eval(b)
    case Negate(n) => -eval(n)
    case Number(v) => v
  }

  def print(expr: Expr): String = expr match {
    case Add(a, b) => "(" + print(a) + " + " + print(b) + ")"
    case Subtract(a, b) => "(" + print(a) + " - " + print(b) + ")"
    case Multiply(a, b) => "(" + print(a) + " * " + print(b) + ")"
    case Divide(a, b) => "(" + print(a) + " / " + print(b) + ")"
    case Negate(n) => "-" + print(n)
    case Number(v) => v.toString()
  }

  def additive = multiplicative ~ ((keyword("+") | keyword("-")) ~ multiplicative).* >> {
    case (h, t) => t.foldLeft(h) {
      case (left, ("+", right)) => Add(left, right)
      case (left, ("-", right)) => Subtract(left, right)
    }
  }
  def multiplicative = primitive ~ ((keyword("*") | keyword("/")) ~ primitive).* >> {
    case (h, t) => t.foldLeft(h) {
      case (left, ("*", right)) => Multiply(left, right)
      case (left, ("/", right)) => Divide(left, right)
    }
  }
  def unary: Rule[Expr] = keyword("-").? ~ primitive >> {
    case (Some(_), v) => Negate(v)
    case (None, v) => v
  }
  def primitive: Rule[Expr] = Rule.firstOf(number >> Number, group)
  def group: Rule[Expr] = keyword("(") ~ additive ~ keyword(")") >> { case ((_, result), _) => result }

  def number: Rule[Int] = range('1', '9') ~ range('0', '9').* ~ ws >> {
    case ((h, t), _) => t.foldLeft(h - '0')((n, c) => n * 10 + (c - '0'))
  }

  def keyword(str: String) = s(str) ~ ws >> { case (s, _) => s }
  def ws = s(" ").* >> { case _ => () }
}

sealed abstract class Expr
case class Number(value: Int) extends Expr
case class Add(left: Expr, right: Expr) extends Expr
case class Subtract(left: Expr, right: Expr) extends Expr
case class Multiply(left: Expr, right: Expr) extends Expr
case class Divide(left: Expr, right: Expr) extends Expr
case class Negate(argument: Expr) extends Expr
