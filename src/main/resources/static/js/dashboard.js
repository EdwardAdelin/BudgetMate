document.addEventListener('DOMContentLoaded', () => {
    initDateSelectors();
    bindFetchStatsEvent();
});

// Populate available years and set current month
function initDateSelectors() {
    const yearSelect = document.getElementById('yearSelect');
    const monthSelect = document.getElementById('monthSelect');
    const now = new Date();

    if (monthSelect) {
        monthSelect.value = (now.getMonth() + 1).toString();
    }

    if (yearSelect) {
        yearSelect.innerHTML = '';
        const currentYear = now.getFullYear();
        for (let y = currentYear; y >= currentYear - 5; y--) {
            const opt = document.createElement('option');
            opt.value = y;
            opt.textContent = y;
            yearSelect.appendChild(opt);
        }
    }
}

// Bind stats fetching
function bindFetchStatsEvent() {
    const fetchBtn = document.getElementById('fetchStatsBtn');
    if (!fetchBtn) return;

    fetchBtn.addEventListener('click', async () => {
        const month = document.getElementById('monthSelect').value;
        const year = document.getElementById('yearSelect').value;

        try {
            const [expensesRes, catRes] = await Promise.all([
                fetch(`/api/expenses/by-month?year=${year}&month=${month}`),
                fetch('/api/categories')
            ]);

            const expenses = await expensesRes.json();
            const categories = await catRes.json();

            updateTotalExpenses(expenses);
            renderCategoryTotals(expenses, categories);
        } catch (err) {
            console.error('Failed to load dashboard data:', err);
        }
    });
}

// Update total monthly expenses
function updateTotalExpenses(expenses) {
    const totalElement = document.getElementById('totalExpenses');
    if (!totalElement) return;

    const total = expenses.reduce((acc, curr) => acc + Number(curr.sum || 0), 0);
    totalElement.textContent = total.toFixed(2);
}

// Render category cards
function renderCategoryTotals(expenses, categories) {
    const row = document.getElementById('categoryTotalsRow');
    if (!row) return;

    const catTotals = {};
    categories.forEach(c => {
        catTotals[c.id] = { id: c.id, name: c.name, total: 0 };
    });

    expenses.forEach(e => {
        if (e.category && catTotals[e.category.id]) {
            catTotals[e.category.id].total += Number(e.sum || 0);
        }
    });

    row.innerHTML = '<h1>Category Totals</h1>';

    Object.values(catTotals).forEach(c => {
        const catObj = categories.find(cat => cat.id === c.id);
        const budget = catObj ? Number(catObj.monthlyBudget || 0) : 0;
        const left = budget - c.total;
        const statusClass = left < 0 ? 'text-negative' : 'text-positive';

        const col = document.createElement('div');
        col.className = 'col-md-3 mb-3';
        col.innerHTML = `
            <div class="summary-card">
                <div class="summary-title">${c.name}</div>
                <div class="summary-value">Spent: ${c.total.toFixed(2)}</div>
                <div class="summary-desc">Budget: ${budget.toFixed(2)}</div>
                <div class="summary-desc">Left: <span class="${statusClass}">${left.toFixed(2)}</span></div>
            </div>
        `;
        row.appendChild(col);
    });
}