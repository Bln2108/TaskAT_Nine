package ru.netology.rest;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;
import static ru.netology.rest.DataGenerator.*;

public class ClassTestPaters {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }


    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(generateCity("ru"));
        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(" ");
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(generateName("ru"));
        $("[data-test-id='phone'] input").setValue(generatePhone("ru"));
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));

        $("[data-test-id='date'] input").doubleClick();
        $("[data-test-id='date'] input").sendKeys(" ");
        $("[data-test-id='date'] input").sendKeys(secondMeetingDate);
        $(byText("Запланировать")).click();

        $(withText("Перепланировать")).shouldBe(visible, Duration.ofSeconds(15));
        $(byText("Перепланировать")).click();
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));
    }

}
