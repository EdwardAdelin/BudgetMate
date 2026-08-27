document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
});

// Handle user registration
function handleRegister(event) {
    event.preventDefault();

    // Reset error display
    document.querySelectorAll('.error-message').forEach(el => {
        el.style.display = 'none';
    });

    const username = document.getElementById('username').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    const confirmPasswordError = document.getElementById('confirmPasswordError');
    const usernameError = document.getElementById('usernameError');
    const emailError = document.getElementById('emailError');

    // Password validation
    if (password !== confirmPassword) {
        if (confirmPasswordError) {
            confirmPasswordError.textContent = 'Passwords do not match';
            confirmPasswordError.style.display = 'block';
        }
        return;
    }

    const userData = { username, email, password };

    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Registration failed');
                });
            }
            return response.json();
        })
        .then(() => {
            alert('Registration successful! Please login.');
            window.location.href = '/login';
        })
        .catch(error => {
            if (error.message.includes('Username already exists') && usernameError) {
                usernameError.textContent = 'Username already exists';
                usernameError.style.display = 'block';
            } else if (error.message.includes('Email already exists') && emailError) {
                emailError.textContent = 'Email already exists';
                emailError.style.display = 'block';
            } else {
                alert('Registration failed: ' + error.message);
            }
        });
}