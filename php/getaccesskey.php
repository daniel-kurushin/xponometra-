<?php
error_reporting(0);
$key = substr($_GET['key'],0,8);
$bro = $_SERVER['HTTP_USER_AGENT'];
$ipa = $_SERVER['REMOTE_ADDR'];
$log = fopen("./log/$ipa.log", "a+");
fwrite($log,"$ipa, $key, $bro\n");
fclose($log);
echo "Ok";
?>
