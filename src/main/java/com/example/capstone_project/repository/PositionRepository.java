package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> , CustomPositionRepository{
    List<Position> findAll();

    boolean existsById(Long id);

    @Query(value = "select distinct count(position.id) from Position position " +
            "where position.name like %:query% and (position.isDelete = false or position.isDelete is null)")
    long countDistinct(String query);
}
