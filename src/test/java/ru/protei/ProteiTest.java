package ru.protei;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.security.Key;
import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ProteiTest {
    private final String SUBMIT_BUTTON_SELECTOR = "button#dataSend";
    private final String EMAIL_FIELD_SELECTOR = "input#dataEmail";
    private final String NAME_FIELD_SELECTOR = "input#dataName";
    private final String GENDER_FIELD_SELECTOR = "select#dataGender";
    private final String CHECK_FIELD_MULTI_SELECTOR = "input[id^=dataCheck1]";
    private final String CHECK_TWO_FIELD_MULTI_SELECTOR = "input[id^=dataCheck1]";
    private final String RADIO_FIELD_MULTI_SELECTOR = "input[name=radioGroup]";
    private final String TABLE_SELECTOR = "table#dataTable";

    private final String LONG_TEXT =
            "Вот пример статьи на 1000 символов. Это достаточно маленький текст, " +
            "оптимально подходящий для карточек товаров в интернет-магазинах или для небольших информационных публикаций." +
            "В таком тексте редко бывает более 2-3 абзацев и обычно один подзаголовок. Но можно и без него." +
            "На 1000 символов рекомендовано использовать 1-2 ключа и одну картину." +
            "Текст на 1000 символов – это сколько примерно слов? Статистика Word показывает, что «тысяча» включает в себя 150-200 слов средней величины." +
            "Но, если злоупотреблять предлогами, союзами и другими частями речи на 1-2 символа, то количество слов неизменно возрастает." +
            "В копирайтерской деятельности принято считать «тысячи» с пробелами или без." +
            "Учет пробелов увеличивает объем текста примерно на 100-200 символов – именно столько раз мы разделяем слова свободным пространством." +
            "Считать пробелы заказчики не любят, так как это «пустое место»." +
            "Однако некоторые фирмы и биржи видят справедливым ставить стоимость за 1000 символов с пробелами," +
            "считая последние важным элементом качественного восприятия. Согласитесь, читать слитный текст без единого пропуска, никто не будет." +
            "Но большинству нужна цена за 1000 знаков без пробелов.";

    @Test
    public void submitEmptyProfileForm() {
        ProteiAuthTest.submitCorrectAuthData();

        $(SUBMIT_BUTTON_SELECTOR).click();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "submitEmptyProfileForm",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    @Test
    public void submitFormByEnterKey() {
        ProteiAuthTest.submitCorrectAuthData();

        $(EMAIL_FIELD_SELECTOR).setValue("invalid_email").pressEnter();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "submitFormByEnterKey",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    @Test
    public void fillingInAllFormFields() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue("Alice");
        $(GENDER_FIELD_SELECTOR).selectOption(1);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(1).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").shouldHave(text("Ok")).click();
    }

    @Test
    public void additionalFillingInAllFieldsForm() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue("Алиса Alice-мария12 Игоревна");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").shouldHave(text("Ok")).click();
    }

    @Test
    public void submitWithEmptyName() {
        ProteiAuthTest.submitCorrectAuthData();

        $(EMAIL_FIELD_SELECTOR).setValue("test@mail.com").pressEnter();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "submitWithEmptyName",
                        cssClass("uk-alert-danger"),
                        id("blankNameError"),
                        text("Поле имя не может быть пустым")
                )
        );
    }

    @Test
    public void displayFieldsTable() throws Throwable {
        ProteiAuthTest.submitCorrectAuthData();

        $$("table#dataTable thead tr th").shouldHave(
                CollectionCondition.texts(
                        "E-Mail",
                        "Имя",
                        "Пол",
                        "Выбор 1",
                        "Выбор 2"
                )

        );

    }

    @Test
    public void inputValidDataWithSingeCheckBoxAndParseTable() {
        ProteiAuthTest.submitCorrectAuthData();

        String email = "test@test.com";
        String name = "Alice";
        String genderOptionValue = "Женский";
        int checkFieldIndex = 1;
        int radioFieldIndex = 1;

        $(EMAIL_FIELD_SELECTOR).setValue(email);
        $(NAME_FIELD_SELECTOR).setValue(name);
        $(GENDER_FIELD_SELECTOR).selectOption(genderOptionValue);
        $$(CHECK_FIELD_MULTI_SELECTOR).get(checkFieldIndex).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(radioFieldIndex).click();

        String checkFieldValue = substringVariableText($$(CHECK_FIELD_MULTI_SELECTOR).get(checkFieldIndex).parent().text());
        String radioFieldValue = substringVariableText($$(RADIO_FIELD_MULTI_SELECTOR).get(radioFieldIndex).parent().text());

        $(SUBMIT_BUTTON_SELECTOR).click();

        $(".uk-modal.uk-open .uk-modal-dialog .uk-button.uk-modal-close").click();

        $$(TABLE_SELECTOR + " tbody tr td").shouldHave(
                CollectionCondition.texts(
                        email,
                        name,
                        genderOptionValue,
                        checkFieldValue,
                        radioFieldValue
                )
        );
    }
    @Test
    public void inputValidDataWithMultipleCheckBoxAndParseTable() {
        ProteiAuthTest.submitCorrectAuthData();

        String email = "test@test.com";
        String name = "Alice";
        String genderOptionValue = "Женский";
        int checkFieldFirstIndex = 0;
        int checkFieldSecondIndex = 1;
        int radioFieldIndex = 2;

        $(EMAIL_FIELD_SELECTOR).setValue(email);
        $(NAME_FIELD_SELECTOR).setValue(name);
        $(GENDER_FIELD_SELECTOR).selectOption(genderOptionValue);
        $$(CHECK_FIELD_MULTI_SELECTOR).get(checkFieldFirstIndex).click();
        $$(CHECK_FIELD_MULTI_SELECTOR).get(checkFieldSecondIndex).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(radioFieldIndex).click();

        String checkFieldFirstValue = substringVariableText($$(CHECK_FIELD_MULTI_SELECTOR).get(checkFieldFirstIndex).parent().text());
        String checkFieldSecondValue = substringVariableText($$(CHECK_FIELD_MULTI_SELECTOR).get(checkFieldSecondIndex).parent().text());
        StringJoiner checkFieldCollectionValues = new StringJoiner(", ")
                .add(checkFieldFirstValue)
                .add(checkFieldSecondValue);
        String radioFieldValue = substringVariableText($$(RADIO_FIELD_MULTI_SELECTOR).get(radioFieldIndex).parent().text());

        $(SUBMIT_BUTTON_SELECTOR).click();

        $(".uk-modal.uk-open .uk-modal-dialog .uk-button.uk-modal-close").click();

        $$(TABLE_SELECTOR + " tbody tr td").shouldHave(
                CollectionCondition.texts(
                        email,
                        name,
                        genderOptionValue,
                        checkFieldCollectionValues.toString(),
                        radioFieldValue
                )
        );
    }

    private String substringVariableText(String fullFieldText) {
        int substringCharsLength = "Вариант ".length();
        return fullFieldText.substring(substringCharsLength);
    }

    @Test
    public void checkGenderValues() {
        ProteiAuthTest.submitCorrectAuthData();

        $$(GENDER_FIELD_SELECTOR + " option").shouldHave(CollectionCondition.texts("Мужской", "Женский"));
    }

    @Test
    public void singleSpaceInName() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue(" ");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").shouldHave(text("Ok")).click();
    }

    @Test
    public void largestName() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue(LONG_TEXT);
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").shouldHave(text("Ok")).click();
    }

    @Test
    public void wrapperWidth() {
        ProteiAuthTest.submitCorrectAuthData();

        int startWrapperWidth = $(TABLE_SELECTOR).getSize().width;

        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue(LONG_TEXT);
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").click();

        int endWrapperWidth = $(TABLE_SELECTOR).getSize().width;

        Assert.assertNotEquals(startWrapperWidth, endWrapperWidth);
    }

    @Test
    public void largestEmail() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue(LONG_TEXT);
        $(NAME_FIELD_SELECTOR).setValue("пробелов.");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "largestEmail",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    private float getWrapperWidth() {
        String wrapperWidthValue  = $(TABLE_SELECTOR).getCssValue("width");
        return Float.parseFloat(wrapperWidthValue.substring(0, wrapperWidthValue.length() - 2));
    }

    @Test
    public void minimumName() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue("a");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").shouldHave(text("Ok")).click();
    }

    @Test
    public void minimumEmail() {
        ProteiAuthTest.submitCorrectAuthData();
        $(EMAIL_FIELD_SELECTOR).setValue("a");
        $(NAME_FIELD_SELECTOR).setValue("a");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(text("Добавить")).click();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR + " .uk-alert").shouldHave(
                and(
                        "minimumEmail",
                        cssClass("uk-alert-danger"),
                        id("emailFormatError"),
                        text("Неверный формат E-Mail")
                )
        );
    }

    @Test
    public void specialCharacterInTheNameField() {
        ProteiAuthTest.submitCorrectAuthData();

        String specChars = "!№;%:?*()_+?\"";

        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue(specChars);
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).click();
        $("button.uk-button.uk-button-primary.uk-modal-close").click();

        $$(TABLE_SELECTOR + " tbody tr td").get(1).shouldHave(text(specChars));
    }

    @Test
    public void xssInNameField() {
        ProteiAuthTest.submitCorrectAuthData();

        String xssNameFieldValue = "<script>document.body.innerHTML = '';</script>";

        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue(xssNameFieldValue);
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR).shouldNotHave(exist);
    }

    @Test
    public void xssInEmailField() {
        ProteiAuthTest.submitCorrectAuthData();

        String xssEmailFieldValue = "<script>document.body.innerHTML = '';</script>";

        $(EMAIL_FIELD_SELECTOR).setValue(xssEmailFieldValue);
        $(NAME_FIELD_SELECTOR).setValue("some_name");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).click();

        $(ProteiAuthTest.INPUT_WRAPPER_SELECTOR).shouldHave(exist);
    }

    @Test
    public void focusingNameField() {
        ProteiAuthTest.submitCorrectAuthData();

        $(NAME_FIELD_SELECTOR).click();
        $(NAME_FIELD_SELECTOR).shouldHave(focused);
    }

    @Test
    public void focusingEmailField() {
        ProteiAuthTest.submitCorrectAuthData();

        $(EMAIL_FIELD_SELECTOR).click();
        $(EMAIL_FIELD_SELECTOR).shouldHave(focused);
    }

    @Test
    public void doubleClickSubmitButton() {
        ProteiAuthTest.submitCorrectAuthData();

        $(EMAIL_FIELD_SELECTOR).setValue("test@test.com");
        $(NAME_FIELD_SELECTOR).setValue("a");
        $(GENDER_FIELD_SELECTOR).selectOption(0);
        $(CHECK_FIELD_MULTI_SELECTOR).click();
        $(CHECK_TWO_FIELD_MULTI_SELECTOR).click();
        $$(RADIO_FIELD_MULTI_SELECTOR).get(2).click();
        $(SUBMIT_BUTTON_SELECTOR).doubleClick();

        $$(TABLE_SELECTOR + " tbody tr td").shouldHave(CollectionCondition.size(5));
    }

    @Test
    public void copyFromEmailFieldToNameField() {
        ProteiAuthTest.submitCorrectAuthData();

        String uniqEmailValue = "someUniqEmail@value.com";

        $(EMAIL_FIELD_SELECTOR).setValue(uniqEmailValue);
        $(EMAIL_FIELD_SELECTOR).sendKeys(Keys.CONTROL, "a");
        $(EMAIL_FIELD_SELECTOR).sendKeys(Keys.CONTROL, "c");
        $(NAME_FIELD_SELECTOR).sendKeys(Keys.CONTROL, "v");

        Assert.assertEquals($(NAME_FIELD_SELECTOR).val(), uniqEmailValue);
    }


    @Test
    public void hasAndActiveSubmitButton() {
        ProteiAuthTest.submitCorrectAuthData();
        $(SUBMIT_BUTTON_SELECTOR).shouldHave(exist).shouldHave(visible);
    }

    @Test
    public void selectedSingleRadio() {
        ProteiAuthTest.submitCorrectAuthData();

        for (SelenideElement option : $$(RADIO_FIELD_MULTI_SELECTOR)) {
            option.click();
        }

        $$(RADIO_FIELD_MULTI_SELECTOR + ":checked").shouldHave(CollectionCondition.size(1));
    }
}