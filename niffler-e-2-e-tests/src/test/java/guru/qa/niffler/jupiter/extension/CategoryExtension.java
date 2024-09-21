package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendApiClient spendApiClient = new SpendApiClient();

//    @Override
//    public void beforeEach(ExtensionContext context) {
//        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
//                .ifPresent(anno ->{
//                    String name = RandomDataUtils.randomCategoryName();
//                    CategoryJson categoryJson = new CategoryJson(
//                            null,
//                            anno.name().isEmpty() ? name : anno.name(),
//                            anno.username(),
//                            false
//                    );
//
//                    CategoryJson createCategory = spendApiClient.addCategory(categoryJson);
//
//                    if (anno.isArchive()) {
//                        CategoryJson archivedCategory = new CategoryJson(
//                                createCategory.id(),
//                                createCategory.name(),
//                                createCategory.username(),
//                                true
//                        );
//                        createCategory = spendApiClient.updateCategory(archivedCategory);
//                    }
//
//                    context.getStore(NAMESPACE).put(context.getUniqueId(), createCategory);
//                });
//    }

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno ->{
                    if (ArrayUtils.isNotEmpty(userAnno.categories())) {
                     Category categoryAnno = userAnno.categories()[0];
                     String name = RandomDataUtils.randomCategoryName();
                     CategoryJson categoryJson = new CategoryJson(
                            null,
                             categoryAnno.name().isEmpty() ? name : categoryAnno.name(),
                             userAnno.username(),
                            false
                     );

                     CategoryJson createCategory = spendApiClient.addCategory(categoryJson);

                     if (categoryAnno.isArchive()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                createCategory.id(),
                                createCategory.name(),
                                userAnno.username(),
                                true
                        );
                        createCategory = spendApiClient.updateCategory(archivedCategory);
                     }

                     context.getStore(NAMESPACE).put(context.getUniqueId(), createCategory);
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);

        if (categoryJson.archived()) {
            CategoryJson archivedCategory = new CategoryJson(
                    categoryJson.id(),
                    categoryJson.name(),
                    categoryJson.username(),
                    true
            );
            spendApiClient.updateCategory(archivedCategory);
        }
    }
}