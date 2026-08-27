document.addEventListener('DOMContentLoaded', () => {
    const uploadBtn = document.getElementById('uploadTriggerBtn');
    const fileInput = document.getElementById('fileInput');
    const docList = document.getElementById('documentList');

    if (uploadBtn && fileInput) {
        uploadBtn.addEventListener('click', () => fileInput.click());
        fileInput.addEventListener('change', (e) => handleFiles(e.target.files));
    }

    if (docList) {
        docList.addEventListener('click', (e) => {
            const downloadLink = e.target.closest('.document-download-link');
            if (downloadLink) {
                e.preventDefault();
                downloadDocument(downloadLink.href, downloadLink.dataset.filename);
            }
        });
    }

    fetchDocuments();
});

// Upload selected files
function handleFiles(files) {
    const list = document.getElementById('documentList');
    if (!list) return;

    Array.from(files).forEach(file => {
        const item = document.createElement('div');
        item.className = 'list-group-item';
        item.textContent = `${file.name} (${Math.round(file.size / 1024)} KB) - Uploading...`;
        list.appendChild(item);

        const formData = new FormData();
        formData.append('file', file);

        fetch('/api/documents/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    item.textContent = `${file.name} (${Math.round(file.size / 1024)} KB) - Uploaded`;
                    fetchDocuments();
                } else {
                    item.textContent = `${file.name} (${Math.round(file.size / 1024)} KB) - Failed`;
                }
            })
            .catch(() => {
                item.textContent = `${file.name} (${Math.round(file.size / 1024)} KB) - Error`;
            });
    });
}

// Fetch user files
function fetchDocuments() {
    fetch('/api/documents/my-files')
        .then(res => res.json())
        .then(files => {
            const list = document.getElementById('documentList');
            if (!list) return;
            list.innerHTML = '';

            files.forEach(file => {
                const item = document.createElement('div');
                item.className = 'list-group-item';

                const link = document.createElement('a');
                link.href = `/api/documents/download/${file.id}`;
                link.textContent = file.filename;
                link.className = 'document-download-link text-decoration-none';
                link.dataset.filename = file.filename;

                item.appendChild(link);
                list.appendChild(item);
            });
        })
        .catch(err => console.error('Failed to load documents:', err));
}

// Download file blob
function downloadDocument(url, filename) {
    fetch(url)
        .then(resp => resp.blob())
        .then(blob => {
            const blobUrl = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = blobUrl;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(blobUrl);
        })
        .catch(err => console.error('Download failed:', err));
}