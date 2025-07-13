const user = JSON.parse(localStorage.getItem("user"));

if (!user || !user.id) {
    window.location.href = "login.html";
}

function fetchUserData() {
    fetch(`http://localhost:8080/api/users/${user.id}`)
        .then(res => res.json())
        .then(data => {
            document.getElementById("username").textContent = data.name;
            document.getElementById("usermobile").textContent = data.mobile;
            document.getElementById("userbalance").textContent = data.balance.toFixed(2);
            localStorage.setItem("user", JSON.stringify(data)); // keep updated
        })
        .catch(err => {
            console.error("Error fetching user:", err);
        });
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

fetchUserData();
