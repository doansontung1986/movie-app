const titleGenreEl = document.getElementById("title");
const saveGenre = document.getElementById("save");

// tạo genre
saveGenre.addEventListener('click', (e) => {
    e.preventDefault();
    if (!$("#form-genre").valid()) return;
    if (titleGenreEl.value === '') {
        alert("Vui lòng nhập nội dung");
        return;
    }

    const dataGenre = {
        name: titleGenreEl.value
    }
    axios.post("/api/admin/genre", dataGenre)
        .then((res) => {
            console.log(res);
            toastr.success("Tạo thể loại thành công ");
            setTimeout(() => {
                window.location.href = "/admin/genre/";
            }, 1000);
        })
        .catch((err) => {
            toastr.error(err.response.data.message);
        })

})

$('#form-genre').validate({
    rules: {
        title: {
            required: true,
        }
    }, messages: {
        title: {
            required: "Vui lòng nhập tiêu đề thể loại",
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



