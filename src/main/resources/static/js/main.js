// CodeCanvas - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {

    // 필터 버튼 활성화
    initFilterButtons();

    // 프로젝트 카드 클릭 이벤트
    initProjectCards();

    // 검색 기능
    initSearch();
});

/**
 * 필터 버튼 초기화
 */
function initFilterButtons() {
    const filterButtons = document.querySelectorAll('.filter-btn');

    filterButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            // 모든 버튼의 active 클래스 제거
            filterButtons.forEach(b => b.classList.remove('active'));

            // 클릭된 버튼에 active 클래스 추가
            this.classList.add('active');

            // 필터링 로직 (서버에서 처리할 수도 있음)
            const category = this.textContent.trim();
            filterProjects(category);
        });
    });
}

/**
 * 프로젝트 필터링
 */
function filterProjects(category) {
    console.log('필터 카테고리:', category);

    // TODO: 서버에 AJAX 요청을 보내거나 클라이언트에서 필터링
    // 예시: /api/projects?category=${category}

    if (category === '전체') {
        // 모든 프로젝트 표시
        showAllProjects();
    } else {
        // 특정 카테고리 프로젝트만 표시
        // filterProjectsByCategory(category);
    }
}

/**
 * 모든 프로젝트 표시
 */
function showAllProjects() {
    const projectCards = document.querySelectorAll('.project-card');
    projectCards.forEach(card => {
        card.parentElement.style.display = 'block';
    });
}

/**
 * 프로젝트 카드 클릭 이벤트 초기화
 */
function initProjectCards() {
    const projectCards = document.querySelectorAll('.project-card');

    projectCards.forEach(card => {
        card.addEventListener('click', function(e) {
            // 프로젝트 ID를 data 속성에서 가져오기
            const projectId = this.dataset.projectId;

            if (projectId) {
                // 상세 페이지로 이동
                window.location.href = `/projects/${projectId}`;
            } else {
                console.log('프로젝트 상세 페이지로 이동');
            }
        });

        // 호버 효과 강화
        card.addEventListener('mouseenter', function() {
            this.style.cursor = 'pointer';
        });
    });
}

/**
 * 검색 기능 초기화
 */
function initSearch() {
    const searchBox = document.querySelector('.search-box');

    if (searchBox) {
        // 실시간 검색 (디바운스 적용)
        let searchTimeout;

        searchBox.addEventListener('input', function(e) {
            clearTimeout(searchTimeout);

            const keyword = e.target.value.trim();

            searchTimeout = setTimeout(() => {
                if (keyword.length >= 2) {
                    searchProjects(keyword);
                } else if (keyword.length === 0) {
                    showAllProjects();
                }
            }, 300);
        });

        // Enter 키 검색
        searchBox.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const keyword = e.target.value.trim();
                if (keyword) {
                    searchProjects(keyword);
                }
            }
        });
    }
}

/**
 * 프로젝트 검색
 */
function searchProjects(keyword) {
    console.log('검색 키워드:', keyword);

    // TODO: 서버에 AJAX 요청
    // 예시: /api/projects/search?keyword=${keyword}

    // 클라이언트 사이드 검색 예시
    const projectCards = document.querySelectorAll('.project-card');

    projectCards.forEach(card => {
        const title = card.querySelector('.project-title').textContent.toLowerCase();
        const description = card.querySelector('.project-description').textContent.toLowerCase();
        const searchKeyword = keyword.toLowerCase();

        if (title.includes(searchKeyword) || description.includes(searchKeyword)) {
            card.parentElement.style.display = 'block';
        } else {
            card.parentElement.style.display = 'none';
        }
    });
}

/**
 * 좋아요 토글
 */
function toggleLike(projectId, element) {
    // TODO: 서버에 좋아요 요청
    // POST /api/projects/${projectId}/like

    console.log('좋아요 토글:', projectId);

    // UI 업데이트 예시
    const icon = element.querySelector('i');
    const count = element.querySelector('.like-count');

    if (icon.classList.contains('far')) {
        icon.classList.remove('far');
        icon.classList.add('fas');
        count.textContent = parseInt(count.textContent) + 1;
    } else {
        icon.classList.remove('fas');
        icon.classList.add('far');
        count.textContent = parseInt(count.textContent) - 1;
    }
}

/**
 * 조회수 증가
 */
function incrementViewCount(projectId) {
    // TODO: 서버에 조회수 증가 요청
    // POST /api/projects/${projectId}/view

    console.log('조회수 증가:', projectId);
}

/**
 * 페이지네이션
 */
function loadPage(pageNumber) {
    console.log('페이지 로드:', pageNumber);

    // TODO: 서버에서 해당 페이지 데이터 가져오기
    // GET /api/projects?page=${pageNumber}

    // 페이지 이동
    window.location.href = `/projects?page=${pageNumber}`;
}

/**
 * 스크롤 시 네비게이션 스타일 변경
 */
window.addEventListener('scroll', function() {
    const navbar = document.querySelector('.navbar');

    if (window.scrollY > 50) {
        navbar.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
    } else {
        navbar.style.boxShadow = '0 1px 3px rgba(0, 0, 0, 0.05)';
    }
});

/* 게시판 스타일 */

/* 사이드바 */
.board-sidebar {
    background: var(--light-bg);
    border: 1px solid var(--border-color);
    border-radius: 12px;
    padding: 1.5rem;
    position: sticky;
    top: 20px;
}

.sidebar-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: var(--primary-color);
    margin-bottom: 1rem;
    padding-bottom: 0.75rem;
    border-bottom: 2px solid var(--border-color);
}

.sidebar-title i {
    margin-right: 0.5rem;
    color: var(--secondary-color);
}

/* 카테고리 리스트 */
.category-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.category-list li {
    margin-bottom: 0.5rem;
}

.category-item {
    display: flex;
    align-items: center;
    padding: 0.75rem 1rem;
    border-radius: 8px;
    color: var(--text-secondary);
    text-decoration: none;
    transition: all 0.3s;
    position: relative;
}

.category-item i {
    margin-right: 0.75rem;
    width: 20px;
    text-align: center;
}

.category-item span:nth-child(2) {
    flex: 1;
}

.category-item .post-count {
    background: var(--card-bg);
    padding: 0.2rem 0.6rem;
    border-radius: 12px;
    font-size: 0.85rem;
    font-weight: 600;
}

.category-item:hover {
    background: var(--hover-bg);
    color: var(--primary-color);
}

.category-item.active {
    background: var(--primary-color);
    color: white;
}

.category-item.active .post-count {
    background: rgba(255, 255, 255, 0.2);
    color: white;
}

/* 인기 태그 */
.popular-tags {
    padding-top: 1rem;
    border-top: 1px solid var(--border-color);
}

.sidebar-subtitle {
    font-size: 0.95rem;
    font-weight: 600;
    color: var(--secondary-color);
    margin-bottom: 0.75rem;
}

.sidebar-subtitle i {
    margin-right: 0.5rem;
    color: #ef4444;
}

.tag-cloud {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
}

.tag-cloud .tag-item {
    background: var(--card-bg);
    color: var(--text-secondary);
    padding: 0.4rem 0.8rem;
    border-radius: 16px;
    font-size: 0.85rem;
    text-decoration: none;
    border: 1px solid var(--border-color);
    transition: all 0.3s;
}

.tag-cloud .tag-item:hover {
    background: var(--primary-color);
    color: white;
    border-color: var(--primary-color);
}

/* 게시판 필터 */
.board-filter {
    background: var(--light-bg);
    border: 1px solid var(--border-color);
    border-radius: 12px;
    padding: 1.5rem;
    margin-bottom: 1.5rem;
}

.search-wrapper {
    position: relative;
}

.search-icon {
    position: absolute;
    left: 1rem;
    top: 50%;
    transform: translateY(-50%);
    color: var(--text-secondary);
}

.search-input {
    padding-left: 2.75rem;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    background: var(--card-bg);
}

.search-input:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 0.2rem rgba(26, 26, 26, 0.1);
    background: var(--light-bg);
}

.sort-select {
    border: 1px solid var(--border-color);
    border-radius: 8px;
    background: var(--card-bg);
}

.sort-select:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 0.2rem rgba(26, 26, 26, 0.1);
}

.btn-write {
    background: var(--primary-color);
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 8px;
    font-weight: 600;
    transition: all 0.3s;
}

.btn-write:hover {
    background: var(--accent-color);
    color: white;
    transform: translateY(-2px);
}

/* 공지사항 섹션 */
.notice-section {
    margin-bottom: 1rem;
}

/* 게시글 목록 */
.board-list {
    display: flex;
    flex-direction: column;
    gap: 1rem;
}

.post-item {
    display: flex;
    gap: 1rem;
    background: var(--light-bg);
    border: 1px solid var(--border-color);
    border-radius: 12px;
    padding: 1.5rem;
    transition: all 0.3s;
    position: relative;
}

.post-item:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    border-color: var(--secondary-color);
}

.post-item.notice {
    background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
    border-color: #fbbf24;
}

/* 게시글 뱃지 */
.post-badge {
    display: flex;
    align-items: center;
    justify-content: center;
    min-width: 70px;
    height: 32px;
    padding: 0 0.75rem;
    border-radius: 16px;
    font-size: 0.85rem;
    font-weight: 600;
    white-space: nowrap;
}

.post-badge i {
    margin-right: 0.35rem;
}

/* 카테고리별 뱃지 색상 */
.post-badge.notice-badge {
    background: #fbbf24;
    color: #78350f;
}

.post-badge.qna {
    background: #dbeafe;
    color: #1e40af;
}

.post-badge.free {
    background: #e0e7ff;
    color: #4338ca;
}

.post-badge.tip {
    background: #fef3c7;
    color: #92400e;
}

.post-badge.study {
    background: #d1fae5;
    color: #065f46;
}

.post-badge.job {
    background: #fce7f3;
    color: #9f1239;
}

/* 게시글 내용 */
.post-content {
    flex: 1;
}

.post-title {
    font-size: 1.1rem;
    font-weight: 600;
    margin-bottom: 0.75rem;
    line-height: 1.4;
}

.post-title a {
    color: var(--primary-color);
    text-decoration: none;
    transition: color 0.3s;
}

.post-title a:hover {
    color: var(--secondary-color);
}

.new-badge {
    display: inline-block;
    background: #ef4444;
    color: white;
    font-size: 0.7rem;
    font-weight: 700;
    padding: 0.15rem 0.4rem;
    border-radius: 4px;
    margin-left: 0.5rem;
    vertical-align: middle;
}

.comment-count {
    color: var(--secondary-color);
    font-size: 0.95rem;
    font-weight: 600;
    margin-left: 0.5rem;
}

.post-summary {
    color: var(--text-secondary);
    font-size: 0.95rem;
    line-height: 1.6;
    margin-bottom: 0.75rem;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

/* 태그 */
.post-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
    margin-bottom: 0.75rem;
}

.post-tags .tag {
    background: var(--card-bg);
    color: var(--text-secondary);
    padding: 0.25rem 0.75rem;
    border-radius: 12px;
    font-size: 0.85rem;
    border: 1px solid var(--border-color);
}

/* 메타 정보 */
.post-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
    font-size: 0.875rem;
    color: var(--text-secondary);
}

.meta-item {
    display: flex;
    align-items: center;
    gap: 0.35rem;
}

.meta-item i {
    color: var(--secondary-color);
}

/* 답변 완료 상태 */
.post-status {
    display: flex;
    align-items: flex-start;
}

.solved-badge {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-width: 60px;
    padding: 0.5rem;
    background: #d1fae5;
    border: 2px solid #10b981;
    border-radius: 8px;
    color: #065f46;
}

.solved-badge i {
    font-size: 1.5rem;
    margin-bottom: 0.25rem;
    color: #10b981;
}

.solved-badge span {
    font-size: 0.75rem;
    font-weight: 700;
}

/* 빈 상태 */
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 4rem 2rem;
    background: var(--light-bg);
    border: 1px dashed var(--border-color);
    border-radius: 12px;
    text-align: center;
}

.empty-state i {
    font-size: 4rem;
    color: var(--text-secondary);
    margin-bottom: 1rem;
}

.empty-state h3 {
    color: var(--primary-color);
    margin-bottom: 0.5rem;
}

.empty-state p {
    color: var(--text-secondary);
    margin-bottom: 1.5rem;
}

/* 반응형 */
@media (max-width: 992px) {
    .board-sidebar {
        position: static