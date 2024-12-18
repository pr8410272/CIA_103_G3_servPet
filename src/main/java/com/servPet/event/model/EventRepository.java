package com.servPet.event.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventVO, Integer> {

 List<EventVO> findAll(Sort sort);
 
 @Query("SELECT e FROM EventVO e WHERE e.infId = :infId")
 public List<EventVO> findAllEventById(@Param("infId") Integer infId);
 
 // 查詢比指定時間晚的公告
 List<EventVO> findByCreatedAfter(Timestamp createdTime);

}