package net.gesekus.newsaswebapp.domainmodel.inmemoryimpl

import net.gesekus.newsaswebapp.domainmodel.Config


object InMemoryConfig extends Config {
  def chatRepository: net.gesekus.newsaswebapp.domainmodel.ChatRepository = {
    ChatRepository
  }
}