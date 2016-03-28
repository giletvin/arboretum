-- creation de la base 
-- creation de la table taxonomy en jointure avec la table arbre


create table arbre(
	id integer,
	directory_name,
	scientific_name,
	scientific_family_fk,
	is_arbuste,
	is_conifere,
	type_aiguille_fk,
	type_fruit_conifere_fk,
	PRIMARY KEY(id)
);

CREATE VIRTUAL TABLE taxonomy USING fts3(lang, taxon, searched_taxon, arbre_fk,taxon_usuel);

create table scientific_family(
		id,
		name,
		lang
);


create table type_aiguille(
	id integer,
	name,
	lang
);


create table type_fruit_conifere(
	id integer,
	name,
	lang
);

create table type_feuille(
	id integer,
	name,
	lang
);


create table arbre_type_feuille(
	arbre_fk,
	type_feuille_fk
);

create table bord_feuille(
	id integer,
	name,
	lang
);


create table arbre_bord_feuille(
	arbre_fk,
	bord_feuille_fk
);

create table forme_feuille(
	id integer,
	name,
	lang
);


create table arbre_forme_feuille(
	arbre_fk,
	forme_feuille_fk
);



create table caracteristique_feuille(
	id integer,
	name,
	lang
);


create table arbre_caracteristique_feuille(
	arbre_fk,
	caracteristique_feuille_fk
);

create table fruit(
	id integer,
	name,
	lang
);


create table arbre_fruit(
	arbre_fk,
	fruit_fk
);


create table application_info(id integer,key,value,date,comments);
insert into application_info(id,key,value,date,comments) VALUES(1,"arboretum_version","1.0.0","","");


create table release_notes(id integer,version_code integer,date,comments_de,comments_en,comments_fr,read integer);

-- release 1.0.0 version 1
insert into release_notes(id,version_code,date,comments_en,comments_de,comments_fr,read) VALUES(1,1,"27/07/2015","","","",0);
update release_notes set comments_fr=" * Version 1.0.0 (27/07/2015)
Première version !
" where version_code=1;
update release_notes set comments_en=" * Version 1.0.0 (07/27/2015)
First release!
" where version_code=1;
