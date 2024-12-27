package samokattest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pageobjects.MainSamokat;
import pageobjects.OrderSamokat;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.containsString;

// Тест для проверки позитивного сценария оформления заказа
@RunWith(Parameterized.class)
public class OrderCheck {
    // Веб-драйвер
    private WebDriver webDriver;
    // URL страницы
    private final String mainPageUrl = "https://qa-scooter.praktikum-services.ru";
    // Переменные для параметров теста - данных для оформления заказа
    private final String name, surname, address, metro, phone, date, term, color, comment;
    // Сообщение об успешном оформлении заказа
    private final String expectedOrderSuccessText = "Заказ оформлен";

    // Конструктор класса
    public OrderCheck(
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
    @Parameterized.Parameters
    public static Object[][] setDataForOrder() {
        return new Object[][] {
                {"Данил", "Тестовый", "г. СПБ, ул. Невский-проспект, д. 52", "Черкизовская", "1111111111111", "02.12.2024", "семеро суток", "чёрный жемчуг", "Завелось?"},
                {"Даниил", "Рестовый", "г. СПБ, ул. Невский-проспект, д. 52 стр 1", "Комсомольская", "1111111111112", "03.12.2024", "семеро суток", "серая безысходность", "А сейчас?"},
        };
    }

    @Before
    public void startUp() {
        WebDriverManager.chromedriver().setup();
        this.webDriver = new ChromeDriver(); // Здесь тест падает на подтверждении оформления заказа
        this.webDriver.get(mainPageUrl);
    }

    @After
    public void tearDown() {
        if (this.webDriver != null) {
            this.webDriver.quit();
        }
    }

    // Тест для проверки процесса оформления заказа после нажатия на кнопку "Заказать" в шапке
    @Test
    public void orderWithHeaderButtonWhenSuccess() {
        MainSamokat mainPage = new MainSamokat(this.webDriver);
        OrderSamokat orderPage = new OrderSamokat(this.webDriver);

        mainPage.clickOnCookieAcceptButton();
        mainPage.clickOrderButtonHeader();
        makeOrder(orderPage);

        MatcherAssert.assertThat(
                "Problem with creating a new order",
                orderPage.getNewOrderSuccessMessage(),
                containsString(this.expectedOrderSuccessText)
        );
    }

    // Тест для проверки процесса оформления заказа после нажатия на кнопку "Заказать" в теле сайта
    @Test
    public void orderWithBodyButtonWhenSuccess() {
        MainSamokat mainPage = new MainSamokat(this.webDriver);
        OrderSamokat orderPage = new OrderSamokat(this.webDriver);

        mainPage.clickOnCookieAcceptButton();
        mainPage.clickOrderButtonBody();
        makeOrder(orderPage);

        MatcherAssert.assertThat(
                "Problem with creating a new order",
                orderPage.getNewOrderSuccessMessage(),
                containsString(this.expectedOrderSuccessText)
        );
    }

    // Метод, описывающий процедуру оформления заказа
    private void makeOrder(OrderSamokat orderPage) {
        orderPage.waitForLoadForm();

        orderPage.setName(this.name);
        orderPage.setSurname(this.surname);
        orderPage.setAddress(this.address);
        orderPage.setMetro(this.metro);
        orderPage.setPhone(this.phone);

        orderPage.clickNextButton();

        orderPage.setDate(this.date);
        orderPage.setTerm(this.term);
        orderPage.setColor(this.color);
        orderPage.setComment(this.comment);

        orderPage.makeOrder();
    }
}
