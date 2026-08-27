document.addEventListener('DOMContentLoaded', () => {
    fetchAllDocuments();
    fetchUsers();

    // Event delegation pentru butoanele dinamice de acțiune (download / delete)
    const tbody = document.getElementById('documentTableBody');
    if (tbody) {
        tbody.addEventListener('click', (e) => {
            const downloadBtn = e.target.closest('.btn-download');
            const deleteBtn = e.target.closest('.btn-delete');

            if (downloadBtn) {
                const docId = downloadBtn.dataset.docId;
                downloadDocument(docId);
            } else if (deleteBtn) {
                const docId = deleteBtn.dataset.docId;
                deleteDocument(docId);
            }
        });
    }
});

// Preluare documente
function fetchAllDocuments() {
    fetch('/api/admin/documents/all')
        .then(res => res.json())
        .then(documents => {
            const tbody = document.getElementById('documentTableBody');
            if (!tbody) return;
            tbody.innerHTML = '';

            documents.forEach(doc => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${doc.filename}</td>
                    <td>${doc.username}</td>
                    <td>${doc.type}</td>
                    <td>${Math.round(doc.size / 1024)} KB</td>
                    <td>${new Date(doc.uploadDate).toLocaleDateString()}</td>
                    <td>
                        <div class="action-buttons">
                            <button type="button" class="btn btn-sm btn-primary btn-download" data-doc-id="${doc.id}">
                                <i class="bi bi-download"></i>
                            </button>
                            <button type="button" class="btn btn-sm btn-danger btn-delete" data-doc-id="${doc.id}">
                                <i class="bi bi-trash"></i>
                            </button>
                        </div>
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => console.error('Error fetching documents:', err));
}

// Descărcare document
function downloadDocument(docId) {
    fetch(`/api/admin/documents/download/${docId}`)
        .then(res => res.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'document';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(err => console.error('Error downloading document:', err));
}

// Ștergere document
function deleteDocument(docId) {
    if (confirm('Are you sure you want to delete this document?')) {
        fetch(`/api/admin/documents/delete/${docId}`, {
            method: 'DELETE'
        })
            .then(res => {
                if (res.ok) {
                    fetchAllDocuments();
                } else {
                    alert('Failed to delete document.');
                }
            })
            .catch(err => console.error('Error deleting document:', err));
    }
}

// Preluare utilizatori pentru filtru
function fetchUsers() {
    fetch('/api/admin/users')
        .then(res => res.json())
        .then(users => {
            const select = document.getElementById('userSelect');
            if (!select) return;

            // Păstrăm prima opțiune de tip placeholder ("All Users")
            select.innerHTML = '<option value="">All Users</option>';

            users.forEach(user => {
                const option = document.createElement('option');
                option.value = user.id;
                option.textContent = user.username;
                select.appendChild(option);
            });
        })
        .catch(err => console.error('Error fetching users:', err));
}