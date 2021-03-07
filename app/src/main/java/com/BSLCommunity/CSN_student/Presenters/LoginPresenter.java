package com.BSLCommunity.CSN_student.Presenters;

import com.BSLCommunity.CSN_student.Models.UserModel;
import com.BSLCommunity.CSN_student.R;
import com.BSLCommunity.CSN_student.ViewInterfaces.LoginView;
import com.BSLCommunity.CSN_student.lib.CallBack;

public class LoginPresenter {

    private final String validRegEx = "([A-Z,a-z]|[А-Я,а-я]|[ІЇЄiїєЁё]|[0-9])+"; // Регулярка для проверки валидации
    private final LoginView loginView; // View регистрации
    private final UserModel userModel; // Модель пользователя, нужна для логина

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        this.userModel= UserModel.getUserModel();
    }

    /**
     * Попытка авторизации пользователя
     * @param nickname - никнейм
     * @param password - пароль
     */
    public void tryLogin(String nickname, String password) {
        if (!nickname.matches(validRegEx) || !password.matches(validRegEx)) {
            this.loginView.showToastError(R.string.invalid_data);
        }
        else {
            this.userModel.login(nickname, password, new CallBack<Void>() {
                @Override
                public void call(Void data) {
                    try {
                        loginView.openMain();
                    } catch (Exception ignored) {}
                }

                @Override
                public void fail(int idResString) {
                    try {
                        loginView.showToastError(idResString);
                    } catch (Exception ignored) {}
                }
            });
        }
    }
}
