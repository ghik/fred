package com.avsystem.fred
package traits

sealed trait SealedOrNot
case class First(data: Int) extends FirstImpl with SealedOrNot
