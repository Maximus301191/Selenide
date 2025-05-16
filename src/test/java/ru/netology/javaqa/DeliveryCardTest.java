package ru.netology.javaqa;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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

    @Test
    void shouldDeliverCardByListsForSevenDays() {

        long addDays = 7;
        String planningDay = generateDate(addDays, "d");
        String planningDate = generateDate(addDays, "dd.MM.yyyy");

        Selenide.open("http://localhost:9999");
        SelenideElement formElement = $("form");

        formElement.$("[data-test-id='city'] input").setValue("Ма");
        $$(".menu .menu-item__control").find(exactText("Майкоп")).click();
        formElement.$(".icon").click();

        LocalDate planningDateL = LocalDate.now().plusDays(addDays);
        LocalDate defaultDate = LocalDate.now().plusDays(4);

        ElementsCollection arrow = $$(".calendar__arrow_direction_right");
        int monthDiff = (planningDateL.getYear() - defaultDate.getYear()) * 12 + (planningDateL.getMonthValue() - defaultDate.getMonthValue());
        while (monthDiff > 0) {
            $(arrow.get(1)).click();
            monthDiff = monthDiff-1;
        }
        $$(".calendar__day").find(exactText(planningDay)).click();

        formElement.$("[data-test-id='name'] input").setValue("Петров Иван");
        formElement.$("[data-test-id='phone'] input").setValue("+79119638521");
        formElement.$("[data-test-id='agreement']").click();
        formElement.$("[class='button__content']").click();
        $("[data-test-id=notification].notification_visible .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно!"));
        $("[data-test-id=notification].notification_visible .notification__content").shouldBe(visible).shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }
}