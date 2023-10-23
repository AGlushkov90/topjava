package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 3;
    public static final int MEAL1_ID = START_SEQ + 4;
    public static final int MEAL2_ID = START_SEQ + 5;
    public static final int MEAL3_ID = START_SEQ + 6;
    public static final int MEAL4_ID = START_SEQ + 7;
    public static final int MEAL5_ID = START_SEQ + 8;
    public static final int MEAL6_ID = START_SEQ + 9;
    public static final int MEAL7_ID = START_SEQ + 10;
    public static final int MEAL8_ID = START_SEQ + 11;
    public static final int NOT_FOUND = 11;
    public static final Meal userMeal = new Meal(MEAL_ID, LocalDateTime.of(2023, 10, 30, 10, 0), "Завтрак юзер", 500);
    public static final Meal userMeal1 = new Meal(MEAL1_ID, LocalDateTime.of(2023, 10, 30, 13, 0), "Обед юзер", 1000);
    public static final Meal userMeal2 = new Meal(MEAL2_ID, LocalDateTime.of(2023, 10, 30, 20, 0), "Ужин юзер", 500);
    public static final Meal userMeal3 = new Meal(MEAL3_ID, LocalDateTime.of(2023, 10, 31, 0, 0), "Еда на граничное значение юзер", 100);
    public static final Meal userMeal4 = new Meal(MEAL4_ID, LocalDateTime.of(2023, 10, 31, 10, 0), "Завтрак юзер", 1000);
    public static final Meal userMeal5 = new Meal(MEAL5_ID, LocalDateTime.of(2023, 10, 31, 13, 0), "Обед юзер", 500);
    public static final Meal userMeal6 = new Meal(MEAL6_ID, LocalDateTime.of(2023, 10, 31, 20, 0), "Ужин юзер", 410);
    public static final Meal adminMeal = new Meal(MEAL7_ID, LocalDateTime.of(2023, 10, 29, 10, 0), "Завтрак админ", 500);
    public static final Meal adminMeal1 = new Meal(MEAL8_ID, LocalDateTime.of(2023, 10, 31, 12, 0), "ужин админ", 700);

    public static Meal getUpdate() {
        Meal update = new Meal(userMeal);
        update.setDateTime(LocalDateTime.of(2023, 11, 1, 0, 0));
        update.setDescription("Update meal");
        update.setCalories(1000);
        return update;
    }

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, 11, 1, 10, 0), "обед админ", 300);
    }

    public static void asserMatch(Object actual, Object expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
