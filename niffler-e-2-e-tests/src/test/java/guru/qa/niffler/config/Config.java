package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String authUrl();

  String authJdbcUrl();

  String getewayUrl();

  String userDataUrl();
  String userdataJdbcUrl();

  String spendUrl();

  String spendJdbcUrl();

  String currencyJdbcUrl();

  String ghUrl();

}
