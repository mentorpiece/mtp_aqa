package com.mtp.aqa.self.api.misc.model;

import java.util.Objects;

/**
 * A data-transfer-object for the User resource.
 * DTO - <a href="https://stackoverflow.com/questions/1051182/what-is-a-data-transfer-object-dto">...</a>
 * JSON would look like:
 * <pre>
 * Created user object
 *
 * {
 *   "id": 0,
 *   "username": "string",
 *   "firstName": "string",
 *   "lastName": "string",
 *   "email": "string",
 *   "password": "string",
 *   "phone": "string",
 *   "userStatus": 0
 * </pre>
 */
public class UserDto {
    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private int userStatus;

    public UserDto() {
    }

    public UserDto(long id, String username, String firstName, String lastName, String email, String password, String phone, int userStatus) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userStatus = userStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && userStatus == userDto.userStatus && Objects.equals(username, userDto.username) && Objects.equals(firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(email, userDto.email) && Objects.equals(password, userDto.password) && Objects.equals(phone, userDto.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstName, lastName, email, password, phone, userStatus);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }
}
