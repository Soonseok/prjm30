<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>list</title>
<link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
</head>
<body>

<!-- 제목 + 우측 정보 -->
<div class="page-info-bar">
	<h1>list</h1>
	<div class="info">
		전체 글 : ${totRowCnt }<br />
		현재 페이지 / 전체 페이지 : ${searchVO.page } / ${searchVO.totPage }
	</div>
</div>

<!-- toast -->
<div id="toast"></div>

<!-- 검색바 -->
<form action="list" method="post">
	<div class="search-form">
		<label>
			제목
			<c:choose>
				<c:when test="${btitle}">
					<input type="checkbox" name="searchType" value="btitle" checked />
				</c:when>
				<c:otherwise>
					<input type="checkbox" name="searchType" value="btitle" />
				</c:otherwise>
			</c:choose>
		</label>
		|
		<label>
			내용
			<c:choose>
				<c:when test="${bcontent}">
					<input type="checkbox" name="searchType" value="bcontent" checked />
				</c:when>
				<c:otherwise>
					<input type="checkbox" name="searchType" value="bcontent" />
				</c:otherwise>
			</c:choose>
		</label>
		<input type="text" name="sk" id="searchBox" value="${searchKeyword}" class="search-input" />
		<button type="submit" class="search-button">검색</button>
	</div>
</form>

<!-- 페이징 UI -->
<div class="pagination-wrapper">
	<div class="pagination-controls">
		<!-- 처음 / 이전 -->
		<c:set var="prevPage" value="${searchVO.page - 1}" />
		<c:if test="${prevPage < 1}">
		    <c:set var="prevPage" value="1" />
		</c:if>
		<a class="nav <c:if test='${searchVO.page == 1}'>disabled</c:if>'" href="list?page=1">처음</a>
		<a class="nav <c:if test='${searchVO.page == 1}'>disabled</c:if>'" href="list?page=${prevPage}">이전</a>

		<!-- 구분 공간 -->
		<span class="spacer"></span>

		<!-- 숫자 버튼 -->
		<c:forEach begin="${searchVO.pageStart}" end="${searchVO.pageEnd}" var="i">
			<c:choose>
				<c:when test="${i eq searchVO.page}">
					<span class="current">${i}</span>
				</c:when>
				<c:otherwise>
					<a href="list?page=${i}">${i}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>

		<!-- 구분 공간 -->
		<span class="spacer"></span>

		<!-- 다음 / 마지막 -->
		<c:set var="nextPage" value="${searchVO.page + 1}" />
		<c:if test="${nextPage > searchVO.totPage}">
		    <c:set var="nextPage" value="${searchVO.totPage}" />
		</c:if>
		<a class="nav <c:if test='${searchVO.page == searchVO.totPage}'>disabled</c:if>'" href="list?page=${nextPage}">다음</a>
		<a class="nav <c:if test='${searchVO.page == searchVO.totPage}'>disabled</c:if>'" href="list?page=${searchVO.totPage}">마지막</a>
	</div>
</div>

<!-- 테이블 출력 (기존 스타일 유지) -->
<table>
	<tr>
		<td>번호</td>
		<td>이름</td>
		<td>제목</td>
		<td>날자</td>
		<td>조회수</td>
	</tr>
	<c:forEach items="${list}" var="dto">
	<tr>
		<td>${dto.bid}</td>
		<td>${dto.bname}</td>
		<td>
			<c:set value="${dto.bindent}" var="endIndent" />
			<c:forEach begin="1" end="${dto.bindent}" var="cnt">
				<c:if test="${cnt ne 1}">&nbsp;&nbsp;</c:if>
				<c:if test="${cnt eq endIndent}">
					<img src="/static/images/arrow-right.gif" alt="" />
				</c:if>
			</c:forEach>
			<a href="content_view?bid=${dto.bid}">${dto.btitle}</a>
		</td>
		<td>${dto.bdate}</td>
		<td>${dto.bhit}</td>
	</tr>
	</c:forEach>
</table>

<!-- 글쓰기 버튼 (기본 스타일 유지) -->
<button type="button" onclick="location.href='write_view'">글작성</button>
</body>
<script>
    const urlParams = new URLSearchParams(window.location.search);
    const result = urlParams.get('result');
    const toast = document.getElementById('toast');

    // 메시지와 스타일을 매핑
    const toastMap = {
        write_success:   { message: '글 작성 성공!', type: 'success' },
        write_failure:   { message: '글 작성 실패!', type: 'failure' },
        delete_success:  { message: '글 삭제 성공!', type: 'success' },
        delete_failure:  { message: '글 삭제 실패!', type: 'failure' },
        reply_success:   { message: '답변 작성 성공!', type: 'success' },
        reply_failure:   { message: '답변 작성 실패!', type: 'failure' },
        modify_success:  { message: '글 수정 성공!', type: 'success' },
        modify_failure:  { message: '글 수정 실패!', type: 'failure' }
    };

    // 해당 result 값이 toastMap에 존재할 경우만 표시
    if (toastMap[result]) {
        const { message, type } = toastMap[result];

        toast.textContent = message;
        toast.className = type;
        toast.style.display = 'block';

        setTimeout(() => {
            toast.style.opacity = '1';
        }, 50);

        setTimeout(() => {
            toast.style.opacity = '0';
            setTimeout(() => {
                toast.style.display = 'none';
            }, 500);
        }, 3000);
    }
    
    function clearVal(){
		let inputVal = document.getElementById("q");
		inputVal.value = "";
	}
</script>
<style>
body{
	margin-top: 4rem;
}
#toast {
	position: fixed;
	top: 20px;
	left: 50%;
	transform: translateX(-50%);
	color: #fff;
	padding: 14px 32px; /* 패딩 늘려서 가로 길이 확장 */
	min-width: 280px;   /* 최소 너비 */
	max-width: 90%;     /* 화면 너무 좁을 경우 대비 */
	border-radius: 6px;
	font-size: 16px;
	font-weight: bold;
	display: none;
	opacity: 0;
	transition: opacity 0.5s ease-in-out;
	z-index: 1000;
	text-align: center;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
#toast.success {
    background-color: #01AA33;
}
#toast.failure {
    background-color: #F1143F;
}
.pagination-wrapper {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin: 1rem;
}
.pagination-controls {
	display: flex;
	justify-content: center;
	gap: 8px; /* 버튼들 간 간격 넓힘 */
	flex-wrap: wrap;
}
.pagination-controls a,
.pagination-controls span {
	border: 1px solid #0d47a1;
	border-radius: 6px;
	padding: 4px 10px;
	color: #0d47a1;
	text-decoration: none;
	font-size: 0.9rem;
}
.pagination-controls a:hover {
	background-color: #1266e2;
}
.pagination-controls .nav {
	background-color: #0d47a1;
	color: white;
	font-weight: bold;
}
.pagination-controls .disabled {
	pointer-events: none;
	background-color: #ccc;
	color: white;
	border-color: #ccc;
}
.pagination-controls .current {
	background-color: #0d47a1;
	color: white;
	font-weight: bold;
}
.pagination-controls .spacer {
	display: inline-block;
	border:none;
	width: 10px;
}
.page-info-bar {
	margin-bottom: 2rem;
	display: flex;
	justify-content: space-between;
	width: 100%;
}
.page-info-bar h1 {
	margin: 0;
}
.page-info-bar .info {
	text-align: right;
	font-size: 0.9rem;
}
.search-form {
	display: flex;
	align-items: center;
	justify-content: flex-end;
	gap: 8px;
	margin-bottom: 1rem;
}
.search-input {
	font-size: 1.2rem;
	padding: 2px 6px;
}
.search-button {
	height: 2rem;
	font-size: 1rem;
	padding: 0 10px;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
}
</style>
</html>
