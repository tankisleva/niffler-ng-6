package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @User(
          username = "duck",
          categories = @Category(
                  isArchive = false
          ),
          spendings = @Spending(
                  category = "Обучение",
                  description = "Обучение Advanced 2.0",
                  amount = 79990
          )
  )
//  @DisabledByIssue("1")
  @Test
  void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {

    final String newDescription = "Обучение Niffler Next Generation";

    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("duck", "12345")
        .editSpending(spend.description())
        .setNewSpendingDescription(newDescription)
        .save();

    new MainPage().checkThatTableContainsSpending(newDescription);
  }
}
