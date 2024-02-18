const stars = document.querySelectorAll(".star");
const ratingValue = document.getElementById("rating-value");

let currentRating = 0;

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        let rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;
        highlightStars(currentRating);
    });
});

function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}

document.getElementById('submitCreateReview').addEventListener('click', function () {
    let inputData = document.getElementById('comment').value;

    let data = {
        "rating": currentRating,
        "comment": inputData,
        "movieId": 1
    };

    fetch('/api/reviews', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())
        .then(result => {
            console.log(result);
            // Xử lý phản hồi từ server nếu cần
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('submitEditReview').addEventListener('click', function () {
    let inputData = document.getElementById('comment').value;

    let data = {
        "rating": currentRating,
        "comment": inputData,
        "movieId": 1
    };

    fetch('/api/reviews/4287', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())
        .then(result => {
            console.log(result);
            // Xử lý phản hồi từ server nếu cần
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('submitDeleteReview').addEventListener('click', function () {
    fetch('/api/reviews/4287', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.text())
        .then(result => {
            console.log(result);
            // Xử lý phản hồi từ server nếu cần
        })
        .catch(error => console.error('Error:', error));
});

//const submitReview = document.getElementById('submitCreteReview')
//const input = document.getElementById('comment');
//submitReview.addEventListener('click', async () =>{
//    // lấy giá trị từ input
//    const value = input.value;
//    const rating = ratingValue;
//    const url = window.location.href;
//    const movieId = extractMovieIdFromUrl(url);
//    const newReview = {
//        ratting :`${rating}`,
//        comment : `${value}`,
//        movieId : `${movieId}`
//    };
//    try{
//        let reponse = await fetch('/api/reviews', {
//            method: 'POST',
//            body: JSON.stringify(newReview),
//            headers:{
//              'Content-Type': 'application/json'
//            }
//          })
//        let data = await reponse.json();
//        console.log(data);
//    }catch(er){
//        console.log(er)
//    }
//});
//function extractMovieIdFromUrl(url) {
//    // Thực hiện phân tích cú pháp URL để lấy giá trị của movieId
//    // Cách thực hiện này phụ thuộc vào cách bạn tổ chức URL của mình
//    // Dưới đây là một ví dụ đơn giản, bạn có thể cải tiến nó phù hợp với cấu trúc URL của bạn
//   const match = url.match(/\/phim\/(\d+)/);
//   return match ? match[1] : null;
//   }