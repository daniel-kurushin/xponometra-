<?php
$key = $_POST['key'];
if ($key == md5("frvhost".$_SERVER['REMOTE_ADDR']))
{
	$str = $_POST['str'];
	$trg = $_POST['trg'];

	$file = fopen("./frv/_".$trg, "a+");
	fwrite($file, "$str\n");
	fclose($file);
	echo "Ok\n";
} else {
	echo "error\n";
}

?>
