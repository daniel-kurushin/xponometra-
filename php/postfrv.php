<?php
$dbconn = pg_connect("host=localhost dbname=xponometra user=xponometrax password=gfhffvbylb'nbkfybkbycekmafn")
    or die('Could not connect: ' . pg_last_error());

function insert_or_get_xpoh($xpoh = 'test')
{
	pg_query_params("insert into XPOH (fio) values ($1)", array($xpoh));
	$result = pg_query_params("select id from XPOH where fio = $1", array($xpoh));
	if ($row = pg_fetch_row($result))
	{
		return $row[0];
	} else {
		return -1;
	}
}

function insert_or_get_oper($oper = 'test')
{
	if (stripos($oper)
	pg_query_params("insert into OPER (name) values ($1)", array($oper));
	$result = pg_query_params("select id from OPER where name = $1", array($oper));
	if ($row = pg_fetch_row($result))
	{
		return $row[0];
	} else {
		return -1;
	}
}

function insert_or_get_fact($fact = 'test')
{
	pg_query_params("insert into FACT (name) values ($1)", array($fact));
	$result = pg_query_params("select id from FACT where name = $1", array($fact));
	if ($row = pg_fetch_row($result))
	{
		return $row[0];
	} else {
		return -1;
	}
}

function insert_or_get_dolg($id_podr = 0, $dolg = 'test')
{
	pg_query_params("insert into DOLG (podr_id, name) values ($1, $2)", array($id_podr, $dolg));
	$result = pg_query_params("select id from DOLG where name = $1", array($dolg));
	if ($row = pg_fetch_row($result))
	{
		return $row[0];
	} else {
		return -1;
	}
}

function insert_or_get_podr($podr = 'test')
{
	pg_query_params("insert into PODR (name) values ($1)", array($podr));
	$result = pg_query_params("select id from PODR where name = $1", array($podr));
	if ($row = pg_fetch_row($result))
	{
		return $row[0];
	} else {
		return -1;
	}
}

function insert_or_get_frvh($date_time, $n_head, $id_podr, $id_dolg, $id_xpoh, $sm_nom, $sm_dlit, $rab_gr_a, $rab_gr_b, $num_men, $worker_nik)
{
	if($n_head == "") $n_head = 0;
	if($sm_nom == "") $sm_nom = 0;
	if($sm_dlit == "") $sm_dlit = 0;
	if($rab_gr_a == "") $rab_gr_a = 0;
	if($rab_gr_b == "") $rab_gr_b = 0;
	if($num_men == "") $num_men = 0;
	if($worker_nik == "") $worker_nik = 0;

	pg_query_params(
		"insert into FRVH (date_time, n_head, id_podr, id_dolg, id_xpoh, sm_nom, sm_dlit, rab_gr_a, rab_gr_b, num_men, worker_nik)
	     values ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11);",
		array($date_time, $n_head, $id_podr, $id_dolg, $id_xpoh, $sm_nom, $sm_dlit, $rab_gr_a, $rab_gr_b, $num_men, $worker_nik)
	);

	$result = pg_query_params("select id from FRVH where date_time = $1 and n_head = $2", array($date_time, $n_head));
	if ($row = pg_fetch_row($result))
	{
		return $row[0];
	} else {
		return -1;
	}
}

function insert_frvb($id_frvh, $optime, $P3B_OTL_OP, $KO, $KP, $id_oper, $id_fact1, $id_fact2, $id_fact3, $comments)
{
	pg_query_params(
		"insert into FRVB (frvh_id, time, P3B_OTL_OP, KO, KP, oper_id, fact1_id, fact2_id, fact3_id, comments)
		 values ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10);",
		array($id_frvh, $optime, $P3B_OTL_OP, $KO, $KP, $id_oper, $id_fact1, $id_fact2, $id_fact3, $comments)
	);
}

$key = $_POST['key'];
if ($key == md5("frvhost".$_SERVER['REMOTE_ADDR']))
{
	$str = $_POST['str'];
	$trg = $_POST['trg'];

	$result = pg_query_params(
		"insert into MD5S (id, str, time) values ($1, $2, $3)",
		array(sha1($str), $str, $trg)
	);
	if($result)
	{
		list($id, $date_time, $n_head, $podr, $dolg, $xpoh, $sm_nom, $sm_dlit, $rab_gr_a, $rab_gr_b, $num_men, $worker_nik, $x, $optime, $P3B_OTL_OP, $KO, $KP, $oper, $fact1, $fact2, $fact3, $comments) = explode('|', $str);
		$id_xpoh = insert_or_get_xpoh($xpoh);
		$id_podr = insert_or_get_podr($podr);
		$id_dolg = insert_or_get_dolg($id_podr, $dolg);
		$id_oper = insert_or_get_oper($oper);
		$id_fact1 = insert_or_get_fact($fact1);
		$id_fact2 = insert_or_get_fact($fact2);
		$id_fact3 = insert_or_get_fact($fact3);

		$id_frvh = insert_or_get_frvh($date_time, $n_head, $id_podr, $id_dolg, $id_xpoh, $sm_nom, $sm_dlit, $rab_gr_a, $rab_gr_b, $num_men, $worker_nik);
		insert_frvb($id_frvh, $optime, $P3B_OTL_OP, $KO, $KP, $id_oper, $id_fact1, $id_fact2, $id_fact3, $comments);
	}
	echo "Ok\n";
} else
{
	echo "error\n";
}

?>
