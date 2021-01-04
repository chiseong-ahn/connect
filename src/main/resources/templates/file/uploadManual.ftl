<html>
	<body>
		<form action="/api/file/uploadManual" method="post" enctype="multipart/form-data">
			<input type="hidden" name="div" id="div" value="manual" />
			<input type="hidden" name="companyId" id="companyId" value="1" />
			<input type="hidden" name="manualIndex" id="manualIndex" value="1" />
			<input type="hidden" name="pageNo" id="pageNo" value="10" />
			<input type="file" name="file" id="file" />
			<input type="submit" />
		</form> 
	</body>
</html>