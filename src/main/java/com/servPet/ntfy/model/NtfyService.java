package com.servPet.ntfy.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servPet.meb.model.MebVO;
import com.servPet.event.model.EventVO;
import com.servPet.ntfy.model.NtfyVO;
import com.servPet.event.model.EventRepository;
import com.servPet.ntfy.model.NtfyRepository;

@Service
public class NtfyService {

    @Autowired
    private NtfyRepository ntfyRepository;
    
    @Autowired
    private EventRepository eventRepository;
    

    // 獲取公告的所有資料
    public List<EventVO> getEventAll(Integer infId) {
        return eventRepository.findAllEventById(infId);
    }
    
    // 獲取會員的所有通知
    public List<NtfyVO> getAllNotificationsForMember(Integer mebId) {
    	return ntfyRepository.findByMebVO_MebIdOrderByNtfyMgmtIdDesc(mebId);
    }

    public List<NtfyVO> getUnreadNotificationsForMember(Integer mebId) {
        return ntfyRepository.findByMebVO_MebIdAndStatusOrderByNtfyMgmtIdDesc(mebId, 0);
    }

    // 標記通知為已讀
    @Transactional
    public void markNotificationAsRead(Integer ntfyMgmtId) {
        NtfyVO notification = ntfyRepository.findById(ntfyMgmtId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus(1); // 設置為已讀
        ntfyRepository.save(notification);
    }
    
    		//		=========================== 後台操作 =========================== //
    
    
    
    public List<NtfyVO> getAllNotifications() {
        return ntfyRepository.findAllByOrderByNtfyMgmtIdDesc();
    }

    
    @Transactional
    public NtfyVO createNotification(NtfyVO ntfy) {
        System.out.println("Creating notification: " + ntfy);
        NtfyVO savedNtfy = ntfyRepository.save(ntfy);
        System.out.println("Saved notification: " + savedNtfy);
        return savedNtfy;
    }
    
    
    @Transactional
    public NtfyVO updateNotification(NtfyVO ntfy) {
        NtfyVO existingNtfy = ntfyRepository.findById(ntfy.getNtfyMgmtId())
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        existingNtfy.setTitle(ntfy.getTitle());
        existingNtfy.setContent(ntfy.getContent());
        existingNtfy.setStatus(ntfy.getStatus());
        
        return ntfyRepository.save(existingNtfy);
    }

    
    @Transactional
        public void deleteNotification(Integer ntfyMgmtId) {
    			ntfyRepository.deleteByNtfyId(ntfyMgmtId);
        }
    
    // 獲取單個通知
    public NtfyVO getNotificationById(Integer ntfyMgmtId) {
        return ntfyRepository.findById(ntfyMgmtId).orElse(null);
    }
    
    public void addNotificationForEvent(EventVO eventVO) {
        // 創建通知對象
        NtfyVO ntfyVO = new NtfyVO();
        ntfyVO.setContent(eventVO.getTitle()); // 使用活動標題作為通知內容
        ntfyVO.setDate(new Timestamp(System.currentTimeMillis())); // 當前時間
        ntfyVO.setStatus(0); // 設置為未讀
        ntfyVO.setTitle("event"); // 標記為活動類型

        ntfyVO.setMebVO(new MebVO());
        
        ntfyRepository.save(ntfyVO);
    }
    @Transactional
	public void updateAllUnreadToRead(Integer mebId) {
	     ntfyRepository.updateAllUnreadToRead(mebId);
		
	}

}

