package com.avsystem.fred
package traits

abstract class FirstImpl { this: First =>
  def veryLongCode: Int = data * 2

  def returnSelf: SealedOrNot = this
}
