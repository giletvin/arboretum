#!/bin/sh
# script de creation de base de données sqlite a partir du fichier csv. Le separateur du csv est |
#pre requis sqlite3 php5-sqlite


#1 lecture du CSV et creation d'un script SQL d'insertion de données
php create_insert_data_sql_file.php

export DATABASE_NAME=arboretum.jpg
rm $DATABASE_NAME
cat create_tables.sql | sqlite3 $DATABASE_NAME
cat generate_insert_data_table_referentiel.sql | sqlite3 $DATABASE_NAME
cat generate_insert_data_table_arbres.sql | sqlite3 $DATABASE_NAME


#commente pour l'instant car non adapte a arboretum
#cat update_libelles.sql | sqlite3 $DATABASE_NAME


sqlite3 $DATABASE_NAME 'VACUUM;'

sqlite3 $DATABASE_NAME 'ANALYZE;'

