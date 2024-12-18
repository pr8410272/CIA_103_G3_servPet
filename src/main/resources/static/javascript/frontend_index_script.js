// 初始化 Slick 輪播功能
$(document).ready(function(){
    $('.carousel-slide').slick({
        dots: true, // 顯示指示點
        centerMode: false, // 停用中心模式
        infinite: true, // 無限循環
        speed: 500, // 切換速度
        slidesToShow: 1, // 每次顯示一張圖片
        slideToScroll: 1,
        autoplay: true, // 自動播放
        autoplaySpeed: 3000, // 每三秒切換一次
        arrows: true // 顯示左右箭頭
        
    });
});


// $("div.myclass").slick({
//     arrows: true,
//     dots: true,
//     centerMode: true,
//     centerPadding: "60px",
//     slidesToShow: 1,
//     responsive: [
//       {
//         breakpoint: 768,  // <= 768px
//         settings: {
//           arrows: false,
//           centerPadding: "0px",
//           slidesToShow: 1
//         }
//     }
//   ]
// });


