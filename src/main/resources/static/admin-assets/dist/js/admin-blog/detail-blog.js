let currentPage = 1;
let images = [];
let totalPages = null;

let titleEl = document.getElementById("title");
let descriptionEl = document.getElementById("description");
let contentEl = document.getElementById("content");
let statusBlogEl = document.getElementById("status");
const updateBlog = document.getElementById("update");
const deleteBlog = document.getElementById("deleteBlog");
const thumbnailPreviewEl = document.getElementById('thumbnail');

// Render blog data
const renderBlog = () => {
    // console.log(currentBlog)
    titleEl.value = currentBlog.title;
    descriptionEl.value = currentBlog.description;
    easyMDE.value(currentBlog.content);
    contentEl.value = currentBlog.content;
    thumbnailPreviewEl.setAttribute("src", currentBlog.thumbnail) ;
}

// lắng nghe sự kiện của easyMDE
easyMDE.codemirror.on('change', function () {
    contentEl.value = easyMDE.value();
});
let idBlogAdmin = currentBlog.id;

updateBlog.addEventListener('click', (e) => {
    e.preventDefault();
    if (!$("#form-blog-detail").valid()) return;
    if (contentEl.value === '') {
        alert("Vui lòng nhập nội dung");
        return;
    }
    let status = false;
    if (statusBlogEl.value === "1") {
        status = true;
    }
    const dataBlog = {
        title: titleEl.value,
        description: titleEl.value,
        content: contentEl.value,
        status: status,
        thumbnail: thumbnailPreviewEl.getAttribute('src') === "" ? null : thumbnailPreviewEl.getAttribute('src')
    }
    console.log(dataBlog);
    axios.put("/api/admin/blogs/" + idBlogAdmin, dataBlog)
        .then((res) => {
            toastr.success("Lưu thành công ");
            setTimeout(() => {
                window.location.href = "/admin/blogs/own-blogs";
            }, 1000);
        })
        .catch((err) => {
            toastr.error(err.response.data.message);
        })

})

$('#form-blog-detail').validate({
    rules: {
        title: {
            required: true,
        },
        content: {
            required: true,
        },
        description: {
            required: true
        },
    },
    messages: {
        title: {
            required: "Vui lòng nhập tiêu đề blog",

        },
        content: {
            required: "Vui lòng nhập nội dung của blog",

        },
        description: {
            required: "Vui lòng nhập mô tả cho blog"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

deleteBlog.addEventListener('click', () => {
    const isConfirm = confirm("Bạn có chắc mình muốn xóa blog này không?");
    if (!isConfirm) return;
    axios.delete("/api/admin/blogs/" + idBlogAdmin)
        .then((res) => {
            toastr.success("Xóa Thành Công ");
            setTimeout(() => {
                window.location.href = "/admin/blogs/own-blogs";
            }, 1000);
        })
        .catch((err) => {
            toastr.error(err.response.data.message);
        })

});

renderBlog();

// Gọi api lấy danh sách image
const getImages = (page) => {
    axios.get(`/api/admin/images?page=${page}`)
        .then(res => {
            images = res.data.content;
            totalPages = res.data.totalPages;
            renderImages(images);
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
        await axios.delete(`/api/admin/images/${imageId}`)
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