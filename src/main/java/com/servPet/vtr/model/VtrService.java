package com.servPet.vtr.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VtrService {

    @Autowired
    private VtrRepository vtrRepository;

    
    
    public VtrVO addRecord(VtrVO record) {
        return vtrRepository.save(record);
    }
    
    
    public VtrVO createTransaction(Integer mebId, Integer money, String transactionType) {
        VtrVO vtr = new VtrVO();
        vtr.setMebId(mebId);
        vtr.setMoney(money);
        vtr.setTransactionType(transactionType);
        vtr.setCreateTime(LocalDateTime.now());
        vtr.setUpdateTime(LocalDateTime.now());
        return vtrRepository.save(vtr);
    }

    public List<VtrVO> getTransactionsByMemberId(Integer mebId) {
        return vtrRepository.findByMebId(mebId);
    }

    public Page<VtrVO> getTransactionsByMemberIdWithPagination(Integer mebId, Pageable pageable) {
        return vtrRepository.findByMebId(mebId, pageable);
    }
    
    public VtrVO updateTransaction(Integer vtrId, Integer money, String transactionType) {
        VtrVO vtr = vtrRepository.findById(vtrId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        vtr.setMoney(money);
        vtr.setTransactionType(transactionType);
        vtr.setUpdateTime(LocalDateTime.now());
        return vtrRepository.save(vtr);
    }

    public void deleteTransaction(Integer vtrId) {
        vtrRepository.deleteById(vtrId);
    }
    
    
    public Integer calculateTotalBalance(Integer mebId) {
        List<VtrVO> transactions = vtrRepository.findByMebId(mebId);
        // 根據交易類型累加金額，扣款要以負數處理
        return transactions.stream()
                .mapToInt(transaction -> {
                    if ("扣款".equals(transaction.getTransactionType())) {
                        return -transaction.getMoney(); // 扣款時金額變為負數
                    }
                    return transaction.getMoney(); // 儲值時為正數
                })
                .sum();
    }

//============================後台========================================================================    
    
    
    
}
	