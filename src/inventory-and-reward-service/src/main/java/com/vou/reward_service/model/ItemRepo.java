package com.vou.reward_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "itemrepo")
public class ItemRepo {
    @Id
    @Column(name = "id_itemrepo")
    Long idItemRepo;

    @Column(name = "id_player")
    Long idPlayer;

    @Column(name = "id_item")
    Long idItem;

    @Column(name = "amount")
    Long amount;
}
