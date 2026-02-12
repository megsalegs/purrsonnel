package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class User {

   private int id;
   private String username;

   @JsonIgnore
   private String hashedPassword;

   private String role;

   public User() { }

   public User(int id, String username, String hashedPassword, String role) {
      this.id = id;
      this.username = username;
      this.hashedPassword = hashedPassword;
      setRole(role);
   }

   public User(String username, String hashedPassword, String role) {
      this(0, username, hashedPassword, role);
   }


   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   @JsonIgnore
   public String getHashedPassword() {
      return hashedPassword;
   }

   public void setHashedPassword(String hashedPassword) {
      this.hashedPassword = hashedPassword;
   }

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role != null && !role.startsWith("ROLE_")
              ? "ROLE_" + role.toUpperCase()
              : role;
   }


   @JsonIgnore
   public String getPassword() {
      return hashedPassword;
   }


   @JsonIgnore
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority(role));
   }


   public boolean isActivated() {
      return true;
   }


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof User user)) return false;
      return id == user.id &&
              Objects.equals(username, user.username) &&
              Objects.equals(role, user.role);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, role);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", role='" + role + '\'' +
              '}';
   }
}
