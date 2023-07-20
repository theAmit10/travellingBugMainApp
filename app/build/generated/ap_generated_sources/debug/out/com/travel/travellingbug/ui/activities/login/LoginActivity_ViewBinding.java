// Generated code from Butter Knife. Do not modify!
package com.travel.travellingbug.ui.activities.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hbb20.CountryCodePicker;
import com.travel.travellingbug.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view7f0a040e;

  private View view7f0a0122;

  private View view7f0a0121;

  private View view7f0a0659;

  private View view7f0a064b;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.etEmail = Utils.findRequiredViewAsType(source, R.id.etEmail, "field 'etEmail'", EditText.class);
    target.etPassword = Utils.findRequiredViewAsType(source, R.id.etPassword, "field 'etPassword'", EditText.class);
    target.ccp = Utils.findRequiredViewAsType(source, R.id.ccp, "field 'ccp'", CountryCodePicker.class);
    view = Utils.findRequiredView(source, R.id.nextIcon, "field 'nextIcon' and method 'btnLoginClick'");
    target.nextIcon = Utils.castView(view, R.id.nextIcon, "field 'nextIcon'", Button.class);
    view7f0a040e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnLoginClick();
      }
    });
    target.mobile_no = Utils.findRequiredViewAsType(source, R.id.mobile_no, "field 'mobile_no'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btnGoogle, "method 'btnGoogleClick'");
    view7f0a0122 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnGoogleClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnFb, "method 'btnFbClick'");
    view7f0a0121 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.btnFbClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.txtSignUp, "method 'txtSignUpClick'");
    view7f0a0659 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.txtSignUpClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.txtForget, "method 'txtForgetClick'");
    view7f0a064b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.txtForgetClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etEmail = null;
    target.etPassword = null;
    target.ccp = null;
    target.nextIcon = null;
    target.mobile_no = null;

    view7f0a040e.setOnClickListener(null);
    view7f0a040e = null;
    view7f0a0122.setOnClickListener(null);
    view7f0a0122 = null;
    view7f0a0121.setOnClickListener(null);
    view7f0a0121 = null;
    view7f0a0659.setOnClickListener(null);
    view7f0a0659 = null;
    view7f0a064b.setOnClickListener(null);
    view7f0a064b = null;
  }
}
