document.addEventListener('DOMContentLoaded', () => {
    fetchAdminOverview();
});

// Load and display category totals across all users
function fetchAdminOverview() {
    const row = document.getElementById('categoryTotalsRow');
    if (!row) return;

    fetch('/api/categories/admin/overview')
        .then(res => res.json())
        .then(data => {
            row.innerHTML = '<h1>Category Totals (All Users)</h1>';
            data.forEach(c => {
                const col = document.createElement('div');
                col.className = 'col-md-3 mb-3';
                col.innerHTML = `
                    <div class="summary-card">
                        <div class="summary-title">${c.name}</div>
                        <div class="summary-value">Total Spent: ${Number(c.totalSpent).toFixed(2)}</div>
                    </div>
                `;
                row.appendChild(col);
            });
        })
        .catch(err => console.error('Failed to load admin overview:', err));
}