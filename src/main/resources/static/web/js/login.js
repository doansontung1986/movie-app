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

// Xử lý login
const emailEl = document.getElementById('email');
const passwordEl = document.getElementById('password');
const formLogin = document.getElementById('form-login');

formLogin.addEventListener("submit", (e) => {
    // Check validate
    e.preventDefault();

    const data = {
        email: emailEl.value, password: passwordEl.value
    }

    // Send data to server using axios
    axios.post('/api/auth/login', data)
        .then((response) => {
            if (response.status === 200) {
                toastr.success("Đăng nhập thành công");
                setTimeout(() => {
                    window.location.href = '/';
                }, 1500);
            }
        })
        .catch((error) => {
            toastr.error("Tài khoản hoặc mật khẩu không đúng");
            console.log(error);
            console.log(error.response.data.message);
        });
})

$('#form-login').validate({
    rules: {
        contentEmail: {
            required: true, email: true,
        }, contentPassword: {
            required: true,
        },
    }, messages: {
        contentEmail: {
            required: "Vui lòng nhập email", email: "Email không đúng định dạng"
        }, contentPassword: {
            required: "Vui lòng nhập mật khẩu",
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