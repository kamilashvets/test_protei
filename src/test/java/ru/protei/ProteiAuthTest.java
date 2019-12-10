package ru.protei;

import com.codeborne.selenide.Selenide;
import org.junit.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProteiAuthTest {
    final static String AUTH_WRAPPER_SELECTOR = "#authPage";
    final static String INPUT_WRAPPER_SELECTOR = "#inputsPage";

    private final static String CORRECT_LOGIN = "test@protei.ru";
    private final static String CORRECT_PASSWORD = "test";

    private final static String LOGIN_FIELD_SELECTOR = "input#loginEmail";
    private final static String PASSWORD_FIELD_SELECTOR = "input#loginPassword";
    private final static String SUBMIT_BUTTON_SELECTOR = "button#authButton";

    /**
     * Open auth page
     */
    private static void openAuthPage() {
        Selenide.open("file:///C:/Users/User/Downloads/Telegram%20Desktop/testqa.html");
    }

    static void submitCorrectAuthData() {
        openAuthPage();
        $(LOGIN_FIELD_SELECTOR).setValue(CORRECT_LOGIN);
        $(PASSWORD_FIELD_SELECTOR).setValue(CORRECT_PASSWORD);
        $(SUBMIT_BUTTON_SELECTOR).click();
    }

    @Test
    public void hasAuthForm() {
        openAuthPage();
        $(AUTH_WRAPPER_SELECTOR).shouldHave(and("hasAuthWrapper", exist, visible));
        $(LOGIN_FIELD_SELECTOR).shouldHave(and("hasLoginField", exist, visible));
        $(PASSWORD_FIELD_SELECTOR).shouldHave(and("hasPasswordField", exist, visible));
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(and("hasSubmitButton", exist, visible));
    }

    @Test
    public void successAuth() {
        submitCorrectAuthData();

        $(AUTH_WRAPPER_SELECTOR).shouldNotHave(visible);
        $(INPUT_WRAPPER_SELECTOR).shouldHave(visible);
    }

    @Test
    public void failureAuth() {
        openAuthPage();
        $(LOGIN_FIELD_SELECTOR).setValue("test@test.ru");
        $(PASSWORD_FIELD_SELECTOR).setValue(CORRECT_PASSWORD);
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(AUTH_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "failureAuth",
                        cssClass("uk-alert-danger"),
                        id("invalidEmailPassword"),
                        text("Неверный E-Mail или пароль")
                )
        );
    }

    @Test
    public void invalidEmail() {
        openAuthPage();
        $(LOGIN_FIELD_SELECTOR).setValue("incorrect_email");
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(AUTH_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "invalidEmail",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    @Test
    public void validEmailAndEmptyPasswordField() {
        openAuthPage();
        $(LOGIN_FIELD_SELECTOR).setValue(CORRECT_LOGIN);
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(AUTH_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "validEmailAndEmptyPasswordField",
                        cssClass("uk-alert-danger"),
                        id("invalidEmailPassword"),
                        text("Неверный E-Mail или пароль")
                )
        );
    }

    @Test
    public void emptyEmailFieldAndValidPassword() {
        openAuthPage();
        $(PASSWORD_FIELD_SELECTOR).setValue(CORRECT_PASSWORD);
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(AUTH_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "emptyEmailFieldAndValidPassword",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    @Test
    public void emptyEmailAndPasswordFields() {
        openAuthPage();
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(AUTH_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "emptyEmailFieldAndValidPassword",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    @Test
    public void validFieldsAndPressEnter() {
        openAuthPage();
        $(LOGIN_FIELD_SELECTOR).setValue(CORRECT_LOGIN);
        $(PASSWORD_FIELD_SELECTOR).setValue(CORRECT_PASSWORD).pressEnter();
    }

    @Test
    public void hidingErrorAlert() {
        openAuthPage();
        $(LOGIN_FIELD_SELECTOR).setValue("test@test.ru");
        $(PASSWORD_FIELD_SELECTOR).setValue("incorrect_password");
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(AUTH_WRAPPER_SELECTOR + " .uk-alert-close.uk-close").click();
        $(AUTH_WRAPPER_SELECTOR + " .uk-alert").shouldNotHave(exist);
    }
}
