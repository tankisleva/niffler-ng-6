package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@WebTest
public class RegisterWebTest {

    private static final Config CFG = Config.getInstance();
    String pass = RandomDataUtils.randomPass(3,8);
    String userName = RandomDataUtils.randomUserName();

    @Test
    void shouldRegisterNewUser() {
        final String messageSuccessRegister = "Congratulations! You've registered!";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccountButton()
                .setUserName(userName)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(messageSuccessRegister);
    }



    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        final String messageSuccessRegister = "Congratulations! You've registered!";
        final String messageErrorExistRegister = "Username `" + userName + "` already exists";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccountButton()
                .setUserName(userName)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldSuccessRegister(messageSuccessRegister)
                .clickSignInButton()
                .clickCreateAccountButton()
                .setUserName(userName)
                .setPassword(pass)
                .setPasswordSubmit(pass)
                .clickSubmitButton()
                .shouldErrorRegister(messageErrorExistRegister);
    }



    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        final String messageErrorNotEqualPass = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateAccountButton()
                .setUserName(userName)
                .setPassword(pass)
                .setPasswordSubmit("123")
                .clickSubmitButton()
                .shouldErrorRegister(messageErrorNotEqualPass);
    }
}
