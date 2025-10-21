// 게시글 상세 페이지 JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // 코드 하이라이팅 (Prism.js 등 사용 시)
    highlightCode();
});

/**
 * 좋아요 토글
 */
function toggleLike() {
    const likeBtn = document.querySelector('.btn-like');
    const icon = likeBtn.querySelector('i');
    const countSpan = likeBtn.querySelector('.count');

    // TODO: 서버에 좋아요 요청
    // POST /board/{id}/like

    if (likeBtn.classList.contains('active')) {
        // 좋아요 취소
        likeBtn.classList.remove('active');
        icon.classList.remove('fas');
        icon.classList.add('far');
        countSpan.textContent = parseInt(countSpan.textContent) - 1;
    } else {
        // 좋아요
        likeBtn.classList.add('active');
        icon.classList.remove('far');
        icon.classList.add('fas');
        countSpan.textContent = parseInt(countSpan.textContent) + 1;
    }
}

/**
 * 댓글 좋아요 토글
 */
function toggleCommentLike(button) {
    const icon = button.querySelector('i');
    const countSpan = button.querySelector('span');

    // TODO: 서버에 댓글 좋아요 요청
    // POST /board/comment/{id}/like

    if (icon.classList.contains('fas')) {
        // 좋아요 취소
        icon.classList.remove('fas');
        icon.classList.add('far');
        countSpan.textContent = parseInt(countSpan.textContent) - 1;
    } else {
        // 좋아요
        icon.classList.remove('far');
        icon.classList.add('fas');
        countSpan.textContent = parseInt(countSpan.textContent) + 1;
    }
}

/**
 * 답글 폼 표시
 */
function showReplyForm(button) {
    const commentItem = button.closest('.comment-item');
    const replyForm = commentItem.querySelector('.reply-form');

    if (replyForm) {
        replyForm.style.display = 'block';
        replyForm.querySelector('textarea').focus();
    }
}

/**
 * 답글 폼 숨기기
 */
function hideReplyForm(button) {
    const replyForm = button.closest('.reply-form');
    if (replyForm) {
        replyForm.style.display = 'none';
        replyForm.querySelector('textarea').value = '';
    }
}

/**
 * 댓글 삭제
 */
function deleteComment(button) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    const commentItem = button.closest('.comment-item');
    const commentId = commentItem.dataset.commentId;

    // TODO: 서버에 삭제 요청
    // DELETE /board/comment/{id}

    // 성공 시 DOM에서 제거
    commentItem.style.opacity = '0';
    commentItem.style.transform = 'translateX(-20px)';

    setTimeout(() => {
        commentItem.remove();
        updateCommentCount(-1);
    }, 300);
}

/**
 * 댓글 수 업데이트
 */
function updateCommentCount(delta) {
    const countElements = document.querySelectorAll('.comment-count');
    countElements.forEach(el => {
        const currentCount = parseInt(el.textContent);
        el.textContent = currentCount + delta;
    });
}

/**
 * 북마크 토글
 */
document.querySelector('.btn-bookmark')?.addEventListener('click', function() {
    const icon = this.querySelector('i');

    // TODO: 서버에 북마크 요청
    // POST /board/{id}/bookmark

    if (icon.classList.contains('far')) {
        icon.classList.remove('far');
        icon.classList.add('fas');
        showToast('북마크에 추가되었습니다');
    } else {
        icon.classList.remove('fas');
        icon.classList.add('far');
        showToast('북마크가 해제되었습니다');
    }
});

/**
 * 공유 기능
 */
document.querySelector('.btn-share')?.addEventListener('click', function() {
    const url = window.location.href;
    const title = document.querySelector('.post-detail-title').textContent;

    // Web Share API 지원 확인
    if (navigator.share) {
        navigator.share({
            title: title,
            url: url
        }).then(() => {
            showToast('공유되었습니다');
        }).catch((error) => {
            console.log('공유 실패:', error);
        });
    } else {
        // 폴백: 클립보드에 복사
        navigator.clipboard.writeText(url).then(() => {
            showToast('링크가 클립보드에 복사되었습니다');
        }).catch(() => {
            // 폴백의 폴백: 수동으로 복사 안내
            prompt('이 링크를 복사하세요:', url);
        });
    }
});

/**
 * 토스트 메시지 표시
 */
function showToast(message) {
    // 토스트가 이미 있으면 제거
    const existingToast = document.querySelector('.toast-message');
    if (existingToast) {
        existingToast.remove();
    }

    // 새 토스트 생성
    const toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.textContent = message;
    toast.style.cssText = `
        position: fixed;
        bottom: 2rem;
        left: 50%;
        transform: translateX(-50%);
        background: var(--primary-color);
        color: white;
        padding: 1rem 2rem;
        border-radius: 8px;
        font-weight: 600;
        z-index: 9999;
        animation: slideUp 0.3s ease;
    `;

    document.body.appendChild(toast);

    // 3초 후 제거
    setTimeout(() => {
        toast.style.animation = 'slideDown 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

/**
 * 코드 하이라이팅
 */
function highlightCode() {
    // Prism.js나 highlight.js 사용 시
    // 여기서는 간단한 스타일만 적용
    const codeBlocks = document.querySelectorAll('pre code');
    codeBlocks.forEach(block => {
        block.style.display = 'block';
        block.style.padding = '1rem';
    });
}

/**
 * 이미지 클릭 시 크게 보기
 */
document.querySelectorAll('.post-detail-content img').forEach(img => {
    img.style.cursor = 'pointer';
    img.addEventListener('click', function() {
        openImageModal(this.src);
    });
});

/**
 * 이미지 모달
 */
function openImageModal(src) {
    // 모달 생성
    const modal = document.createElement('div');
    modal.className = 'image-modal';
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.9);
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 10000;
        cursor: pointer;
    `;

    const img = document.createElement('img');
    img.src = src;
    img.style.cssText = `
        max-width: 90%;
        max-height: 90%;
        border-radius: 8px;
    `;

    modal.appendChild(img);
    document.body.appendChild(modal);

    // 클릭 시 닫기
    modal.addEventListener('click', function() {
        this.remove();
    });

    // ESC 키로 닫기
    document.addEventListener('keydown', function closeModal(e) {
        if (e.key === 'Escape') {
            modal.remove();
            document.removeEventListener('keydown', closeModal);
        }
    });
}

/**
 * 댓글 작성 폼 검증
 */
document.querySelectorAll('form[action*="/comment"]').forEach(form => {
    form.addEventListener('submit', function(e) {
        const textarea = this.querySelector('textarea');
        const content = textarea.value.trim();

        if (content.length < 2) {
            e.preventDefault();
            alert('댓글은 최소 2자 이상 입력해주세요.');
            textarea.focus();
            return false;
        }

        if (content.length > 1000) {
            e.preventDefault();
            alert('댓글은 최대 1000자까지 입력 가능합니다.');
            textarea.focus();
            return false;
        }
    });
});

// CSS 애니메이션 추가
const style = document.createElement('style');
style.textContent = `
    @keyframes slideUp {
        from {
            opacity: 0;
            transform: translateX(-50%) translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateX(-50%) translateY(0);
        }
    }

    @keyframes slideDown {
        from {
            opacity: 1;
            transform: translateX(-50%) translateY(0);
        }
        to {
            opacity: 0;
            transform: translateX(-50%) translateY(20px);
        }
    }
`;
document.head.appendChild(style);