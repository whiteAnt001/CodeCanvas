document.addEventListener("DOMContentLoaded", function () {
    // 회원가입 데이터 넘기기
    const registerForm = document.getElementById('registerForm');
    if(registerForm) {
        registerForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            const data = {
                username: document.getElementById('username').value,
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
            };

            await registerUser(data);
        });
    }

    // 로그인 데이터 넘기기
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            const data = {
                email: document.getElementById('email').value,
                password: document.getElementById('password').value,
            };

            await loginUser(data);
        });
    }
});

// 회원가입
async function registerUser(data) {
    try {
        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (response.ok) {
            alert("회원가입 완료!\n 이메일 인증을 완료해주세요!");
        } else {
            alert(result.error);
        }
    } catch (error) {
        console.error("회원가입 에러:", error);
    }
}

async function loginUser(data) {
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type' : 'application/json'},
            credentials: 'include',
            body: JSON.stringify(data),
        });

        const result = await response.json();

        if (response.ok) {
            window.location.href= '/';
        } else {
            if(result.error === '이메일 인증을 완료해주세요.') {
                alert(result.error);
                window.location.href= '/email/verification/resend'; // 이메일 재전송 안내
            }
            alert(result.error || "로그인 실패!");
        }
    } catch (error) {
        console.error('로그인 에러:', error);
    }
}


// 로그아웃
document.addEventListener("DOMContentLoaded", () => {
    const logoutBtn = document.getElementById("logout-btn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", (e) => {
            e.preventDefault();

            fetch("/api/auth/logout", {
                method: 'POST',
                credentials: 'include'
            }).then(response => {
                if (!response.ok) {
                    throw new Error("로그아웃 실패");
                }
                return;
            }).then(() => {
                alert("로그아웃 되었습니다.");
                window.location.href = "/";
            }).catch(error => {
                console.error("에러 발생", error);
                alert("로그아웃 중 문제가 발생했습니다.");
            });
        });
    }
});


// 닉네임 중복체크
document.addEventListener('DOMContentLoaded', () => {
    const checkBtn = document.getElementById('checkBtn');
    const usernameInput = document.getElementById('username');
    const resultElem = document.getElementById('checkResult');

    if (!checkBtn || !usernameInput || !resultElem) return; // 요소 없으면 종료

    checkBtn.addEventListener('click', () => {
        const username = usernameInput.value.trim();

        if (!username) {
            resultElem.textContent = "닉네임을 입력해주세요.";
            resultElem.className = "error";
            return;
        }

        fetch(`/api/user/check-username?username=${encodeURIComponent(username)}`)
            .then(res => res.json())
            .then(isTaken => {
                if (isTaken) {
                    resultElem.textContent = "이미 사용 중인 닉네임입니다.";
                    resultElem.className = "error";
                } else {
                    resultElem.textContent = "사용 가능한 닉네임입니다.";
                    resultElem.className = "success";
                }
            })
            .catch(() => {
                resultElem.textContent = "오류가 발생했습니다.";
                resultElem.className = "error";
            });
    });
});