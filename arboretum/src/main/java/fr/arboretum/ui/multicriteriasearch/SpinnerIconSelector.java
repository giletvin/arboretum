package fr.arboretum.ui.multicriteriasearch;

import fr.arboretum.R;

/**
 * The Class SpinnerIconSelector. This is a helper that allows to display icons
 * in the spinners.
 */
public class SpinnerIconSelector {
	// TODO : c'est ici qu'on déclare les icones des menus déroulants
	/**
	 * INSERT INTO type_feuille(id,name,lang) VALUES(0,"Composées",'fr'); INSERT
	 * INTO type_feuille(id,name,lang) VALUES(1,"Simples",'fr');
	 * 
	 * @param id
	 * @return
	 */
	public static int getIconResourceIdFromLeafTypeId(final int id) {

		int resourceId = 0;

		switch (id) {
		case 0:
			resourceId = R.drawable.type_feuille_composee;
			break;
		case 1:
			resourceId = R.drawable.type_feuille_entiere;
			break;

		default:
			resourceId = R.drawable.tous;
			break;
		}
		return resourceId;
	}

	/**
	 * INSERT INTO disposition_feuille(id,name,lang) VALUES(0,"Alternées",'fr');
	 * INSERT INTO disposition_feuille(id,name,lang) VALUES(1,"Opposées",'fr');
	 * INSERT INTO disposition_feuille(id,name,lang)
	 * VALUES(2,"Verticillées",'fr');
	 * 
	 * @param id
	 * @return
	 */
	public static int getIconResourceIdFromLeafDispositionId(final int id) {

		int resourceId = 0;

		switch (id) {
		case 0:
			resourceId = R.drawable.disposition_feuille_alterne;
			break;
		case 1:
			resourceId = R.drawable.disposition_feuille_opposes;
			break;
		case 2:
			resourceId = R.drawable.disposition_feuille_verticillee;
			break;

		default:
			resourceId = R.drawable.tous;
			break;
		}
		return resourceId;
	}

}
