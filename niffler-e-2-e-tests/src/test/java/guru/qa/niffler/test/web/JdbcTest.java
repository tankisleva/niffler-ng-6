package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {
    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-253535gg",
                                "duckdf",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spgend-name-txg",
                        "kgdlkgldkg"
                )
        );

        System.out.println(spend);
    }
}
