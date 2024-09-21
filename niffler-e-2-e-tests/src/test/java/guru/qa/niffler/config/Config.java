package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String frontUrl();

  String authUrl();

  String getewayUrl();

  String userDataUrl();

  String spendUrl();

  String ghUrl();
}
