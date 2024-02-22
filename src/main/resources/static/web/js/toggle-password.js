const togglePassword = document.getElementById('togglePassword');
const toggleRepeatPassword = document.getElementById('toggleRepeatPassword');
const passwordInput = document.getElementById('password');
const repeatPasswordInput = document.getElementById('repeat-password');

togglePassword.addEventListener('click', function() {
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);
    // Toggle eye icon between open and closed
    togglePassword.innerHTML = type === 'password' ? '&#128065;' : '&#128064;';
});

toggleRepeatPassword.addEventListener('click', function() {
    const type = repeatPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    repeatPasswordInput.setAttribute('type', type);
    // Toggle eye icon between open and closed
    toggleRepeatPassword.innerHTML = type === 'password' ? '&#128065;' : '&#128064;';
});