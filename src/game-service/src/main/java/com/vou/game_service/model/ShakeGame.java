package com.vou.game_service.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ShakeGame")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShakeGame extends Game {

}