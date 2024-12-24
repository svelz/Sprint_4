package samokatTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import pageObjects.main;
import pageObjects.order;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.CoreMatchers.containsString;

// Тест для проверки позитивного сценария оформления заказа
@RunWith(Parameterized.class)
public class orderCheck {
    // Веб-драйвер
    private WebDriver webDriver;
    // URL страницы
    private final String mainPageUrl = "https://qa-scooter.praktikum-services.ru";
    // Переменные для параметров теста - данных для оформления заказа
    private final String name, surname, address, metro, phone, date, term, color, comment;
    // Сообщение об успешном оформлении заказа
    private final String expectedOrderSuccessText = "Заказ оформлен";
    // Контруктор класса
    public orderCheck(
            String name,
            String surname,
            String address,
            String metro,
            String phone,
            String date,
            String term,
            String color,
            String comment
    ) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.term = term;
        this.color = color;
        this.comment = comment;
    }
    // Параметры для запуска теста
    @Parameterized.Parameters(name = "Оформление заказа. Позитивный сценарий. Пользователь: {0} {1}")
    public static Object[][] setDataForOrder() {
        return new Object[][] {
                {"Данил", "Тестовый", "г. СПБ,  ул. Невский-проспект, д. 52", "Черкизовская", "1111111111111", "02.12.2024", "семеро суток", "чёрный жемчуг", "Завелось?"},
                {"Даниил", "Рестовый", "г. СПБ,  ул. Невский-проспект, д. 52 стр 1", "Комсомольская", "1111111111112", "03.12.2024", "семеро суток", "серая безысходность", "А сейчас?"},
        };
    }
    @Before
    public void startUp() {
        WebDriverManager.chromedriver().setup();
        this.webDriver = new ChromeDriver();    // здесь тест падает на подтверждении оформления заказа
        this.webDriver.get(mainPageUrl);
    }
    @After
    public void tearDown() {
        this.webDriver.quit();
    }
    // Тест для проверки процесса оформления заказа после нажатия на кнопку "Заказать" в шапке
    @Test
    public void orderWithHeaderButtonWhenSuccess() {
        main main = new main(this.webDriver);
        order order = new order(this.webDriver);

        main.clickOnCookieAcceptButton();
        main.clickOrderButtonHeader();
        makeOrder(order);

        MatcherAssert.assertThat(
                "Problem with creating a new order",
                order.getNewOrderSuccessMessage(),
                containsString(this.expectedOrderSuccessText)
        );
    }
    // Тест для проверки процесса оформления заказа после нажатия на кнопку "Заказать" в теле сайта
    @Test
    public void orderWithBodyButtonWhenSuccess() {
        main main = new main(this.webDriver);
        order order = new order(this.webDriver);

        main.clickOnCookieAcceptButton();
        main.clickOrderButtonBody();
        makeOrder(order);

        MatcherAssert.assertThat(
                "Problem with creating a new order",
                order.getNewOrderSuccessMessage(),
                containsString(this.expectedOrderSuccessText)
        );
    }

    // Метод, описывающий процедуру оформления заказа
    private void makeOrder(order order) {
        order.waitForLoadForm();

        order.setName(this.name);
        order.setSurname(this.surname);
        order.setAddress(this.address);
        order.setMetro(this.metro);
        order.setPhone(this.phone);

        order.clickNextButton();

        order.setDate(this.date);
        order.setTerm(this.term);
        order.setColor(this.color);
        order.setComment(this.comment);

        order.makeOrder();
    }
}
