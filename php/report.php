<?php
$dbconn = pg_connect("host=localhost dbname=xponometra user=xponometrax password=gfhffvbylb'nbkfybkbycekmafn")
    or die('Could not connect: ' . pg_last_error());
$result = pg_query("
select
	frvh.date_time as date_time,
	frvh.n_head as n_head,
	(select name from podr where id = frvh.id_podr) as podr,
	(select name from dolg where id = frvh.id_dolg) as dolg,
	(select fio from xpoh where id = frvh.id_xpoh) as xpoh,
	frvh.sm_nom as sm_nom,
	frvh.sm_dlit as sm_dlit,
	frvh.rab_gr_a as rab_gr_a,
	frvh.rab_gr_b as rab_gr_b,
	frvh.num_men as num_men,
	frvh.worker_nik as worker_nik,
	frvb.time as time,
	frvb.P3B_OTL_OP as P3B_OTL_OP,
	frvb.KO as KO,
	frvb.KP as KP,
	(select name from oper where id = frvb.oper_id) as oper,
	(select name from fact where id = frvb.fact1_id) as fact1,
	(select name from fact where id = frvb.fact2_id) as fact2,
	(select name from fact where id = frvb.fact3_id) as fact3,
	frvb.comments as comments
from frvb, frvh
where frvb.frvh_id = frvh.id
order by frvh.date_time;
");

$rows  = array('date_time', 'n_head', 'podr', 'dolg', 'xpoh', 'sm_nom', 'sm_dlit', 'rab_gr_a', 'rab_gr_b', 'num_men', 'worker_nik', 'time', 'p3b_otl_op', 'ko', 'kp', 'oper', 'fact1', 'fact2', 'fact3', 'comments');
$rrows = array('Дата/время', 'Номер', 'Подразделение', 'Должность', 'Хронометражист', 'Номер смены', 'Длительность сменя', 'Сутки', 'Через', 'Номер чел', 'Работник', 'ВНО', 'П3B/ОТЛ/ОП', 'KO', 'KП', 'Операция', 'Фактор', 'Фактор', 'Фактор', 'Прим');
echo "<table border><tr>";
foreach ($rrows as $v)
{
	echo "<th>$v</th>";
}
echo "</tr>\n";
while ($row = pg_fetch_assoc($result))
{
	echo "<tr>";
	foreach ($rows as $v)
	{
		echo "<td>$row[$v]</td>";
	}
	echo "</tr>\n";
}
echo "</table>";
?>
