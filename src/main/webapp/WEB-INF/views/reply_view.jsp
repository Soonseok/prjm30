<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
<title>reply_view</title>
</head>
<body>
	<h1>reply_view</h1>
	<form action="reply" method="post">
		<input type="hidden" name="bid" value="${reply_view.bid }" />
		<input type="hidden" name="bgroup" value="${reply_view.bgroup }" />
		<input type="hidden" name="bstep" value="${reply_view.bstep }" />
		<input type="hidden" name="bindent" value="${reply_view.bindent }" />
		<table width="100%">
			<tr>
				<td>번호</td>
				<td>${reply_view.bid }</td>
			</tr>
			<tr>
				<td>조회수</td>
				<td>${reply_view.bhit }</td>
			</tr>
			<tr>
				<td>이름</td>
				<td><input type="text" name="bname" value="${reply_view.bname }" /></td>
			</tr>
			<tr>
				<td>제목</td>
				<td><textarea name="btitle" rows="1">${reply_view.btitle }</textarea></td>
			</tr>
		</table>
		<fieldset>
			<legend>내용</legend>
			<textarea name="bcontent" rows="10">${reply_view.bcontent }</textarea>
		</fieldset>
		<input type="submit" value="답변달기" class="sendButton"/>
		<button type="button" class="sendButton" onclick="location.href='content_view?bid=${reply_view.bid }'">돌아가기</button>
	</form>
</body>
<style>
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