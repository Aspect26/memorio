package aspect.memorio.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import aspect.memorio.R;

public class SnackbarUtils {

    public static void showUndoSnackbar(View view, int actionDoneStringResource, View.OnClickListener undoAction) {
        Snackbar undoSnackBar = Snackbar.make(view, view.getResources().getString(actionDoneStringResource), Snackbar.LENGTH_LONG);
        undoSnackBar.setAction(R.string.undo, undoAction);
        undoSnackBar.setActionTextColor(view.getResources().getColor(R.color.colorPrimaryLight));
        undoSnackBar.show();
    }

}
