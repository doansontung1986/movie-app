let currentPage = 1;
let videos = [];
let totalPages = null;

const titleEl = document.getElementById('title');
const descriptionEl = document.getElementById('description');
const relishedYearEl = document.getElementById("releaseYear");
const statusEl = document.getElementById("status")
const btnSave = document.getElementById("btn-create");
const thumbnailPreviewEl = document.getElementById('thumbnail');
const movieTypeEl = document.getElementById("type");

btnSave.addEventListener('click', (e) => {
    e.preventDefault();
    if (!$('#form-create-movie').valid()) return;
    let status = false;
    if (statusEl.getAttribute("value") === "true") {
        status = true;
    }
    let directorValue = $('#director').val();
    let actorValue = $('#actor').val();
    let genreValue = $('#genre').val();
    let directorList = directorValue.map(dir => parseInt(dir));
    let actorList = actorValue.map(dir => parseInt(dir));
    let genreList = genreValue.map(dir => parseInt(dir));
    const data = {
        title: titleEl.value,
        description: descriptionEl.value,
        type: movieTypeEl.value,
        releaseYear: relishedYearEl.value,
        status: status,
        directorIds: directorList,
        actorIds: actorList,
        genreIds: genreList,
        poster: thumbnailPreviewEl.getAttribute("src")
    }
    console.log(data);
    axios.post("/api/admin/videos", data)
        .then((res) => {
            toastr.success("Lưu thành công ");
            setTimeout(() => {
                window.location.href = "/admin/videos";
            }, 1500);
        })
        .catch(() => {
            toastr.error("Lưu thất bại ");
        })
})
$('#form-create-movie').validate({
    rules: {
        title: {
            required: true,
        }, director: {
            required: true,
        }, description: {
            required: true
        }, actor: {
            required: true
        }, genre: {
            required: true
        }, relishedAt: {
            required: true
        }, thumbnail: {
            required: true
        }
    }, messages: {
        title: {
            required: "Vui lòng nhập tiêu đề phim",

        }, director: {
            required: "Vui lòng nhập đạo diễn bộ phim",

        }, description: {
            required: "Vui lòng nhập mô tả cho phim"
        }, actor: {
            required: "Vui lòng nhập diễn viên"
        }, genre: {
            required: "Vui lòng nhập thể loại "
        }, relishedAt: {
            required: "Vui lòng nhập năm sản xuất"
        }, thumbnail: {
            required: "Vui lòng chọn hình ảnh"
        }
    }, errorElement: 'span', errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    }, highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    }, unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

// Gọi api lấy danh sách image
const getImages = (page) => {
    axios.get(`/api/admin/videos?page=${page}`)
        .then(res => {
            videos = res.data.content;
            totalPages = res.data.totalPages;
            renderImages(videos);
            renderPagination(totalPages);
        })
        .catch(err => {
            console.log(err);
            toastr.error(err.response.data.message);
        })
}

// Render danh sách image
const imageContainerEl = document.querySelector('.image-container');
const btnChoseImage = document.getElementById('btn-chose-image');
const btnDeleteImage = document.getElementById('btn-delete-image');
const inputImageEl = document.getElementById('avatar');

// Hiển thị danh sách ảnh
const renderImages = images => {
    let html = "";
    images.forEach(image => {
        html += `
            <div class="image-item" onclick="choseImage(this)" data-id="${image.id}">
                <img src="${image.url}" alt="">
            </div>        
        `
    });
    imageContainerEl.innerHTML = html;
}

// Render pagination
const renderPagination = totalPages => {
    let html = "";
    for (let i = 1; i <= totalPages; i++) {
        html += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
              <a class="page-link" onclick="chosePage(${i})">${i}</a>
            </li>        
        `
    }

    document.querySelector(".pagination-container").innerHTML =
        `
        ${totalPages > 1 ?
            `
                <nav aria-label="...">
                  <ul class="pagination">
                    <li class="page-item ${currentPage === 1 ? 'disabled' : ''}">
                      <a class="page-link" onclick="prevPage()">Previous</a>
                    </li>
                    ${html}
                    <li class="page-item ${currentPage === totalPages ? 'disabled' : ''}">
                      <a class="page-link" onclick="nextPage()">Next</a>
                    </li>
                  </ul>
                </nav>
            ` : ""}
        `;
}

const chosePage = page => {
    currentPage = page;
    getImages(currentPage);
}

const prevPage = () => {
    if (currentPage > 1) {
        currentPage--;
        getImages(currentPage);
    }
}

const nextPage = () => {
    if (currentPage < totalPages) {
        currentPage++;
        getImages(currentPage);
    }
}

const  choseImage = el => {
    const selectedEl = document.querySelector(".image-item.selected");
    if (selectedEl) {
        selectedEl.classList.remove("selected");
    }

    el.classList.add("selected");
    btnChoseImage.disabled = false;
    btnDeleteImage.disabled = false;
}

// Chọn ảnh
btnChoseImage.addEventListener("click", () => {
    const selectedEl = document.querySelector(".image-item.selected");
    if (!selectedEl) {
        toastr.warning("Vui lòng chọn ảnh");
        return;
    }

    const imageUrl = selectedEl.querySelector("img").getAttribute("src");
    thumbnailPreviewEl.src = imageUrl;

    $('#modal-image').modal('hide');
})

// Upload ảnh
inputImageEl.addEventListener("change", async (e) => {
    // Lấy ra thông tin của file upload
    const file = e.target.files[0];

    // Tạo đối tượng formData
    const formData = new FormData();
    formData.append("file", file);

    // Call api sử dụng axios
    try {
        await axios.post("/api/admin/images", formData);
        toastr.success("Upload ảnh thành công");

        // Hiển thị ảnh đã upload
        currentPage = 1
        getImages(currentPage);
    } catch (err) {
        console.log(err);
        toastr.error(err.response.data.message);
    }
})

// Xóa ảnh
btnDeleteImage.addEventListener("click", async () => {
    const selectedEl = document.querySelector(".image-item.selected");
    if (!selectedEl) {
        toastr.warning("Vui lòng chọn ảnh cần xóa");
        return;
    }

    const imageId = selectedEl.dataset.id;

    try {
        await axios.delete(`/api/admin/videos/${imageId}`)
        btnChoseImage.disabled = true;
        btnDeleteImage.disabled = true;
        toastr.success("Xoá ảnh thành công");

        // Hiển thị ảnh đã upload
        currentPage = 1
        getImages(currentPage);
    } catch (err) {
        console.log(err);
        toastr.error(err.response.data.message);
    }
})

getImages(currentPage);


































