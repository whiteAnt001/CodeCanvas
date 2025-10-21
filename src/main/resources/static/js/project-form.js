// 프로젝트 작성 폼 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // 폼 제출 처리
    initFormSubmit();

    // 커스텀 기술 스택 처리
    initCustomTech();
});

/**
 * 썸네일 이미지 미리보기
 */
function previewThumbnail(event) {
    const file = event.target.files[0];
    if (!file) return;

    // 파일 크기 체크 (5MB)
    if (file.size > 5 * 1024 * 1024) {
        alert('파일 크기는 5MB를 초과할 수 없습니다.');
        event.target.value = '';
        return;
    }

    // 이미지 파일인지 체크
    if (!file.type.startsWith('image/')) {
        alert('이미지 파일만 업로드 가능합니다.');
        event.target.value = '';
        return;
    }

    const reader = new FileReader();
    reader.onload = function(e) {
        const previewDiv = document.getElementById('thumbnailPreview');
        previewDiv.innerHTML = `
            <img src="${e.target.result}" alt="Thumbnail Preview">
            <button type="button" class="remove-image" onclick="removeThumbnail()">
                <i class="fas fa-times"></i>
            </button>
        `;

        // 업로드 라벨 숨기기
        document.querySelector('#thumbnailArea .image-upload-label').style.display = 'none';
    };
    reader.readAsDataURL(file);
}

/**
 * 썸네일 제거
 */
function removeThumbnail() {
    document.getElementById('thumbnail').value = '';
    document.getElementById('thumbnailPreview').innerHTML = '';
    document.querySelector('#thumbnailArea .image-upload-label').style.display = 'flex';
}

/**
 * 다중 이미지 미리보기
 */
function previewImages(event) {
    const files = Array.from(event.target.files);

    if (files.length > 5) {
        alert('최대 5개의 이미지만 업로드 가능합니다.');