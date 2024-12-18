package com.servPet.event.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    
    public EventVO addEvent(EventVO eventVO) {
        // 為 created 賦值
        eventVO.setCreated(new Timestamp(System.currentTimeMillis()));
        return eventRepository.save(eventVO);
        
//        EventVO savedEvent = eventRepository.save(eventVO);
//        ntfyService.addNotificationForEvent(savedEvent);
//
//               return savedEvent;
    }



    
    public EventVO updateEvent(Integer infId, String title, String content, Integer infType) {
        // 查找現有的事件
        EventVO eventVO = eventRepository.findById(infId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + infId + " not found"));

        // 更新事件的屬性
        eventVO.setTitle(title);
        eventVO.setContent(content);
        eventVO.setInfType(infType);
        eventVO.setUpdated(new Timestamp(System.currentTimeMillis())); // 設置當前時間為修改時間

        // 保存更新後的事件
        return eventRepository.save(eventVO);
    }

    
    public EventVO getEventById(Integer infId) {
        return eventRepository.findById(infId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + infId + " not found"));
    }

  
    public List<EventVO> getAllEvents() {
        return eventRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
    }

   
    public Page<EventVO> getPaginatedEvents(int page, int size) {
        return eventRepository.findAll(PageRequest.of(page, size));
    }

  
    public void unpublishEvent(Integer infId) {
        EventVO eventVO = eventRepository.findById(infId)
                .orElseThrow(() -> new RuntimeException("Event with ID " + infId + " not found"));
        eventVO.setInfType(99); // 设置为下架状态
        eventRepository.save(eventVO);
    }
}
