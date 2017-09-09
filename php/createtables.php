<?php
// Соединение, выбор базы данных
$dbconn = pg_connect("host=localhost dbname=xponometra user=xponometrax password=gfhffvbylb'nbkfybkbycekmafn")
    or die('Could not connect: ' . pg_last_error());

// pg_query('drop table PROJ cascade;');
// pg_query('drop table FRVH cascade;');
// pg_query('drop table FRVB cascade;');
// pg_query('drop table FACT cascade;');
// pg_query('drop table XPOH cascade;');
// pg_query('drop table OPER cascade;');
// pg_query('drop table PODR cascade;');
// pg_query('drop table DOLG cascade;');
// pg_query('drop table MD5S cascade;');

pg_query("SELECT * FROM PROJ;") or pg_query(
	"CREATE TABLE PROJ(
		id serial primary key,
		name varchar not null unique,
		comments varchar,
		p_start date not null,
		p_end date not null
	);"
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
		"P3B_OTL_OP varchar, " .
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

pg_query("SELECT * FROM MD5S;") or pg_query(
	"create table MD5S (" .
		"id varchar primary key, " .
		"str varchar, " .
		"time varchar " .
	");"
) or die('Ошибка запроса: ' . pg_last_error());


// pg_query("SELECT * FROM XPOH_2_PROJ")


pg_close($dbconn);
?>
