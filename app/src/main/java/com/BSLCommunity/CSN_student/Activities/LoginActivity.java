package com.BSLCommunity.CSN_student.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.BSLCommunity.CSN_student.Managers.AnimationManager;
import com.BSLCommunity.CSN_student.Objects.User;
import com.BSLCommunity.CSN_student.R;

import java.util.concurrent.Callable;

// Форма логина для пользователя
public class LoginActivity extends BaseActivity{
    Boolean can_click; //нажата кнопка

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimationManager.setAnimation(getWindow(), this);
        setContentView(R.layout.activity_login);
        createClickableSpan();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button loginButton = (Button) findViewById(R.id.activity_login_bt_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                TransitionDrawable transitionDrawable = (TransitionDrawable) view.getBackground();

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN  && can_click) {
                    transitionDrawable.startTransition(150);
                    view.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.btn_pressed));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP  && can_click) {
                    EditText NickName = (EditText) findViewById(R.id.activity_login_et_nickname);
                    EditText Password = (EditText) findViewById(R.id.activity_login_et_password);
                    if (!NickName.getText().toString().equals("") && !Password.getText().toString().equals("")){
                        User.login(getApplicationContext(), LoginActivity.this, NickName.getText().toString().toLowerCase(), Password.getText().toString(), new Callable<Void>() {
                            @Override
                            public Void call() throws Exception {
                                can_click = false;
                                startActivity(new Intent(getApplicationContext(), MainActivity.class)) ;//запускаем главное окно
                                return null;
                            }
                        });
                    }
                    else
                        Toast.makeText(LoginActivity.this, R.string.nodata,Toast.LENGTH_SHORT).show();


                    transitionDrawable.reverseTransition(150);
                    view.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.btn_unpressed));
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        can_click = true;
    }

    //возращает активити в исходное состояние
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //обработчик перехода на форму регистрации
    public void OnClickRegistration() {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this);
        startActivity(new Intent(this, RegistrationActivity.class), options.toBundle());
    }

    //кнопка перехода в регистрацию
    protected void createClickableSpan() {
        TextView text = findViewById(R.id.activity_login_tv_span2);

        SpannableString ss = new SpannableString(text.getText());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                OnClickRegistration();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(0xFF5EE656);
            }
        };
        ss.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(ss);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}


