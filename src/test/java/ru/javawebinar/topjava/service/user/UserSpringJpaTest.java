package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(value = "datajpa", resolver = ActiveDbProfileResolver.class)
public class UserSpringJpaTest extends AbstractUserTest {

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void duplicateMailCreate() {
        super.duplicateMailCreate();
    }

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public void deletedNotFound() {
        super.deletedNotFound();
    }

    @Override
    public void get() {
        super.get();
    }

    @Override
    public void getNotFound() {
        super.getNotFound();
    }

    @Override
    public void getByEmail() {
        super.getByEmail();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void getAll() {
        super.getAll();
    }

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
    }
}
