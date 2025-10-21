document.addEventListener("DOMContentLoaded", () => {
    const submitBtn = document.getElementById("submitBtn");
    const boardType = document.getElementById("board");
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const imagesInput = document.getElementById("images");

    submitBtn.addEventListener("click", async () => {
        const title = titleInput.value.trim();
        const content = contentInput.value.trim();
        const files = imagesInput.files;

        if (!title || !content) {
            alert("제목과 내용을 입력해주세요.")
            return;
        }

        const formData = new FormData();
        formData.append("data", new Blob([JSON.stringify({
            boardType: boardType.value,
            title: title,
            content: content
        })], {type: "application/json" }));

        for (const file of files) {
            formData.append("files", file);
        }

        try {
            const res = await fetch("/api/board/write", {
                method: "POST",
                body: formData
            });

            if(!res.ok) throw new Error("HTTP error " + res.status);

            const result = await res.json();
            console.log(result);
            alert("게시글이 작성되었습니다!")
            location.href="/";
        } catch (err) {
            console.log(err);
            alert("게시글 등록 실패: " + err.message);
        }
    })
})