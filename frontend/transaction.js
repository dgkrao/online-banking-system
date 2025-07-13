const user = JSON.parse(localStorage.getItem("user"));

if (!user || !user.id) {
    window.location.href = "login.html";
}

function loadTransactions() {
    fetch(`http://localhost:8080/api/transactions/user/${user.id}`)
        .then(res => res.json())
        .then(transactions => {
            const tbody = document.querySelector("#transactionTable tbody");
            tbody.innerHTML = "";
            transactions.forEach(txn => {
                const row = document.createElement("tr");
                row.innerHTML = `
          <td>${txn.id}</td>
          <td>${txn.type}</td>
          <td>₹${txn.amount.toFixed(2)}</td>
          <td>${txn.toMobile || txn.toAccount || "-"}</td>
          <td>${txn.timestamp.replace("T", " ").slice(0, 19)}</td>
        `;
                tbody.appendChild(row);
            });
        })
        .catch(err => {
            console.error("Failed to load transactions", err);
        });
}

loadTransactions();
