// Personal Finance Manager - Frontend Controller
document.addEventListener("DOMContentLoaded", () => {
  // Elements
  const lblTotalIncome = document.getElementById("lblTotalIncome");
  const lblTotalExpense = document.getElementById("lblTotalExpense");
  const lblSavingsProgress = document.getElementById("lblSavingsProgress");
  const lblSavingsGoal = document.getElementById("lblSavingsGoal");
  const goalProgressBar = document.getElementById("goalProgressBar");
  const goalPercentageBadge = document.getElementById("goalPercentageBadge");
  const expenseRatioBadge = document.getElementById("expenseRatioBadge");
  
  const expenseAlertBanner = document.getElementById("expenseAlertBanner");
  const alertMessageText = document.getElementById("alertMessageText");

  const transactionsTableBody = document.getElementById("transactionsTableBody");
  const filterCategory = document.getElementById("filterCategory");
  
  const transactionForm = document.getElementById("transactionForm");
  const txtAmount = document.getElementById("txtAmount");
  const txtDescription = document.getElementById("txtDescription");
  const comboCategory = document.getElementById("comboCategory");
  const txtDate = document.getElementById("txtDate");

  const savingsGoalForm = document.getElementById("savingsGoalForm");
  const txtSavingsGoal = document.getElementById("txtSavingsGoal");
  const categoryBreakdownList = document.getElementById("categoryBreakdownList");

  // Set today's date in input by default
  const today = new Date().toISOString().split("T")[0];
  txtDate.value = today;

  let allTransactions = [];

  // Currency Formatter
  const formatMoney = (val) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
      minimumFractionDigits: 2,
    }).format(val || 0);
  };

  // Toast notification
  const showToast = (message, isError = false) => {
    const toastContainer = document.getElementById("toastContainer");
    const toast = document.createElement("div");
    toast.className = `toast ${isError ? "toast-error" : ""}`;
    toast.textContent = message;
    toastContainer.appendChild(toast);
    setTimeout(() => {
      toast.style.opacity = "0";
      toast.style.transition = "opacity 0.3s";
      setTimeout(() => toast.remove(), 300);
    }, 3500);
  };

  // Category Color Map
  const categoryColors = {
    Food: "#f97316",
    Rent: "#6366f1",
    Utilities: "#06b6d4",
    Entertainment: "#ec4899",
    Income: "#10b981",
    Other: "#64748b",
  };

  // Fetch Summary
  const loadSummary = async () => {
    try {
      const res = await fetch("/api/dashboard/summary");
      if (!res.ok) throw new Error("Failed to load dashboard summary");
      const summary = await res.json();

      lblTotalIncome.textContent = formatMoney(summary.totalIncome);
      lblTotalExpense.textContent = formatMoney(summary.totalExpense);
      lblSavingsProgress.textContent = formatMoney(summary.savingsProgress);
      lblSavingsGoal.textContent = formatMoney(summary.savingsGoal);

      txtSavingsGoal.value = summary.savingsGoal > 0 ? summary.savingsGoal : "";

      const goalPct = Math.round(summary.goalProgressPercent || 0);
      goalPercentageBadge.textContent = `${goalPct}%`;
      goalProgressBar.style.width = `${Math.min(100, Math.max(0, goalPct))}%`;

      const expPct = Math.round(summary.expenseRatioPercent || 0);
      expenseRatioBadge.textContent = `${expPct}% of inc`;

      // Check Expense Alert (Swing app: expenses exceed 50% of income)
      if (summary.expenseAlertActive) {
        expenseAlertBanner.classList.remove("hidden");
        if (summary.expenseAlertMessage) {
          alertMessageText.textContent = summary.expenseAlertMessage;
        }
      } else {
        expenseAlertBanner.classList.add("hidden");
      }

      // Render Category Breakdown
      renderCategoryBreakdown(summary.categoryBreakdown);
    } catch (err) {
      console.error(err);
      showToast("Could not load summary data", true);
    }
  };

  // Render Category Breakdown
  const renderCategoryBreakdown = (categories) => {
    if (!categories || categories.length === 0) {
      categoryBreakdownList.innerHTML = `<p class="text-muted">No expenses recorded yet.</p>`;
      return;
    }

    categoryBreakdownList.innerHTML = categories
      .map((cat) => {
        const color = categoryColors[cat.category] || categoryColors.Other;
        return `
          <div class="category-breakdown-item">
            <div class="cat-item-header">
              <span>${cat.category}</span>
              <span><strong>${formatMoney(cat.totalAmount)}</strong> (${cat.percentage}%)</span>
            </div>
            <div class="cat-bar-bg">
              <div class="cat-bar-fill" style="width: ${cat.percentage}%; background-color: ${color};"></div>
            </div>
          </div>
        `;
      })
      .join("");
  };

  // Fetch Transactions
  const loadTransactions = async () => {
    try {
      const res = await fetch("/api/transactions");
      if (!res.ok) throw new Error("Failed to load transactions");
      allTransactions = await res.json();
      renderTransactionsTable();
    } catch (err) {
      console.error(err);
      showToast("Could not load transactions", true);
    }
  };

  // Render Table
  const renderTransactionsTable = () => {
    const selectedFilter = filterCategory.value;
    const filtered = allTransactions.filter((tx) => {
      if (selectedFilter === "ALL") return true;
      return tx.category && tx.category.toLowerCase() === selectedFilter.toLowerCase();
    });

    if (filtered.length === 0) {
      transactionsTableBody.innerHTML = `
        <tr>
          <td colspan="5" class="table-empty">No transactions found for this selection.</td>
        </tr>
      `;
      return;
    }

    transactionsTableBody.innerHTML = filtered
      .map((tx) => {
        const isIncome = (tx.category && tx.category.toLowerCase() === "income") || tx.type === "INCOME";
        const catClass = tx.category ? `badge-${tx.category.toLowerCase()}` : "badge-other";
        const formattedDate = tx.date ? tx.date : "Today";
        return `
          <tr>
            <td style="color: var(--text-muted); font-size: 0.85rem;">${formattedDate}</td>
            <td><strong>${escapeHtml(tx.description)}</strong></td>
            <td><span class="badge ${catClass}">${escapeHtml(tx.category)}</span></td>
            <td class="text-right" style="font-weight: 700; color: ${isIncome ? "var(--success-text)" : "var(--danger-text)"}">
              ${isIncome ? "+" : "-"}${formatMoney(tx.amount)}
            </td>
            <td class="text-center">
              <button class="btn-delete" onclick="window.deleteTransactionItem(${tx.id})" title="Delete transaction">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M3 6h18"/>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/>
                </svg>
              </button>
            </td>
          </tr>
        `;
      })
      .join("");
  };

  const escapeHtml = (unsafe) => {
    if (!unsafe) return "";
    return unsafe
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  };

  // Add Transaction Form Submission
  transactionForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const amountVal = parseFloat(txtAmount.value);
    const descVal = txtDescription.value.trim();
    const catVal = comboCategory.value;
    const dateVal = txtDate.value || null;

    if (isNaN(amountVal) || amountVal <= 0) {
      showToast("Please enter a valid amount greater than 0", true);
      return;
    }
    if (!descVal) {
      showToast("Please provide a description", true);
      return;
    }

    try {
      const res = await fetch("/api/transactions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          amount: amountVal,
          description: descVal,
          category: catVal,
          date: dateVal,
        }),
      });

      if (!res.ok) throw new Error("Failed to add transaction");

      showToast("Transaction added successfully!");
      txtAmount.value = "";
      txtDescription.value = "";
      txtDate.value = today;

      await loadTransactions();
      await loadSummary();
    } catch (err) {
      console.error(err);
      showToast("Error adding transaction", true);
    }
  });

  // Set Savings Goal Form Submission
  savingsGoalForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const goalVal = parseFloat(txtSavingsGoal.value);
    if (isNaN(goalVal) || goalVal < 0) {
      showToast("Please enter a valid savings goal amount", true);
      return;
    }

    try {
      const res = await fetch("/api/savings-goal", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          targetAmount: goalVal,
          title: "Personal Savings Goal",
        }),
      });

      if (!res.ok) throw new Error("Failed to save savings goal");

      showToast("Savings goal updated!");
      await loadSummary();
    } catch (err) {
      console.error(err);
      showToast("Error updating savings goal", true);
    }
  });

  // Filter Change Listener
  filterCategory.addEventListener("change", () => {
    renderTransactionsTable();
  });

  // Global Delete Function
  window.deleteTransactionItem = async (id) => {
    if (!confirm("Are you sure you want to delete this transaction?")) return;
    try {
      const res = await fetch(`/api/transactions/${id}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("Failed to delete transaction");

      showToast("Transaction deleted");
      await loadTransactions();
      await loadSummary();
    } catch (err) {
      console.error(err);
      showToast("Error deleting transaction", true);
    }
  };

  // Initial Load
  loadSummary();
  loadTransactions();
});
