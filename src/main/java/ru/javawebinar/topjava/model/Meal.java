package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id AND m.user.id = :user_id"),
        @NamedQuery(name = Meal.GET, query = "SELECT m FROM Meal m WHERE m.id=:id AND m.user.id = :user_id"),
        @NamedQuery(name = Meal.GET_ALL, query = "SELECT m FROM Meal m WHERE m.user.id = :user_id ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal m SET m.description = ?1, m.calories = ?2, m.dateTime = ?3 " +
                "WHERE m.id = ?4 AND m.user.id = ?5"),
        @NamedQuery(name = Meal.GET_BETWEEN_HALF_OPEN,
                query = "SELECT m FROM Meal m WHERE m.user.id = ?1 AND m.dateTime >= ?2 AND m.dateTime < ?3" +
                        " ORDER BY m.dateTime DESC")
})

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "meal_unique_user_datetime_idx", columnNames = {"user_id", "date_time"}))
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "meal.delete";
    public static final String GET = "meal.get";
    public static final String GET_ALL = "meal.getAll";
    public static final String UPDATE = "meal.update";
    public static final String GET_BETWEEN_HALF_OPEN = "meal.getBetweenHalfOpen";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @Column(nullable = false)
    @NotNull
    private int calories;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
