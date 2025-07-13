// script.js

document.addEventListener("DOMContentLoaded", () => {
    const page = window.location.pathname;

    // 🔐 LOGIN
    if (page.includes("login.html")) {
        document.getElementById("loginForm").addEventListener("submit", async (e) => {
            e.preventDefault();
            const identifier = document.getElementById("loginIdentifier").value;
            const password = document.getElementById("loginPassword").value;

            const res = await fetch("/api/users/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ identifier, password })
            });

            const data = await res.json();
            if (res.ok) {
                localStorage.setItem("user", JSON.stringify(data));
                window.location.href = "otp.html";
            } else {
                document.getElementById("loginError").textContent = data.message || "Invalid credentials";
            }
        });
    }

    // 📝 REGISTER
    if (page.includes("register.html")) {
        document.getElementById("registerForm").addEventListener("submit", async (e) => {
            e.preventDefault();
            const name = document.getElementById("name").value;
            const email = document.getElementById("email").value;
            const mobile = document.getElementById("mobile").value;
            const password = document.getElementById("password").value;
            const confirm = document.getElementById("confirmPassword").value;

            if (password !== confirm) {
                document.getElementById("registerError").textContent = "Passwords do not match.";
                return;
            }

            const res = await fetch("/api/users/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name, email, mobile, password })
            });

            const data = await res.json();
            if (res.ok) {
                localStorage.setItem("user", JSON.stringify(data));
                window.location.href = "otp.html";
            } else {
                document.getElementById("registerError").textContent = data.message || "Registration failed.";
            }
        });
    }

    // 🔑 OTP
    if (page.includes("otp.html")) {
        document.getElementById("otpForm").addEventListener("submit", async (e) => {
            e.preventDefault();
            const otp = document.getElementById("otp").value;
            const user = JSON.parse(localStorage.getItem("user"));

            const res = await fetch(`/api/otp/verify?mobile=${user.mobile}&otp=${otp}`);
            const result = await res.json();

            if (res.ok) {
                window.location.href = "dashboard.html";
            } else {
                document.getElementById("otpError").textContent = result.message || "Invalid OTP.";
            }
        });
    }

    // 🏦 DASHBOARD
    if (page.includes("dashboard.html")) {
        const user = JSON.parse(localStorage.getItem("user"));
        document.getElementById("userName").textContent = user.name;

        fetch(`/api/users/balance?mobile=${user.mobile}`)
            .then(res => res.json())
            .then(data => {
                document.getElementById("userBalance").textContent = data.balance;
            });
    }

    // 💸 TRANSFER
    if (page.includes("transfer.html")) {
        const transferTypeSelect = document.getElementById("transferType");
        const mobileTransfer = document.getElementById("mobileTransfer");
        const bankTransfer = document.getElementById("bankTransfer");

        transferTypeSelect.addEventListener("change", () => {
            const type = transferTypeSelect.value;
            mobileTransfer.style.display = type === "mobile" ? "block" : "none";
            bankTransfer.style.display = type === "bank" ? "block" : "none";
        });

        document.getElementById("transferForm").addEventListener("submit", async (e) => {
            e.preventDefault();
            const user = JSON.parse(localStorage.getItem("user"));
            const type = transferTypeSelect.value;
            const amount = parseFloat(document.getElementById("amount").value);
            let requestBody = { senderMobile: user.mobile, amount };

            if (type === "mobile") {
                const recipientMobile = document.getElementById("recipientMobile").value;
                requestBody.recipientMobile = recipientMobile;
            } else {
                const acc = document.getElementById("recipientAccount").value;
                const confirm = document.getElementById("confirmAccount").value;
                const ifsc = document.getElementById("ifsc").value;
                const bankName = document.getElementById("bankName").value;

                if (acc !== confirm) {
                    document.getElementById("transferError").textContent = "Account numbers do not match.";
                    return;
                }

                requestBody.recipientAccount = acc;
                requestBody.ifsc = ifsc;
                requestBody.bankName = bankName;
            }

            const res = await fetch("/api/transactions/transfer", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody)
            });

            const data = await res.json();

            if (res.ok) {
                document.getElementById("transferSuccess").textContent = "Transfer successful.";
                document.getElementById("transferError").textContent = "";
            } else {
                document.getElementById("transferError").textContent = data.message || "Transfer failed.";
                document.getElementById("transferSuccess").textContent = "";
            }
        });
    }

    // 📄 TRANSACTIONS
    if (page.includes("transactions.html")) {
        const user = JSON.parse(localStorage.getItem("user"));
        fetch(`/api/transactions/history?mobile=${user.mobile}`)
            .then(res => res.json())
            .then(transactions => {
                const list = document.getElementById("transactionList");
                transactions.forEach(txn => {
                    const li = document.createElement("li");
                    li.textContent = `#${txn.id} | ₹${txn.amount} | ${txn.type.toUpperCase()} | To/From: ${txn.recipient}`;
                    list.appendChild(li);
                });
            });
    }
});
