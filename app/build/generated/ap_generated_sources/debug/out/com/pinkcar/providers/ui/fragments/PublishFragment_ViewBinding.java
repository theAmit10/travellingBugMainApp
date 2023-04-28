// Generated code from Butter Knife. Do not modify!
package com.pinkcar.providers.ui.fragments;

import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.pinkcar.providers.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PublishFragment_ViewBinding implements Unbinder {
  private PublishFragment target;

  @UiThread
  public PublishFragment_ViewBinding(PublishFragment target, View source) {
    this.target = target;

    target.llFlow = Utils.findRequiredViewAsType(source, R.id.llFlow, "field 'llFlow'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PublishFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.llFlow = null;
  }
}
