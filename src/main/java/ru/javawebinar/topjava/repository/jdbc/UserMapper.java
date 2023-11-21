package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Objects;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt(1), rs.getString(2),
                rs.getString(3), rs.getString(4), rs.getInt(7),
                rs.getBoolean(6), rs.getDate(5),
                rs.getString(9) == null ? Collections.emptySet() :
                        Collections.singleton(Role.valueOf(rs.getString(9))));
    }

    public User getMapUser(ResultSet rs) throws SQLException {
        User user = null;
        while (rs.next()) {
            if (user == null) {
                user = mapRow(rs, 0);
            }
            String role = rs.getString(9);
            if (role != null) {
                Objects.requireNonNull(user).getRoles().add(Role.valueOf(role));
            }
        }
        return user;
    }
}
