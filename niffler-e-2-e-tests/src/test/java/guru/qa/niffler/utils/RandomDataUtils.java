package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String randomUserName(){
        return faker.name().username();
    }

    public static String randomName(){
        return faker.name().lastName();
    }

    public static String randomSurName(){
        return faker.name().name();
    }

    public static String randomCategoryName(){
        return faker.name().title();
    }

    public static String randomSentence(int wordsCount){
        return faker.lorem().sentence(wordsCount);
    }

    public static String randomPass(int min, int max){
        return faker.internet().password(min, max);
    }


}
