package com.avsystem.fred
package rest

import com.avsystem.commons.rpc.AsRaw
import com.avsystem.commons.serialization.json.JsonStringOutput
import com.avsystem.commons.serialization.whenAbsent
import io.udash.rest._
import io.udash.rest.openapi.adjusters.description
import io.udash.rest.openapi.{Header => _, _}
import io.udash.rest.raw._

@description("juzer luzer")
case class User(id: String, name: String, birthYear: Int)
object User extends RestDataCompanion[User]

trait UserApi {
  /** Returns newly created user */
  @PUT def createUser(@Body user: User): Future[Unit]
  @PATCH def updateName(@Path id: String, name: String): Future[User]
  @GET("") def getUser(id: String, @whenAbsent(true) mustBeFred: Boolean = whenAbsent.value): Future[User]
}
object UserApi extends DefaultRestApiCompanion[UserApi] {
  type TT = AsRaw[RawRest.Async[RestResponse], Try[Future[User]]]
}

trait AuthApi {
  @Prefix("") def auth(@Header("Authorization") token: String): UserApi
}
object AuthApi extends DefaultRestApiCompanion[AuthApi]

trait GlobalApi {
  @Prefix("userssss") def users(org: String): UserApi
}
object GlobalApi extends DefaultRestApiCompanion[GlobalApi]

class GlobalApiImpl extends GlobalApi {
  def users(org: String): UserApi =
    throw HttpErrorException(404, "nie ma")
}

object PrintThis {
  def main(args: Array[String]): Unit = {
    val openapi = GlobalApi.openapiMetadata.openapi(
      Info("Some REST API", "0.1", description = "Some example REST API"),
      servers = List(Server("http://localhost"))
    )
    println(JsonStringOutput.writePretty(openapi))
  }
}