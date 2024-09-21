package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    final String messageUpdateProfile = "Profile successfully updated";


    @Test
    void pictureShouldBeUploaded() {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("oleg", "123456")
                .clickAvatar()
                .clickProfile()
                .editName("Petya")
                .saveName()
                .shouldSuccessProfileUpdated(messageUpdateProfile);
    }




    @User(
            username = "superduck1",
            categories = @Category(
                    isArchive = false
            )
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("superduck1", "12345")
                .clickAvatar()
                .clickProfile()
                .clickArchiveButtonForCategoryName(category.name())
                .clickArchiveButtonSubmit()
                .shouldBeVisibleArchiveSuccessMessage(category.name())
                .shouldNotVisibleArchiveCategory(category.name());
    }

    //
    @User(
            username = "superduck1",
            categories = @Category(
                    isArchive = true
            )
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) throws InterruptedException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("superduck1", "12345")
                .clickAvatar()
                .clickProfile()
                .clickShowArchiveCategoryButton()
                .clickUnarchiveButtonForCategoryName(category.name())
                .clickUnarchiveButtonSubmit()
                .shouldBeVisibleUnarchiveSuccessMessage(category.name())
                .clickShowArchiveCategoryButton()
                .shouldVisibleActiveCategory(category.name());
    }
}
