const user = JSON.parse(localStorage.getItem("user"));
if (!user || !user.id) {
    window.location.href = "login.html";
}

const methodSelect = document.getElementById("method");
const mobileField = document.getElementById("mobileField");
const accountField = document.getElementById("accountField");
const form = document.getElementById("transferForm");
const message = document.getElementById("transferMessage");

function toggleTransferFields() {
    if (methodSelect.value === "mobile") {
        mobileField.style.display = "block";
        accountField.style.display = "none";
    } else {
        mobileField.style.display = "none";
        accountField.style.display = "block";
    }
}

form.addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        senderId: user.id,
        amount: parseFloat(document.getElementById("amount").value),
        toMobile: methodSelect.value === "mobile" ? document.getElementById("toMobile").value : null,
        toAccount: methodSelect.value === "account" ? document.getElementById("toAccount").value : null
    };

    fetch("http://localhost:8080/api/transactions/transfer", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => res.text())
        .then(res => {
            message.textContent = res;
            if (res.includes("success")) {
                updateUserBalance();
            }
        })
        .catch(err => {
            message.textContent = "Transfer failed!";
            console.error(err);
        });
});

function updateUserBalance() {
    fetch(`http://localhost:8080/api/users/${user.id}`)
        .then(res => res.json())
        .then(updatedUser => {
            localStorage.setItem("user", JSON.stringify(updatedUser));
        });
}
