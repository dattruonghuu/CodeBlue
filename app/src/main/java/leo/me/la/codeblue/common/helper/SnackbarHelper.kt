package leo.me.la.codeblue.common.helper

import android.app.Activity
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar

class SnackbarHelper {
    private var messageSnackbar: Snackbar? = null


    private enum class DismissBehavior {
        HIDE,
        SHOW,
        FINISH
    }

    /** Shows a snackbar with a given message.  */
    fun showMessage(activity: Activity, message: String) {
        show(activity, message, DismissBehavior.HIDE)
    }


    /**
     * Shows a snackbar with a given error message. When dismissed, will finish the activity. Useful
     * for notifying errors, where no further interaction with the activity is possible.
     */
    fun showError(activity: Activity, errorMessage: String) {
        show(activity, errorMessage, DismissBehavior.FINISH)
    }

    /**
     * Hides the currently showing snackbar, if there is one. Safe to call from any thread. Safe to
     * call even if snackbar is not shown.
     */
    fun hide(activity: Activity) {
        activity.runOnUiThread {
            if (messageSnackbar != null) {
                messageSnackbar!!.dismiss()
            }
            messageSnackbar = null
        }
    }

    private fun show(
            activity: Activity, message: String, dismissBehavior: DismissBehavior) =
            activity.runOnUiThread {
                messageSnackbar = Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        message,
                        Snackbar.LENGTH_INDEFINITE)
                messageSnackbar!!.view.setBackgroundColor(BACKGROUND_COLOR)
                if (dismissBehavior != DismissBehavior.HIDE) {
                    messageSnackbar!!.setAction(
                            "Dismiss"
                    ) { messageSnackbar!!.dismiss() }
                    if (dismissBehavior == DismissBehavior.FINISH) {
                        messageSnackbar!!.addCallback(
                                object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                        super.onDismissed(transientBottomBar, event)
                                        activity.finish()
                                    }
                                })
                    }
                }
                this.messageSnackbar!!.show()
            }

    companion object {
        private var BACKGROUND_COLOR = -0x40cdcdce
        val instance = SnackbarHelper()
    }
}