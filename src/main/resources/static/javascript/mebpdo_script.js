// 取消訂單的燈箱
let currentPdoId = null;
let currentMebId = null;
let refundAmount = null;

// 開啟確認取消的燈箱
function openCancelModal(pdoId, mebId, amount) {
    currentPdoId = pdoId;
    currentMebId = mebId;
    refundAmount = amount;
    const modal = document.getElementById('cancel-modal');
    modal.style.display = "block";
}

// 關閉燈箱
function closeCancelModal() {
    currentPdoId = null;
    currentMebId = null;
    refundAmount = null;
    const modal = document.getElementById('cancel-modal');
    modal.style.display = "none";
}

// 確認取消訂單
function confirmCancel() {
    if (currentPdoId && currentMebId && refundAmount) {
        fetch(`/mebPdo/${currentPdoId}/cancel`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                alert('訂單已取消，金額已退回錢包');
                location.reload(); // 重新加載頁面顯示最新資料
            } else {
                alert('取消訂單失敗，請稍後再試');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('發生錯誤，請稍後再試');
        });
    }
    closeCancelModal();
}

// 修改地址的燈箱
let currentEditPdoId = null;

// 開啟修改地址的燈箱
function openEditAddressModal(pdoId, currentAddress) {
    currentEditPdoId = pdoId;
    document.getElementById('new-address').value = currentAddress; // 預填當前地址
    document.getElementById('edit-address-modal').style.display = 'block';
}

// 從按鈕的 data-* 屬性中讀取數據並打開燈箱
function openEditAddressModalFromData(button) {
    const pdoId = button.getAttribute('data-pdoid');
    const shippingAddr = button.getAttribute('data-shippingaddr');
    openEditAddressModal(pdoId, shippingAddr);
}

// 關閉燈箱
function closeEditAddressModal() {
    currentEditPdoId = null;
    document.getElementById('edit-address-modal').style.display = 'none';
}

// 確認修改地址
function confirmUpdateAddress() {
    const newAddress = document.getElementById('new-address').value;

    if (currentEditPdoId) {
        fetch(`/mebPdo/${currentEditPdoId}/updateAddress`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `newAddress=${encodeURIComponent(newAddress)}`
        })
        .then(response => {
            if (response.ok) {
                alert('配送地址更新成功');
                location.reload(); // 重新載入頁面
            } else {
                alert('更新配送地址失敗，請稍後再試');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('發生錯誤，請稍後再試');
        });
    }
    closeEditAddressModal();
}
