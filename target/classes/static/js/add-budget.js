document.addEventListener('DOMContentLoaded', () => {
    const submitBtn = document.getElementById('submitBudgetBtn') || document.querySelector('.form-actions .btn-primary');
    const budgetNameInput = document.getElementById('budgetName');
    const allocatedBudgetInput = document.getElementById('allocatedBudget');

    if (submitBtn) {
        submitBtn.addEventListener('click', () => {
            const name = budgetNameInput.value.trim();
            const monthlyBudget = parseFloat(allocatedBudgetInput.value);

            if (!name || isNaN(monthlyBudget)) {
                alert('Please enter a valid name and budget.');
                return;
            }

            fetch('/api/categories', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, monthlyBudget })
            })
                .then(res => res.ok ? res.json() : Promise.reject('Failed to save'))
                .then(() => {
                    alert('Category saved!');
                    budgetNameInput.value = '';
                    allocatedBudgetInput.value = '';
                })
                .catch(err => alert('Error: ' + err));
        });
    }
});