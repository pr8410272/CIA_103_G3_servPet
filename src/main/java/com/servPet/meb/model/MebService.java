package com.servPet.meb.model;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;//新增的import(學雍)

@Service
public class MebService {
    private final MebRepository mebRepository;
    private final PasswordEncoder passwordEncoder;

    public MebService(MebRepository mebRepository, PasswordEncoder passwordEncoder) {
        this.mebRepository = mebRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    // 查詢所有(學雍.會員停權)
    public List<MebVO> getAll() {
        return mebRepository.findAll();
    }
    
    // 根據會員編號查詢檢舉 (Get complaints by PS_ID)(學雍.美容師保母檢舉)
    public MebVO getByMebId(Integer mebId) {
        return mebRepository.findMemberById(mebId);  // 根據保母編號查詢檢舉
    }

    // 註冊會員
    public MebVO registerMember(MebVO member) {
        if (mebRepository.findByMebMail(member.getMebMail()).isPresent()) {
            throw new IllegalArgumentException("電子郵件已被註冊");
        }
        member.setMebPwd(passwordEncoder.encode(member.getMebPwd()));
        return mebRepository.save(member);
    }

    // 根據電子郵件查找會員
    public Optional<MebVO> findMemberByEmail(String email) {
        return mebRepository.findByMebMail(email);
    }
    // 更新會員資料
    public MebVO updateMember(MebVO updatedMember) {
        // 查詢現有會員資料
        MebVO existingMember = mebRepository.findByMebMail(updatedMember.getMebMail())
                .orElseThrow(() -> new IllegalArgumentException("會員不存在，無法更新"));

        // 合併更新的欄位
        if (updatedMember.getMebName() != null) {
            existingMember.setMebName(updatedMember.getMebName());
        }
        if (updatedMember.getMebAddress() != null) {
            existingMember.setMebAddress(updatedMember.getMebAddress());
        }
        if (updatedMember.getMebPhone() != null) {
            existingMember.setMebPhone(updatedMember.getMebPhone());
        }
        if (updatedMember.getMebImg() != null) {
            existingMember.setMebImg(updatedMember.getMebImg());
        }

        // 處理密碼更新（需要加密）
        if (updatedMember.getMebPwd() != null 
                && !passwordEncoder.matches(updatedMember.getMebPwd(), existingMember.getMebPwd())) {
            existingMember.setMebPwd(passwordEncoder.encode(updatedMember.getMebPwd()));
        }

        // 保存更新後的會員資料
        return mebRepository.save(existingMember);
    }
    public MebVO getMemberById(Integer mebId) {
        return mebRepository.findMemberById(mebId);
    }

    public void deductBalance(Integer mebId, Double amount) {
     mebRepository.deductBalance(mebId, amount);
    }
    
    

    public Integer getMebIdByEmail(String mebMail) {
        return mebRepository.findMebIdByMebMail(mebMail);
    }
    
    
    public void updatePassword(Integer userId, String newPassword) {
        Optional<MebVO> member = mebRepository.findById(userId);
        if (member.isPresent()) {
            MebVO meb = member.get();
            meb.setMebPwd(newPassword); // 根據資料庫結構更新密碼
            mebRepository.save(meb);
        } else {
            throw new RuntimeException("會員不存在！");
        }
    } 

}
