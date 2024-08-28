/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 28/08/2024 - 16:35
 */
package com.vou.streaming_service.repository;


import com.vou.streaming_service.model.ItemRepo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepoRepository extends JpaRepository<ItemRepo, Long> {
    ItemRepo findByIdUser(Long idUser);
    ItemRepo findByIdItem(Long id_item);

    Optional<ItemRepo> findById(Long id_item);
    ItemRepo findItembyIdUser(Long id_player, Long id_item);

}