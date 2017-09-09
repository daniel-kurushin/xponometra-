<?php
function process_upload($post)
{
	var_dump($post);
}

function upload_form()
{
	return '
<form method="post">
	<select name="target">
		<option value="ORGS">Оргструктура</option>
		<option value="OPER">Операции</option>
		<option value="FACT">Факторы</option>
	</select>
	<input type="file" name="file">
	<input type="submit" name="submit" value="Ok">
</form>';
}

if(isset($_POST['submit']))
{
	echo process_upload($_POST);
}
else
{
	echo upload_form();
}
?>
