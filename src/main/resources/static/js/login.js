document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const passwordError = document.getElementById('passwordError');

    if (loginForm) {
        loginForm.addEventListener('submit', (event) => {
            event.preventDefault();

            // Reset error messages
            document.querySelectorAll('.error-message').forEach(el => {
                el.style.display = 'none';
            });

            const usernameInput = document.getElementById('username');
            const passwordInput = document.getElementById('password');
            const actionUrl = loginForm.getAttribute('action') || '/api/auth/login';

            // Send login request
            fetch(actionUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({
                    username: usernameInput.value,
                    password: passwordInput.value
                })
            })
                .then(response => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    } else if (response.ok) {
                        window.location.href = '/dashboard';
                    } else {
                        throw new Error('Invalid username or password');
                    }
                })
                .catch(() => {
                    if (passwordError) {
                        passwordError.textContent = 'Invalid username or password';
                        passwordError.style.display = 'block';
                    }
                });
        });
    }
});