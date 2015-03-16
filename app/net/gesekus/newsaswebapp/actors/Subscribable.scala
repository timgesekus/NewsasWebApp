package net.gesekus.newsaswebapp.actors

import akka.actor.Actor
import akka.actor.ActorRef

trait Subscribable {
  this : Actor =>
  val subscribers: Set[ActorRef] = Set()

  def addSubscriber() {
    subscribers + sender()  
  }
  
  def removeSubscriber() {
    subscribers - sender()
  }
  
  
  def send(message: Object) {
    subscribers.foreach { _ tell (message,self) }
  }

}