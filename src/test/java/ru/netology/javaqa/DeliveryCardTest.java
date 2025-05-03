package ru.netology.javaqa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DeliveryCardTest {


    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    String planningDate = generateDate(4, "dd.MM.yyyy");

    @Test
    public void ShouldSuccessFormDeliveryCard() {
        Selenide.open("http://localhost:9999");
        SelenideElement formElement = $("form");
        formElement.$("[data-test-id='city'] input").setValue("Кемерово");
        formElement.$("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        formElement.$("[data-test-id='date'] input").setValue(planningDate);
        formElement.$("[data-test-id='name'] input").setValue("Петров Иван");
        formElement.$("[data-test-id='phone'] input").setValue("+79119638521");
        formElement.$("[data-test-id='agreement']").click();
        formElement.$("[class='button__content']").click();
        $(".notification__title").shouldHave(Condition.text("Успешно!"), Duration.ofSeconds(15)).shouldBe(visible);
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);


    }
}