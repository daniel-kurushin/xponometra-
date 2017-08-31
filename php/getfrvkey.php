<?php
	$key = md5("frvhost".$_SERVER['REMOTE_ADDR']);
	echo $key;
?>
