
<?php
// Соединение, выбор базы данных
$dbconn = pg_connect("host=localhost dbname=xponometra user=xponometrax password=gfhffvbylb'nbkfybkbycekmafn")
    or die('Could not connect: ' . pg_last_error());

pg_query('drop table PROJ;');
pg_query('drop table FRVH;');
pg_query('drop table FRVB;');
pg_query('drop table FACT;');
pg_query('drop table XPOH;');
pg_query('drop table OPER;');
pg_query('drop table PODR;');
pg_query('drop table DOLG;');

pg_query("SELECT * FROM PROJ;") or pg_query(
	"CREATE TABLE projects(
		id serial primary key,
		name varchar not null unique,
		comments varchar,
		p_start date not null,
		p_end date not null
	);"
) or die('Ошибка запроса: ' . pg_last_error());

pg_query("SELECT * FROM FRVH;") or pg_query(
	"create table FRVH (" .
		"id serial primary key, " .
		"date_time varchar, " .
		"n_head integer, " .
		"id_podr integer references PODR (id), " .
		"id_dolg integer references DOLG (id), " .
		"id_xpoh integer references XPOH (id), " .
		"sm_nom integer, " .
		"sm_dlit integer, " .
		"rab_gr_a integer, " .
		"rab_gr_b integer, " .
		"num_men integer, " .
		"worker_nik varchar, " .
		"constraint date_time_n_head_unique unique (date_time, n_head)" .
	");"
) or die('Ошибка запроса: ' . pg_last_error());

pg_query("SELECT * FROM FRVB;") or pg_query(
	"create table FRVB (" .
		"id serial primary key, " .
		"frvh_id integer references FRVH (id), " .
		"time varchar, " .
		"P3B_OTL_OP integer, " .
		"KO integer, " .
		"KP integer, " .
		"oper_id integer references OPER (id), " .
		"fact1_id integer references FACT (id), " .
		"fact2_id integer references FACT (id), " .
		"fact3_id integer references FACT (id), " .
		"comments varchar, " .
		"constraint frvb_unique unique (time, frvh_id)" .
	");"
) or die('Ошибка запроса: ' . pg_last_error());

pg_query("SELECT * FROM FACT;") or pg_query(
	"create table FACT (" .
		"id serial primary key, " .
		"name varchar, " .
		"vruc varchar, " .
		"date varchar, " .
		"xpoh varchar, " .
		"constraint fact_name_unique unique (name)" .
	");"
) or die('Ошибка запроса: ' . pg_last_error());

pg_query("SELECT * FROM XPOH;") or pg_query(
	"create table XPOH (" .
		"id serial primary key, " .
		"fio varchar, " .
		"comments varchar, " .
		"constraint xpoh_name_unique unique (fio)" .
	");"
) or die('Ошибка запроса: ' . pg_last_error());


pg_query("SELECT * FROM OPER;") or pg_query(
	"create table OPER (" .
		"id serial primary key, " .
		"name varchar, " .
		"vruc varchar, " .
		"date varchar, " .
		"xpoh varchar, " .
		"constraint oper_name_unique unique (name)" .
	");"
) or die('Ошибка запроса: ' . pg_last_error());


pg_query("SELECT * FROM PODR;") or pg_query(
	"create table PODR (" .
			"id serial primary key, " .
			"name varchar, " .
			"constraint podr_name_unique unique (name)" .
		");"
) or die('Ошибка запроса: ' . pg_last_error());


pg_query("SELECT * FROM DOLG;") or pg_query(
	"create table DOLG (" .
		"id serial primary key, " .
		"podr_id integer, " .
		"name varchar, " .
		"constraint dolg_name_unique unique (name)" .
	");"
) or die('Ошибка запроса: ' . pg_last_error());

pg_query("SELECT * FROM XPOH_2_PROJ")


pg_close($dbconn);
?>
