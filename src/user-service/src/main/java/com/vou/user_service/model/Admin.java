package com.vou.user_service.model;

import com.vou.user_service.constant.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@AllArgsConstructor
@Getter
@Setter
public class Admin extends User {

}