<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="https://cdn.simplecss.org/simple.min.css">
<title>write_view</title>
</head>
<body>
<h1>write_view</h1>
<fieldset>
<form action="write" method="post" enctype="multipart/form-data">
	제목 : <input type="text" name="btitle" id="btitle" class="row"/> <br />
	이름 : <input type="text" name="bname" id="bname" class="row"/> <br />
	첨부 : <input multiple type="file" name="file" size="50"/> <br />
	내용 : <textarea name="bcontent" id="bcontent" cols="30" rows="10" class="row"></textarea>
<input type="submit" value="작성" class="sendButton"/>
</form>
<button onclick="location.href='list'" class="sendButton">취소</button>
</fieldset>
</body>
<style>
	  body{
	  	justify-items: center;
	  	margin: 5%;
	  }
	  fieldset {
	    width: 40vw;
	    padding: 16px;
	    border: 2px solid #ccc;
	    border-radius: 8px;
	  }
	  legend {
	    font-weight: bold;
	    padding: 0 8px;
	  }
	  .row {
	    display: flex;
	    justify-content: flex-start;
	    margin-bottom: 8px;
	    width: 100%;
	  }
	  .label {
	    width: 80px;
	    font-weight: bold;
	  }
	  .value {
	    flex: 1;
	  }
	  .sendButton{
	  	width: 100%;
	  }
	</style>
</html>