package galactrix.ui

import java.lang.reflect.InvocationTargetException
import scala.swing.MainFrame
import scala.swing.SimpleSwingApplication

class TranslucentMainFrame(val alpha: Float) extends MainFrame {
  if (peer.getPeer != null) {
    val peerClass = peer.getPeer.getClass
    
    try {
      val nativeClass = Class.forName("apple.awt.CWindow")
      
      if (nativeClass.isAssignableFrom(peerClass)) {
        val setAlpha = nativeClass.getMethod("setAlpha", java.lang.Float.TYPE)
        setAlpha.invoke(peer, Float.box(math.max(0f, math.min(1f, alpha))))
      }
    } catch {
      case _: ClassNotFoundException => ()
      case _: NoSuchMethodException => ()
      case _: IllegalAccessException => ()
      case _: InvocationTargetException => ()
    }
  }
}