package com.vou.game_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "QuizGame")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizGame extends Game {
    @Column(name="aim_score")
    private float aimScore;
}