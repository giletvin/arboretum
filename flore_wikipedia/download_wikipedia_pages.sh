#!/bin/sh


export DATABASE_NAME=../flore_database/flore.jpg
export WIKIPEDIA_ROOT_DIR=wikipedia
export WIKIPEDIA_ROOT_DIR_FR=$WIKIPEDIA_ROOT_DIR/fr/
export WIKIPEDIA_ROOT_DIR_EN=$WIKIPEDIA_ROOT_DIR/en/
rm -rf $WIKIPEDIA_ROOT_DIR
rm *.tmp
mkdir -p $WIKIPEDIA_ROOT_DIR_FR
mkdir -p $WIKIPEDIA_ROOT_DIR_EN


#echo 'select scientific_name from fleur;'|sqlite3 $DATABASE_NAME >names.txt
echo 'Leucanthemum vulgare' > names.txt

while read line
do
	scientific_name=`echo $line | sed 's/\ /_/g'`
	rm $scientific_name*
	echo $scientific_name
	wget -k http://fr.wikipedia.org/wiki/$scientific_name
	php transform_wikipedia_file.php $scientific_name fr > $scientific_name.tmp
	echo ""> $scientific_name
	#extraire la ligne a partir de laquelle on peut tailler
	final_line=`grep -n 'Menu de navigation' $scientific_name.tmp | cut -d ":" -f1`
	i=0
	#recopier chaque ligne du fichier telecharge jusqu'à la final_line
	while read wikipedia_line
	do
		i=`expr $i + 1`
		if [ "$i" -lt "$final_line" ];then
			echo $wikipedia_line >> $scientific_name 
		fi
	done < $scientific_name.tmp
	rm $scientific_name.tmp
	#fermer proprement la balise html
	echo "</html>" >> $scientific_name 
	mv $scientific_name $WIKIPEDIA_ROOT_DIR_FR

	#ENGLISH
	rm $scientific_name*
	echo $scientific_name
	wget -k http://en.wikipedia.org/wiki/$scientific_name
	php transform_wikipedia_file.php $scientific_name en > $scientific_name.tmp
	echo ""> $scientific_name
	#extraire la ligne a partir de laquelle on peut tailler
	final_line=`grep -n 'Navigation menu' $scientific_name.tmp | cut -d ":" -f1`
	i=0
	#recopier chaque ligne du fichier telecharge jusqu'à la final_line
	while read wikipedia_line
	do
		i=`expr $i + 1`
		if [ "$i" -lt "$final_line" ];then
			echo $wikipedia_line >> $scientific_name 
		fi
	done < $scientific_name.tmp
	rm $scientific_name.tmp
	#fermer proprement la balise html
	echo "</html>" >> $scientific_name 
	mv $scientific_name $WIKIPEDIA_ROOT_DIR_EN

	
done < names.txt

