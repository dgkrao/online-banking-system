const form = document.getElementById("registerForm");
const otpSection = document.getElementById("otpSection");
const message = document.getElementById("message");
const resendBtn = document.getElementById("resendOtpBtn");

let currentMobile = "";
let resendTimer = 30;
let timerInterval;

form.addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        mobile: document.getElementById("mobile").value,
        password: document.getElementById("password").value
    };

    currentMobile = data.mobile;

    fetch("http://localhost:8080/api/users/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
        .then(res => res.text())
        .then(res => {
            message.textContent = res;
            if (res.includes("OTP sent")) {
                form.style.display = "none";
                otpSection.style.display = "block";
                startTimer();
            }
        })
        .catch(err => {
            message.textContent = "Registration failed!";
            console.error(err);
        });
});

function verifyOtp() {
    const otp = document.getElementById("otp").value;

    fetch("http://localhost:8080/api/otp/verify?mobile=" + currentMobile + "&otp=" + otp)
        .then(res => res.text())
        .then(res => {
            message.textContent = res;
            if (res.includes("success")) {
                setTimeout(() => {
                    window.location.href = "login.html";
                }, 2000);
            }
        })
        .catch(err => {
            message.textContent = "OTP verification failed!";
            console.error(err);
        });
}

function resendOtp() {
    fetch("http://localhost:8080/api/otp/resend?mobile=" + currentMobile)
        .then(res => res.text())
        .then(res => {
            message.textContent = res;
            startTimer();
        })
        .catch(err => {
            message.textContent = "Resend failed!";
            console.error(err);
        });
}

function startTimer() {
    resendBtn.disabled = true;
    resendBtn.textContent = "Resend OTP (30s)";
    resendTimer = 30;

    timerInterval = setInterval(() => {
        resendTimer--;
        resendBtn.textContent = `Resend OTP (${resendTimer}s)`;
        if (resendTimer <= 0) {
            clearInterval(timerInterval);
            resendBtn.disabled = false;
            resendBtn.textContent = "Resend OTP";
        }
    }, 1000);
}
