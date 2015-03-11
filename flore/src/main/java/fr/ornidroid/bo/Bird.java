package fr.ornidroid.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Class Bird.
 */
public class Bird extends SimpleBird {

	/** The category. */
	private String category;

	/** The description. */
	private String description;

	/** The distribution. */
	private String distribution;

	/** The habitat. */
	private String habitat;

	/** The oiseaux net url. */
	private String oiseauxNetUrl;

	/** The pictures. */
	private List<OrnidroidFile> pictures;

	/** The wikipedia page. */
	private OrnidroidFile wikipediaPage;

	/**
	 * Gets the wikipedia page.
	 * 
	 * @return the wikipedia page
	 */
	public OrnidroidFile getWikipediaPage() {
		return wikipediaPage;
	}

	/**
	 * Sets the wikipedia page.
	 * 
	 * @param wikipediaPage
	 *            the new wikipedia page
	 */
	public void setWikipediaPage(OrnidroidFile wikipediaPage) {
		this.wikipediaPage = wikipediaPage;
	}

	/** The scientific family. */
	private String scientificFamily;

	/** The scientific name2. */
	private String scientificName2;

	/** The scientific order. */
	private String scientificOrder;

	/** The size. */
	private String size;

	/** The sounds. */
	private List<OrnidroidFile> sounds;

	/**
	 * Instantiates a new bird.
	 */
	protected Bird() {
		super();
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the distribution.
	 * 
	 * @return the distribution
	 */
	public String getDistribution() {
		return this.distribution;
	}

	/**
	 * Gets the habitat.
	 * 
	 * @return the habitat
	 */
	public String getHabitat() {
		return this.habitat;
	}

	/**
	 * Gets the list of the properties of the audio files. This is to be
	 * displayed in the screen.
	 * 
	 * @return the list containing maps of properties of the audio file. One map
	 *         per audio file.
	 * @see AudioOrnidroidFile AudioOrnidroidFile the keys of the map are
	 *      defined in this class
	 */
	public List<Map<String, String>> getListAudioFiles() {
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (null != this.sounds) {
			for (final OrnidroidFile audioFile : this.sounds) {
				final AudioOrnidroidFile ornidroidAudioFile = (AudioOrnidroidFile) audioFile;
				list.add(ornidroidAudioFile.getPropertiesForScreen());
			}
		}
		return list;
	}

	/**
	 * Gets the number of pictures.
	 * 
	 * @return the number of pictures, 0 if list of pictures is null
	 */
	public int getNumberOfPictures() {
		if (this.pictures != null) {
			return this.pictures.size();
		}
		return 0;
	}

	/**
	 * Gets the number of sounds.
	 * 
	 * @return the number of sounds
	 */
	public int getNumberOfSounds() {
		if (this.sounds != null) {
			return this.sounds.size();
		}
		return 0;
	}

	/**
	 * Gets the oiseaux net url.
	 * 
	 * @return the oiseaux net url
	 */
	public String getOiseauxNetUrl() {
		return this.oiseauxNetUrl;
	}

	/**
	 * Gets the picture.
	 * 
	 * @param pictureNumber
	 *            the picture number
	 * @return the picture
	 */
	public OrnidroidFile getPicture(final int pictureNumber) {
		if ((this.pictures != null) && (this.pictures.size() > pictureNumber)) {
			return this.pictures.get(pictureNumber);
		} else {
			return null;
		}
	}

	/**
	 * Gets the list of pictures.
	 * 
	 * @return the pictures.
	 */
	public List<OrnidroidFile> getPictures() {
		// if (pictures == null) {
		// setPictures(new ArrayList<AbstractOrnidroidFile>());
		// }
		return this.pictures;
	}

	/**
	 * Gets the scientific family.
	 * 
	 * @return the scientific family
	 */
	public String getScientificFamily() {
		return this.scientificFamily;
	}

	/**
	 * Gets the scientific name2.
	 * 
	 * @return the scientific name2
	 */
	public String getScientificName2() {
		return this.scientificName2;
	}

	/**
	 * Gets the scientific order.
	 * 
	 * @return the scientific order
	 */
	public String getScientificOrder() {
		return this.scientificOrder;
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public String getSize() {
		return this.size;
	}

	/**
	 * Gets the sound.
	 * 
	 * @param soundNumber
	 *            the sound number
	 * @return the sound, can be null if soundNumber is not valid
	 */
	public OrnidroidFile getSound(final int soundNumber) {
		if ((this.sounds != null) && (this.sounds.size() > soundNumber)) {
			return this.sounds.get(soundNumber);
		} else {
			return null;
		}
	}

	/**
	 * Gets the sounds.
	 * 
	 * @return the sounds
	 */
	public List<OrnidroidFile> getSounds() {
		return this.sounds;
	}

	/**
	 * Sets the distribution.
	 * 
	 * @param distribution
	 *            the new distribution
	 */
	public void setDistribution(final String distribution) {
		this.distribution = distribution;
	}

	/**
	 * Sets the habitat.
	 * 
	 * @param habitat
	 *            the new habitat
	 */
	public void setHabitat(final String habitat) {
		this.habitat = habitat;
	}

	/**
	 * Sets the pictures.
	 * 
	 * @param pictures
	 *            the new pictures
	 */
	public void setPictures(final List<OrnidroidFile> pictures) {
		this.pictures = pictures;
	}

	/**
	 * Sets the scientific family.
	 * 
	 * @param scientificFamily
	 *            the new scientific family
	 */
	public void setScientificFamily(final String scientificFamily) {
		this.scientificFamily = scientificFamily;
	}

	/**
	 * Sets the scientific name2.
	 * 
	 * @param scientificName2
	 *            the new scientific name2
	 */
	public void setScientificName2(final String scientificName2) {
		this.scientificName2 = scientificName2;
	}

	/**
	 * Sets the scientific order.
	 * 
	 * @param scientificOrder
	 *            the new scientific order
	 */
	public void setScientificOrder(final String scientificOrder) {
		this.scientificOrder = scientificOrder;
	}

	/**
	 * Sets the sounds.
	 * 
	 * @param sounds
	 *            the new sounds
	 */
	public void setSounds(final List<OrnidroidFile> sounds) {
		this.sounds = sounds;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 */
	protected void setCategory(final String category) {
		this.category = category;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	protected void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets the oiseaux net url.
	 * 
	 * @param oiseauxNetUrl
	 *            the new oiseaux net url
	 */
	protected void setOiseauxNetUrl(final String oiseauxNetUrl) {
		this.oiseauxNetUrl = oiseauxNetUrl;
	}

	/**
	 * Sets the size.
	 * 
	 * @param size
	 *            the new size
	 */
	protected void setSize(final String size) {
		this.size = size;
	}

}
