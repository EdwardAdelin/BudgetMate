document.addEventListener('DOMContentLoaded', () => {
    const categoryContainer = document.getElementById('categoryContainer');
    const expenseForm = document.querySelector('.expenses-form');
    const submitBtn = document.getElementById('submitExpenseBtn');
    let selectedCategoryId = null;

    // 1. Incarcam categoriile din backend
    fetch('/api/categories')
        .then(res => res.json())
        .then(categories => {
            if (!categoryContainer) return;
            categoryContainer.innerHTML = '';

            categories.forEach(cat => {
                const btn = document.createElement('button');
                btn.type = 'button';
                btn.className = 'category-btn';
                btn.textContent = cat.name;
                btn.dataset.categoryId = cat.id;

                btn.addEventListener('click', () => {
                    document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
                    btn.classList.add('active');
                    selectedCategoryId = cat.id;
                });

                categoryContainer.appendChild(btn);
            });
        })
        .catch(err => console.error('Error fetching categories:', err));

    // 2. Trimiterea formularului de adaugare cheltuiala
    if (submitBtn) {
        submitBtn.addEventListener('click', async (e) => {
            e.preventDefault();

            const name = document.getElementById('name').value.trim();
            const sum = document.getElementById('sum').value.trim();
            const date = document.getElementById('date').value;

            if (!name || !sum || !date || !selectedCategoryId) {
                alert('Please fill all fields and select a category. If no category has ever been added, go to categories and add your expenses categories!');
                return;
            }

            const expense = {
                name: name,
                sum: parseFloat(sum),
                date: date,
                category: { id: selectedCategoryId }
            };

            try {
                const res = await fetch('/api/expenses', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(expense)
                });

                if (res.ok) {
                    alert('Expense added!');
                    expenseForm.reset();
                    document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
                    selectedCategoryId = null;
                } else {
                    alert('Failed to add expense.');
                }
            } catch (err) {
                alert('Error: ' + err);
            }
        });
    }
});