<?php

/**
* suppression des caractères accentués d'une string
* @param $str string
* @return string sans les caractères accentués
*/
function removeDiacritics($str) {
	return str_replace(array (
		"à",
		"â",
		"ä",
		"å",
		"ã",
		"á",
		"Â",
		"Ä",
		"À",
		"Å",
		"Ã",
		"Á",
		"æ",
		"Æ",
		"ç",
		"Ç",
		"é",
		"è",
		"ê",
		"ë",
		"É",
		"Ê",
		"Ë",
		"È",
		"ï",
		"î",
		"ì",
		"í",
		"Ï",
		"Î",
		"Ì",
		"Í",
		"ñ",
		"Ñ",
		"ö",
		"ô",
		"ó",
		"ò",
		"õ",
		"Ó",
		"Ô",
		"Ö",
		"Ò",
		"Õ",
		"ù",
		"û",
		"ü",
		"ú",
		"Ü",
		"Û",
		"Ù",
		"Ú",
		"ý",
		"ÿ",
		"Ÿ"
	), array (
		"a",
		"a",
		"a",
		"a",
		"a",
		"a",
		"A",
		"A",
		"A",
		"A",
		"A",
		"A",
		"a",
		"A",
		"c",
		"C",
		"e",
		"e",
		"e",
		"e",
		"E",
		"E",
		"E",
		"E",
		"i",
		"i",
		"i",
		"i",
		"I",
		"I",
		"I",
		"I",
		"n",
		"N",
		"o",
		"o",
		"o",
		"o",
		"o",
		"O",
		"O",
		"O",
		"O",
		"O",
		"u",
		"u",
		"u",
		"u",
		"U",
		"U",
		"U",
		"U",
		"y",
		"y",
		"Y"
	), $str);
}



/*
* renvoie une string en minuscule avec la premier lettre en maj
*/
function normalizeString($string){
	return ucfirst(strtolower(trim($string)));
}

/**
* nettoyage du nom français de l'arbre pour créer le répertoire correspondant.
* le répertoire de l'arbre est le nom francais sans les caractères accentués, sans apostrophe et sans espace, remplacés par des _
*/
function nettoie_nom($string){
	return trim(strtolower(str_replace(array (
		" ",
		"'"),array("_","_"),removeDiacritics($string))));

}


/*
* Collecte les données référentielles dans le tab array_data
* @param array_data : tableau des données référentielles
* @data : cellule du fichier csv qu'on analyse
* @return : un tableau contenant les indexes des données pour générer la requête SQL d'insert
*/
function collectData(&$array_data,$data){
	$indexes = array();
	if ($data!=''){
		$data_tab = explode(";", $data);

		foreach ($data_tab as $key => $value){
			$value=	normalizeString($value);
			if (!in_array($value,$array_data)){
				array_push($array_data,$value);
			}
			array_push($indexes,array_search($value,$array_data));
		}
		
	}
	return $indexes;
}

/*
* MAIN
*/


$array_scientific_family = array();
$array_type_aiguille = array();
$array_type_fruit_conifere = array();
$array_type_feuille = array();
$array_bord_feuille = array();
$array_forme_feuille = array();
$array_caracteristique_feuille = array();
$array_fruit = array();
$array_fruit_conifere= array();

$nom_fr_1_index=0;
$nom_fr_2_index=1;

$nom_scientifique_1_index=2;
$nom_scientifique_2_index=21;
$nom_en_index=3;
$famille_index=4;
$is_arbuste_index=5;
$is_conifere_index=6;
$type_aiguille_index=7;
$type_fruit_conifere_index=8;
$type_feuille_index=9;
$bord_feuille_index=10;
$forme_feuille_index=11;
$caracteristique_feuille_index=12;
$fruit_index=13;
$fruit2_index=20;


$idArbre=0;
$sql_queries = array();
if (($handle = fopen("Arboretum.csv", "r")) !== FALSE) {
    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
	if ($idArbre>0){
		//attention : si on ajoute une colonne dans le csv, changer ce test !
		if (count($data)==23&&$data[$nom_scientifique_1_index]!=''){
			$indexes_scientific_family= collectData($array_scientific_family,$data[$famille_index]);
			//gestion des conifères
			if ($data[$type_aiguille_index]!=''&&$data[$type_fruit_conifere_index]!=''){
				$indexes_type_aiguille=collectData($array_type_aiguille,$data[$type_aiguille_index]);
				$indexes_fruit_conifere=collectData($array_fruit_conifere,$data[$type_fruit_conifere_index]);
			}
			else{
				//pas un conifère
				$indexes_fruit_conifere=array();
				array_push($indexes_fruit_conifere,-1);
				$indexes_type_aiguille=array();
				array_push($indexes_type_aiguille,-1);
			}
			$indexes_type_feuille=collectData($array_type_feuille,$data[$type_feuille_index]);
			$indexes_bord_feuille=collectData($array_bord_feuille,$data[$bord_feuille_index]);
			$indexes_forme_feuille=collectData($array_forme_feuille,$data[$forme_feuille_index]);
			$indexes_caracteristique_feuille=collectData($array_caracteristique_feuille,$data[$caracteristique_feuille_index]);


			//collecter les donnees sur deux colonnes distinctes pour les fruits
			$indexes_fruit=collectData($array_fruit,$data[$fruit_index]);
			$indexes_fruit2=collectData($array_fruit,$data[$fruit2_index]);

			$is_conifere=(trim($data[$is_arbuste_index])=='conifère' ? 1:0);
			$is_arbuste=(trim($data[$is_arbuste_index])=='arbuste' ? 1:0);


			array_push($sql_queries,"insert into arbre (id,directory_name,scientific_name,scientific_family_fk,is_arbuste,is_conifere,type_aiguille_fk,type_fruit_conifere_fk) values (".$idArbre.",\"".nettoie_nom(trim($data[$nom_fr_1_index]))."\",\"".trim($data[$nom_scientifique_1_index])."\",".$indexes_scientific_family[0].",".$is_arbuste.",".$is_conifere.",".$indexes_type_aiguille[0].",".$indexes_fruit_conifere[0].");");
			//langues
			array_push($sql_queries,"insert into taxonomy (lang, taxon, searched_taxon, arbre_fk, taxon_usuel) values (\"fr\",\"".trim($data[$nom_fr_1_index])."\",\"".strtolower(removeDiacritics(trim($data[$nom_fr_1_index])))."\",".$idArbre.",1);");

			//$nom fr 2 : multivalué!
			if ($data[$nom_fr_2_index]!=''){
				$data_tab = explode(",", $data[$nom_fr_2_index]);
				foreach ($data_tab as $key => $value){
					array_push($sql_queries,"insert into taxonomy (lang, taxon, searched_taxon, arbre_fk, taxon_usuel) values (\"fr\",\"".$value."\",\"".strtolower(removeDiacritics(trim($value)))."\",".$idArbre.",0);");
				}
			}

			//nom scientifique 1
			array_push($sql_queries,"insert into taxonomy (lang, taxon, searched_taxon, arbre_fk, taxon_usuel) values (\"la\",\"".trim($data[$nom_scientifique_1_index])."\",\"".strtolower(removeDiacritics(trim($data[$nom_scientifique_1_index])))."\",".$idArbre.",1);");

			//nom scientifique 2
			if ($data[$nom_scientifique_2_index]!=''){
				array_push($sql_queries,"insert into taxonomy (lang, taxon, searched_taxon, arbre_fk, taxon_usuel) values (\"la\",\"".trim($data[$nom_scientifique_2_index])."\",\"".strtolower(removeDiacritics(trim($data[$nom_scientifique_2_index])))."\",".$idArbre.",0);");
			}

			//nom anglais
			if ($data[$nom_en_index]!=''){
				array_push($sql_queries,"insert into taxonomy (lang, taxon, searched_taxon, arbre_fk, taxon_usuel) values (\"en\",\"".trim($data[$nom_en_index])."\",\"".strtolower(removeDiacritics(trim($data[$nom_en_index])))."\",".$idArbre.",1);");
			}

			//type_feuille
			if ($data[$type_feuille_index]!=''){
				foreach ($indexes_type_feuille as $key => $value){
					array_push($sql_queries,"insert into arbre_type_feuille (arbre_fk, type_feuille_fk) values (".$idArbre.",".$value.");");
				}
			}

			//bord_feuille
			if ($data[$bord_feuille_index]!=''){
				foreach ($indexes_bord_feuille as $key => $value){
					array_push($sql_queries,"insert into arbre_bord_feuille (arbre_fk, bord_feuille_fk) values (".$idArbre.",".$value.");");
				}
			}

			//forme_feuille
			if ($data[$forme_feuille_index]!=''){
				foreach ($indexes_forme_feuille as $key => $value){
					array_push($sql_queries,"insert into arbre_forme_feuille (arbre_fk, forme_feuille_fk) values (".$idArbre.",".$value.");");
				}
			}

			//caracteristique_feuille
			//multivalué!
			if ($data[$caracteristique_feuille_index]!=''){
				foreach ($indexes_caracteristique_feuille as $key => $value){
					array_push($sql_queries,"insert into arbre_caracteristique_feuille (arbre_fk, caracteristique_feuille_fk) values (".$idArbre.",".$value.");");
				}
			}


			//fruit
			if ($data[$fruit_index]!=''){
				foreach ($indexes_fruit as $key => $value){
					array_push($sql_queries,"insert into arbre_fruit (arbre_fk, fruit_fk) values (".$idArbre.",".$value.");");
				}
			}
		

		}
		else {
			echo "erreur sur la ligne : ".$idArbre. " ". count($data) . "\n";
		}
	}

	$idArbre++;
    }
    fclose($handle);
}

///////////////////////////////////
//ECRITURE DES DONNEES REF
/*
$array_scientific_family = array();
$array_type_aiguille = array();
$array_type_fruit_conifere = array();
$array_type_feuille = array();
$array_bord_feuille = array();
$array_forme_feuille = array();
$array_caracteristique_feuille = array();
$array_fruit = array();
*/
$handlerTableReferentiel = fopen('generate_insert_data_table_referentiel.sql', 'w');

foreach ($array_scientific_family as $key => $value){
	$sql_insert_query = "INSERT INTO  scientific_family(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}


foreach ($array_type_aiguille as $key => $value){
	$sql_insert_query = "INSERT INTO type_aiguille(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}


foreach ($array_type_fruit_conifere as $key => $value){
	$sql_insert_query = "INSERT INTO type_fruit_conifere(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}

foreach ($array_type_feuille as $key => $value){
	$sql_insert_query = "INSERT INTO type_feuille(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}

foreach ($array_bord_feuille as $key => $value){
	$sql_insert_query = "INSERT INTO bord_feuille(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}

foreach ($array_forme_feuille as $key => $value){
	$sql_insert_query = "INSERT INTO forme_feuille(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}
foreach ($array_caracteristique_feuille as $key => $value){
	$sql_insert_query = "INSERT INTO caracteristique_feuille(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}
foreach ($array_fruit as $key => $value){
	$sql_insert_query = "INSERT INTO fruit(id,name,lang) VALUES(".$key.",\"".$value."\",'fr');\n";
	fwrite($handlerTableReferentiel, $sql_insert_query);
}

fclose($handlerTableReferentiel);


///////////////////////////////////
//ECRITURE DES DONNEES arbreS
//print_r($sql_queries);
$handlerTablearbres = fopen('generate_insert_data_table_arbres.sql', 'w');
foreach ($sql_queries as $key => $value){
	fwrite($handlerTablearbres, $value."\n");
}


fclose($handlerTablearbres);





?>
