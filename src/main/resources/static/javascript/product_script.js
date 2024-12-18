function changeMainImage(src) {
  const mainImage = document.getElementById('main-image');
  const thumbnails = document.querySelectorAll('.product-thumbnails img');

  // 暫存目前的大圖 URL
  const currentMainImageSrc = mainImage.src;

  // 將大圖更換為被點擊的小圖
  mainImage.src = src;

  // 將原本的大圖 URL 放回到被點擊的小圖
  thumbnails.forEach((thumbnail) => {
    if (thumbnail.src === src) {
      thumbnail.src = currentMainImageSrc;
    }
  });
}

function addToFavorites() {
  alert('商品已加入收藏列表！');
  window.location.href = '/front_end/templates/pdFav.html'; // 假設此頁面為收藏列表
}

function addToCart() {
  alert('商品已加入購物車！');
}
