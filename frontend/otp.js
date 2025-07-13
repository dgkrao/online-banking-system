const resendBtn = document.getElementById("resendBtn");
const otpMessage = document.getElementById("otpMessage");

let resendCountdown = 30;
let interval;

function verifyOtp() {
    const mobile = document.getElementById("mobile").value;
    const otp = document.getElementById("otp").value;

    fetch(`http://localhost:8080/api/otp/verify?mobile=${mobile}&otp=${otp}`)
        .then(res => res.text())
        .then(res => {
            otpMessage.textContent = res;
            if (res.includes("success")) {
                setTimeout(() => {
                    window.location.href = "login.html";
                }, 1500);
            }
        })
        .catch(err => {
            otpMessage.textContent = "OTP verification failed.";
            console.error(err);
        });
}

function resendOtp() {
    const mobile = document.getElementById("mobile").value;
    fetch(`http://localhost:8080/api/otp/resend?mobile=${mobile}`)
        .then(res => res.text())
        .then(res => {
            otpMessage.textContent = res;
            startTimer();
        })
        .catch(err => {
            otpMessage.textContent = "Resend failed!";
            console.error(err);
        });
}

function startTimer() {
    resendBtn.disabled = true;
    resendCountdown = 30;
    resendBtn.textContent = `Resend OTP (${resendCountdown}s)`;

    interval = setInterval(() => {
        resendCountdown--;
        resendBtn.textContent = `Resend OTP (${resendCountdown}s)`;

        if (resendCountdown <= 0) {
            clearInterval(interval);
            resendBtn.disabled = false;
            resendBtn.textContent = "Resend OTP";
        }
    }, 1000);
}

startTimer(); // Automatically start countdown when page loads
