function openCancelModal(orderId) {
    const modal = document.getElementById('cancel-modal');
    modal.style.display = 'flex';
    // console.log(`即將取消訂單編號：${orderId}`);
    console.log(`即將取消商品收藏`);
  }
  
  function closeCancelModal() {
    const modal = document.getElementById('cancel-modal');
    modal.style.display = 'none';
  }
  
  function confirmCancel() {
    alert('已取消商品收藏！');
    closeCancelModal();
  }
  