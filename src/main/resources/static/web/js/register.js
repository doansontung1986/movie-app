// hide/show password with icon in inputs using valina javascript
const togglePasswordBtns = document.querySelectorAll('.icon-toggle-password');
const passwordInputs = document.querySelectorAll('input[type="password"]');
togglePasswordBtns.forEach((btn, index) => {
    btn.addEventListener('click', () => {
        if (passwordInputs[index].type === 'password') {
            passwordInputs[index].type = 'text';
            btn.innerHTML = '<i class="fa-regular fa-eye-slash"></i>';
        } else {
            passwordInputs[index].type = 'password';
            btn.innerHTML = '<i class="fa-regular fa-eye"></i>';
        }
    });
});

// Xử lý signup
const nameEl = document.getElementById('name');
const emailEl = document.getElementById('email');
const passwordEl = document.getElementById('password');
const confirmPasswordEl = document.getElementById('confirm-password');
const formRegister = document.getElementById('form-register');
const errorMessage = document.getElementById('error-message');

formRegister.addEventListener("submit", (e) => {
    // Check validate
    e.preventDefault();

    if (!$('#form-register').valid()) {
        return;
    }

    if (passwordEl.value !== confirmPasswordEl.value) {
        errorMessage.innerText = "Mật khẩu và Nhập lại mật khẩu phải giống nhau";
    } else {
        const data = {
            name: nameEl.value, email: emailEl.value, password: passwordEl.value,
        }

        // Send data to server using axios
        axios.post('/api/auth/signup', data)
            .then((response) => {
                if (response.status === 200) {
                    toastr.success("Đăng ký thành công");
                    setTimeout(() => {
                        window.location.href = '/';
                    }, 1500);
                }
            })
            .catch((error) => {
                toastr.error("Tài khoản này đã tồn tại");
                console.log(error);
                console.log(error.response.data.message);
            });
    }
})

$('#form-register').validate({
    rules: {
        nameUser: {
            required: true,
        }, contentEmail: {
            required: true, email: true,
        }, contentPassword: {
            required: true,
        }, contentPasswordConfirm: {
            required: true, equalTo: "#contentPasswordConfirm"
        }

    }, messages: {
        nameUser: {
            required: "Vui lòng nhập tên của bạn"
        }, contentEmail: {
            required: "Vui lòng nhập email", email: "Email không đúng định dạng"
        }, contentPassword: {
            required: "Vui lòng nhập mật khẩu",
        }, contentPasswordConfirm: {
            required: "Mật khẩu không trùng khớp",
        },
    }, errorElement: 'span', errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    }, highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    }, unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});