const loginForm = document.getElementById("loginForm");
const loginMessage = document.getElementById("loginMessage");

loginForm.addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        mobile: document.getElementById("mobile").value,
        password: document.getElementById("password").value
    };

    fetch("http://localhost:8080/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => {
            if (!res.ok) throw new Error("Login failed");
            return res.json();
        })
        .then(user => {
            localStorage.setItem("user", JSON.stringify(user));
            loginMessage.textContent = "Login successful!";
            setTimeout(() => {
                window.location.href = "dashboard.html";
            }, 1500);
        })
        .catch(err => {
            loginMessage.textContent = "Invalid mobile number or password!";
            console.error(err);
        });
});
