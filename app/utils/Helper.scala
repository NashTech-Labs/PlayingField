package utils

import play.api.mvc.RequestHeader

object Helper {
  /*
   * find URI
   * @param request - HTTP request
   */
  def findUri(request: RequestHeader): String = {
    request.uri
  }
  /*
   * find session values
   * @param request - HTTP request
   * @param element - find session value stored in element
   */
  def findSession(request: RequestHeader, element: String): String = {
    request.session.get(element).map { sessionvalue =>
      sessionvalue
    }.getOrElse {
      " "
    }

  }

  /*
   * find flash values
   * @param request - HTTP request
   * @param element - find flash value stored in element
   */
  def findFlash(request: RequestHeader, element: String): String = {
    
    request.flash.get(element).getOrElse {
      " "
    }

  }

} 
