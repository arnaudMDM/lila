package lila.socket

import scala.concurrent.duration._

import lila.hub.Trouper
import lila.hub.actorApi.HasUserId

abstract class SocketTrouper[M <: SocketMember](
    uidTtl: Duration
) extends SocketBase[M] with Trouper {

  override def start() = {
    // #TODO find another way to propaget Deploy event (through the TrouperMap)
    // lilaBus.publish(lila.socket.SocketHub.Open(this), 'socket)
  }

  override def stop() = {
    members foreachKey ejectUidString
  }

  val receiveTrouper: PartialFunction[Any, Unit] = {
    case Trouper.Ask(HasUserId(userId), promise) => promise success hasUserId(userId)
  }

  val process = receiveSpecific orElse receiveTrouper orElse receiveGeneric
}