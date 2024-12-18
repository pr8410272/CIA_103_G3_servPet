package com.servPet.psSvcItem.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsSvcItemRepository extends JpaRepository<PsSvcItemVO, Integer> {
    //提供基本幾項資料，剩下給使用者自己增加。
    //增加功能JPA已內建
    //list all功能已內件
}
