document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
    bindLogoutEvent();
    bindSaveProfileEvent();
});

// Fetch authenticated user info
function loadUserProfile() {
    const profileUsername = document.getElementById('profileUsername');

    fetch('/api/auth/me')
        .then(response => {
            if (!response.ok) throw new Error('Not authenticated');
            return response.text();
        })
        .then(username => {
            if (profileUsername) profileUsername.textContent = username;
        })
        .catch(() => {
            if (profileUsername) profileUsername.textContent = 'Guest';
        });
}

// Handle logout
function bindLogoutEvent() {
    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            fetch('/api/auth/logout', { method: 'POST' })
                .then(() => {
                    window.location.href = '/login';
                })
                .catch(err => console.error('Logout failed:', err));
        });
    }
}

// Handle profile update
function bindSaveProfileEvent() {
    const saveBtn = document.querySelector('.save-btn');
    if (saveBtn) {
        saveBtn.addEventListener('click', () => {
            const nameInput = document.querySelector('.update-form input[type="text"]');
            const emailInput = document.querySelector('.update-form input[type="email"]');
            const passwordInput = document.querySelector('.update-form input[type="password"]');

            const payload = {};
            if (nameInput && nameInput.value.trim()) payload.name = nameInput.value.trim();
            if (emailInput && emailInput.value.trim()) payload.email = emailInput.value.trim();
            if (passwordInput && passwordInput.value.trim()) payload.password = passwordInput.value.trim();

            if (Object.keys(payload).length === 0) {
                alert('Please fill at least one field to update.');
                return;
            }

            fetch('/api/auth/update', {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            })
                .then(response => {
                    if (!response.ok) throw new Error('Update failed');
                    alert('Profile updated successfully!');
                })
                .catch(error => {
                    alert('Error updating profile: ' + error.message);
                });
        });
    }
}