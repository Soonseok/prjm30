<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
<title>content_view</title>
</head>
<body>
	<h1>content_view</h1>
	<table width="100%">
		<tr>
			<td>번호</td>
			<td>${content_view.bid }</td>
		</tr>
		<tr>
			<td>이름</td>
			<td>${content_view.bname }</td>
		</tr>
		<tr>
			<td>제목</td>
			<td>${content_view.btitle }</td>
		</tr>
		<tr>
			<td>조회수</td>
			<td>${content_view.bhit }</td>
		</tr>
	</table>
	<fieldset>
		<legend>내용</legend>
		${content_view.bcontent }
		<c:if test="${not empty imgList }">
			<br /><br /><br />
			<fieldset>
				<legend>첨부 파일 목록</legend>
				<c:forEach items="${imgList }" var="imgdto">
					<a href="download?f=${imgdto.rebchgfile }&bid=${content_view.bid}">${imgdto.rebchgfile }</a> <br />
				</c:forEach>
			</fieldset>
		</c:if>
	</fieldset>
	<button type="button" onclick="location.href='modify_view?bid=${content_view.bid}'">수정</button>
	<button type="button" onclick="location.href='reply_view?bid=${content_view.bid }'">답변</button>
	<button type="button" onclick="location.href='list'">목록</button>
	<button type="button" onclick="if(confirm('정말 삭제하시겠습니까?')) location.href='delete?bid=${content_view.bid }&bgroup=${content_view.bgroup }';" id="deleteBtn">삭제</button>
</body>
<style>
#deleteBtn {
		margin-top: 1rem;
		background-color: rgb(255, 0, 0);
		border-color: rgb(255, 0, 0);
	}
#deleteBtn:hover {
		background-color: rgb(255, 100, 100);
		border-color: rgb(255, 100, 100);
	}
td:first-child {
		width: 120px;
		font-weight: bold;
	}
td {
		padding: 0.5rem;
		vertical-align: top;
	}
</style>
</html>