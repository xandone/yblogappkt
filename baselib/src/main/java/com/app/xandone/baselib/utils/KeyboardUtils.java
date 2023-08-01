package com.app.xandone.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bbgo.common_base.util.AppUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: xiao
 * created on: 2023/7/4 09:51
 * description:
 */
public class KeyboardUtils {
    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    public static void hideSoftInput(@Nullable final Activity activity) {
        if (activity == null) {
            return;
        }
        hideSoftInput(activity.getWindow());
    }

    /**
     * Hide the soft input.
     *
     * @param window The window.
     */
    public static void hideSoftInput(@Nullable final Window window) {
        if (window == null) {
            return;
        }
        View view = window.getCurrentFocus();
        if (view == null) {
            View decorView = window.getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null) {
                view = new EditText(window.getContext());
                view.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(view, 0, 0);
            } else {
                view = focusView;
            }
            view.requestFocus();
        }
        hideSoftInput(view);
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    public static void hideSoftInput(@NonNull final View view) {
        InputMethodManager imm =
                (InputMethodManager) AppUtil.sApp.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
