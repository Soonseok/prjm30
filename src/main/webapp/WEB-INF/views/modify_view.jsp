<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
<title>modify_view</title>
</head>
<body>
<h1>modify_view</h1>
<form action="modify" method="post">
<input type="hidden" name="bid" value="${content_view.bid }" />
	<table width="100%">
	<tr>
		<td>번호</td>
		<td>${content_view.bid }</td>
	</tr>
	<tr>
		<td>조회수</td>
		<td>${content_view.bhit }</td>
	</tr>
	<tr>
		<td>이름</td>
		<td><input type="text" name="bname" value="${content_view.bname }" /></td>
	</tr>
	<tr>
		<td>제목</td>
		<td><textarea name="btitle" rows="1">${content_view.btitle }</textarea></td>
	</tr>
	</table>
	<fieldset>
		<legend>내용</legend>
		<textarea name="bcontent" rows="10">${content_view.bcontent }</textarea>
	</fieldset>
	<input type="submit" value="수정" class="sendButton"/>
	<button type="button" class="sendButton" onclick="location.href='content_view?bid=${content_view.bid }'">돌아가기</button>
	<button type="button" class="sendButton" onclick="" id="deleteBtn">삭제</button>
</form>
</body>
<style>
#deleteBtn{
		float: right;
		margin-top: 1rem;
		background-color: rgb(255, 0, 0);
		border-color: rgb(255,0,0);
	}
#deleteBtn:hover{
		background-color: rgb(255,100,100);
		border-color: rgb(255,100,100);
	}
.sendButton{
	  	width: 100%;
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