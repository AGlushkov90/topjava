package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final UserMapper userMapper;

    private final Validator validator;
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);


    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, UserMapper userMapper, ValidatorFactory validatorFactory) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.userMapper = userMapper;
        this.validator = validatorFactory.getValidator();
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("insert into user_role (user_id, role) values (?, ?)", getRolesList(user));
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password,
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
            jdbcTemplate.batchUpdate("insert into user_role (user_id, role) values (?, ?)", getRolesList(user));
        }
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(user);
        for (ConstraintViolation<Object> cv : constraintViolations) {
            log.error("Error! property: {}, value: {}, message: {}",
                    cv.getPropertyPath(), cv.getInvalidValue(), cv.getMessage());
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role ur on u.id = ur.user_id WHERE id=?",
                userMapper::getMapUser, id);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role ur on u.id = ur.user_id WHERE email=?",
                userMapper::getMapUser, email);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_role ur on u.id = ur.user_id ORDER BY name, email",
                rs -> {
                    Map<Integer, User> userMap = new HashMap<>();
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        User user = userMap.getOrDefault(id, userMapper.mapRow(rs, 0));
                        if (userMap.containsKey(id)) {
                            user.getRoles().add(Role.valueOf(rs.getString(9)));
                        }
                        userMap.put(rs.getInt(1), user);
                    }
                    return userMap.values().stream()
                            .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                            .collect(Collectors.toList());
                });
    }

    public List<Role> getAllRoles(int id) {
        return jdbcTemplate.query("SELECT * FROM user_role WHERE user_id=?", rs -> {
            List<Role> list = new ArrayList<>();

            while (rs.next()) {
                list.add(Role.valueOf(rs.getString("role")));

            }
            return list;
        }, id);
    }

    private List<Object[]> getRolesList(User user) {
        List<Object[]> list = new ArrayList<>();
        for (Role element : user.getRoles()) {
            Object[] ob = {user.getId(), element.name()};
            list.add(ob);
        }
        return list;
    }

}
