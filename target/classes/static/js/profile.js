document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
    bindLogoutEvent();
    bindSaveProfileEvent();
    bindProfilePictureUpload();
});

// Load current username
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

// Handle user logout
function bindLogoutEvent() {
    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            fetch('/api/auth/logout', { method: 'POST' })
                .then(() => {
                    window.location.href = '/login';
                })
                .catch(err => console.error('Logout error:', err));
        });
    }
}

// Handle profile credentials update
function bindSaveProfileEvent() {
    const saveBtn = document.getElementById('saveChangesBtn');
    if (saveBtn) {
        saveBtn.addEventListener('click', () => {
            const username = document.getElementById('updateUsername').value.trim();
            const email = document.getElementById('updateEmail').value.trim();
            const password = document.getElementById('updatePassword').value.trim();

            const payload = {};
            if (username) payload.username = username;
            if (email) payload.email = email;
            if (password) payload.password = password;

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

// Handle profile picture selection and upload
function bindProfilePictureUpload() {
    const uploadPicBtn = document.getElementById('uploadPicBtn');
    const profilePicInput = document.getElementById('profilePicInput');

    if (uploadPicBtn && profilePicInput) {
        uploadPicBtn.addEventListener('click', () => {
            profilePicInput.click();
        });

        profilePicInput.addEventListener('change', () => {
            if (!profilePicInput.files || profilePicInput.files.length === 0) {
                return;
            }

            const file = profilePicInput.files[0];
            const formData = new FormData();
            formData.append('file', file);

            fetch('/api/documents/upload-profile-pic', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (!response.ok) throw new Error('Failed to upload picture');
                    return response.text();
                })
                .then(() => {
                    alert('Profile picture updated!');
                    window.location.reload();
                })
                .catch(error => {
                    alert('Error: ' + error.message);
                });
        });
    }
}