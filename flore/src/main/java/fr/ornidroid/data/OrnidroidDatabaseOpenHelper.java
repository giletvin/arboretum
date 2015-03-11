package fr.ornidroid.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.ornidroid.helper.BasicConstants;
import fr.ornidroid.helper.Constants;
import fr.ornidroid.helper.IOHelper;
import fr.ornidroid.helper.OrnidroidError;
import fr.ornidroid.helper.OrnidroidException;

/**
 * Pompé sur
 * http://www.reigndesign.com/blog/using-your-own-sqlite-database-in-android
 * -applications/
 * 
 * @author hamlet
 * 
 */
public class OrnidroidDatabaseOpenHelper extends SQLiteOpenHelper {

	/** The already checked. */
	private boolean alreadyChecked = false;

	/** The my context. */
	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 *            the context
	 */
	public OrnidroidDatabaseOpenHelper(final Context context) {

		super(context, Constants.DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates the db if necessary.
	 * 
	 * @throws OrnidroidException
	 *             the ornidroid exception
	 */
	public void createDbIfNecessary() throws OrnidroidException {
		if (!this.alreadyChecked) {
			try {
				final boolean dbUptodate = checkDataBase();

				if (dbUptodate) {
					// do nothing - database already exist
				} else {

					// By calling this method and empty database will be created
					// into
					// the default system path
					// of your application so we are gonna be able to overwrite
					// that
					// database with our database.
					this.getReadableDatabase();
					copyDataBase();
				}
			} catch (final IOException e) {
				Log.e(BasicConstants.LOG_TAG, e.getMessage(), e);
				throw new OrnidroidException(OrnidroidError.DATABASE_NOT_FOUND,
						e);
			} catch (final SQLiteException e) {
				Log.e(BasicConstants.LOG_TAG, e.getMessage(), e);
				throw new OrnidroidException(OrnidroidError.DATABASE_NOT_FOUND,
						e);
			} finally {
				this.alreadyChecked = true;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
			final int newVersion) {

	}

	/**
	 * Check if the database already exists and is uptodate to avoid re-copying
	 * the file each time you open the application.
	 * 
	 * @return true if it exists and is uptodate, false if it doesn't
	 */
	private boolean checkDataBase() {

		final File dbFile = this.myContext.getDatabasePath(Constants.DB_NAME);

		if (!dbFile.exists()) {
			return false;
		}

		int countResults = 0;
		final SQLiteDatabase db = this.getReadableDatabase();
		try {
			StringBuffer query = new StringBuffer();
			query.append("select count(*) from release_notes where version_code=");
			query.append(Constants.getVersionCode());
			countResults = (int) DatabaseUtils.longForQuery(db,
					query.toString(), null);
		} catch (Exception e) {
			countResults = 0;
		} finally {
			db.close();
		}
		return countResults == 1;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * 
	 * @param outFileName
	 * @param myInput
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		final InputStream myInput = this.myContext.getAssets().open(
				Constants.DB_NAME);
		// Path to the just created empty db
		final File outFile = this.myContext.getDatabasePath(Constants.DB_NAME);

		// Open the empty db as the output stream
		final OutputStream myOutput = new FileOutputStream(outFile);
		IOHelper.copy(myInput, myOutput);
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

}