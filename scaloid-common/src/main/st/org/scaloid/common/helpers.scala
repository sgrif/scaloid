$license()$

package org.scaloid.common

import android.app._
import android.content._
import android.media._
import android.net._
import android.preference._
import android.widget._


trait AppHelpers {

  /**
   * Displays a simple alert dialog.
   * @param clickCallback This is called when the button is clicked. Does nothing by default.
   */
  @inline def alert(title: CharSequence, text: CharSequence, clickCallback: => Unit = {})(implicit context: Context) {
    new AlertDialogBuilder(title, text) {
      neutralButton(android.R.string.ok, clickCallback)
    }.show()
  }

  /**
   * Launches a new activity for a give uri. For example, opens a web browser for http protocols.
   */
  def openUri(uri: Uri)(implicit context: Context) {
    context.startActivity(new Intent(Intent.ACTION_VIEW, uri))
  }

  @inline def pendingService(intent: Intent)(implicit context: Context) =
    PendingIntent.getService(context, 0, intent, 0)

  @inline def pendingActivity(intent: Intent)(implicit context: Context) =
    PendingIntent.getActivity(context, 0, intent, 0)

  @inline def pendingActivity[T](implicit context: Context, mt: ClassManifest[T]) =
    PendingIntent.getActivity(context, 0, SIntent[T], 0)

}
object AppHelpers extends AppHelpers


trait ContentHelpers {

  def broadcastReceiver(filter: IntentFilter)(onReceiveBody: (Context, Intent) => Any)(implicit ctx: Context, reg: Registerable) {
    val receiver = new BroadcastReceiver {
      def onReceive(context: Context, intent: Intent) {
        onReceiveBody(context, intent)
      }
    }
    reg.onRegister(ctx.registerReceiver(receiver, filter))
    reg.onUnregister(ctx.unregisterReceiver(receiver))
  }

}
object ContentHelpers extends ContentHelpers


trait MediaHelpers {
  /**
   * Plays a sound from a given Uri.
   */
  def play(uri: Uri = notificationSound)(implicit context: Context) {
    val r = RingtoneManager.getRingtone(context, uri)
    if (r != null) {
      r.play()
    }
  }

  @inline def alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

  @inline def notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

  @inline def ringtoneSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

}
object MediaHelpers extends MediaHelpers


trait PreferenceHelpers {
  /**
   * Returns DefaultSharedPreferences object for given implicit context.
   */
  @inline def defaultSharedPreferences(implicit context: Context): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(context)

}
object PreferenceHelpers extends PreferenceHelpers

/**
 * Contains helper methods that displaying some UI elements.
 */
trait WidgetHelpers {
  /**
   * Displays a toast message.
   * This method can be called from any threads.
   */
  @inline def toast(message: CharSequence)(implicit context: Context) {
    runOnUiThread(Toast.makeText(context, message, Toast.LENGTH_SHORT).show())
  }

  /**
   * Displays a toast message for a longer time.
   * This method can be called from any threads.
   */
  @inline def longToast(message: CharSequence)(implicit context: Context) {
    runOnUiThread(Toast.makeText(context, message, Toast.LENGTH_LONG).show())
  }

  /**
   * Displays a dialog with spinner icon.
   * This method can be called from any threads.
   */
  @inline def spinnerDialog(title: String, message: String)(implicit context: Context): ProgressDialog =
    runOnUiThread(ProgressDialog.show(context, title, message, true))

}

/**
 * Contains helper methods that displaying some UI elements.
 */
object WidgetHelpers extends WidgetHelpers

/**
 * Aggregate trait for helpers.
 */
trait Helpers extends AppHelpers with ContentHelpers with MediaHelpers with PreferenceHelpers with WidgetHelpers

/**
 * Aggregate object for helpers.
 */
object Helpers extends Helpers
