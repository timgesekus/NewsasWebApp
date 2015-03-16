package net.gesekus.newsaswebapp.actors

sealed trait SubscriptionMessages 
case class Subscribe() extends SubscriptionMessages
case class Unsubscribe() extends SubscriptionMessages