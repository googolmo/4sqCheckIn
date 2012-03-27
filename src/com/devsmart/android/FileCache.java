/* 
 * The MIT License
 * Copyright (c) 2011 Paul Soucy (paul@dev-smart.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.devsmart.android;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.devsmart.android.StringUtils;

;

public class FileCache implements Closeable {

	private static FileCache mSingleton;

	public static FileCache getFileCache(Context c) throws IOException {
		synchronized (FileCache.class) {
			if (mSingleton == null) {
				mSingleton = new FileCache(c);
			}
			return mSingleton;
		}
	}

	private class FileDBSQLHelper extends SQLiteOpenHelper {

		public static final String DB_NAME = "filecache.db";

		public FileDBSQLHelper(Context context) {
			super(context, DB_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL("CREATE TABLE IF NOT EXISTS "
						+ "fileCache ( key TEXT PRIMARY KEY, "
						+ "filename TEXT, " + "dateInsert INTEGER);");
			} catch (SQLException e) {
				Log.e(getClass().getName(), "Unable to create database", e);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	private static final String CACHE_DIR = "fileCache";
	private SQLiteDatabase mDB;
	private final File mCacheDir;

	private FileCache(Context context) throws IOException {
		mCacheDir = new File(context.getCacheDir(), CACHE_DIR);
		if (!mCacheDir.exists()) {
			if (!mCacheDir.mkdir()) {
				throw new IOException("Cannot create cache directory: "
						+ mCacheDir.getAbsolutePath());
			}
		}
		mDB = new FileDBSQLHelper(context).getWritableDatabase();

	}

	@Override
	public void close() {
		mDB.close();
	}

	private String createCacheFilename(String key) {
		String filenameHash = null;

		try {
			filenameHash = StringUtils.getSha1Hash(key);
			Cursor c = mDB.query("fileCache", new String[] { "filename" },
					"filename=?", new String[] { filenameHash }, null, null,
					null);
			try {
				if (c.getCount() > 0) {
					filenameHash = createCacheFilename(StringUtils
							.getSha1Hash(filenameHash));
				}
			} finally {
				c.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return filenameHash;
	}

	public String getCacheFilename(String key) {
		String retval = null;
		if (key != null) {
			Cursor c = mDB.query("fileCache", new String[] { "filename" },
					"key=?", new String[] { key }, null, null, null);
			try {
				if (c.moveToFirst()) {
					retval = c.getString(0);
				}

			} finally {
				c.close();
			}

		}
		return retval;
	}

	public synchronized void put(String key, InputStream is) {

		String cacheFilename = getCacheFilename(key);
		if (cacheFilename == null) {
			// new insert
			cacheFilename = createCacheFilename(key);
		}

		File cacheFile = getCacheFile(cacheFilename);

		try {
			if (!mCacheDir.exists()) {
				if (!mCacheDir.mkdir()) {

				}
			}
			if (!cacheFile.exists()) {
				cacheFile.createNewFile();
			}
			FileOutputStream os = new FileOutputStream(cacheFile);

			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, numRead);
			}
			os.close();

			insertEntry(key, cacheFilename);

		} catch (Exception e) {
			Log.e(getClass().getName(), "Error while putting", e);
		}

	}

	private void insertEntry(String key, String cacheFilename) {
		ContentValues values = new ContentValues();
		values.put("key", key);
		values.put("filename", cacheFilename);
		long insertTime = Long.valueOf(sDateFormat.format(new Date()));
		values.put("dateInsert", insertTime);
		mDB.replace("fileCache", null, values);
	}

	public class WriteAsync {
		protected OutputStream os;
		protected String key;
		protected String cacheFilename;
		private boolean mIsCommited = false;
		protected File cacheFile;

		public OutputStream getOutputStream() {
			return os;
		}

		public void commit() {

			insertEntry(key, cacheFilename);
			mIsCommited = true;
		}

		public void finished() {
			if (!mIsCommited) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e(getClass().getName(),
							"cannot close failed outputstream", e);
				}
				cacheFile.delete();
			}

		}
	}

	public WriteAsync put(String key) throws IOException {
		WriteAsync retval = new WriteAsync();

		String cacheFilename = getCacheFilename(key);
		if (cacheFilename == null) {
			// new insert
			cacheFilename = createCacheFilename(key);
		}

		File cacheFile = getCacheFile(cacheFilename);

		if (!mCacheDir.exists()) {
			if (!mCacheDir.mkdir()) {

			}
		}
		if (!cacheFile.exists()) {
			cacheFile.createNewFile();
		}
		retval.os = new FileOutputStream(cacheFile);
		retval.key = key;
		retval.cacheFilename = cacheFilename;
		retval.cacheFile = cacheFile;

		return retval;
	}

	private File getCacheFile(String filename) {
		File cacheFile = new File(mCacheDir, filename);
		return cacheFile;
	}

	/**
	 * @param key
	 * @return InputStream to the cached file or null if the key does not exist
	 */
	public synchronized InputStream get(String key) {
		InputStream retval = null;

		String filename = getCacheFilename(key);
		if (filename != null) {
			File cacheFile = getCacheFile(filename);
			if (cacheFile.exists() && cacheFile.isFile() && cacheFile.canRead()) {
				try {
					retval = new FileInputStream(cacheFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// delete from database
				mDB.delete("fileCache", "key=?", new String[] { key });
			}
		}
		return retval;
	}

	public synchronized void delete(String key) {
		String cacheFilename = getCacheFilename(key);
		try {
			if (cacheFilename != null) {
				File cacheFile = getCacheFile(cacheFilename);
				cacheFile.delete();
			}
		} finally {
			mDB.delete("fileCache", "key=?", new String[] { key });
		}
	}

	public synchronized void deleteOldest() {
		Cursor c = mDB.query("fileCache", new String[] { "key" }, null, null,
				null, null, "dateInsert ASC");
		try {
			if (c.moveToFirst()) {
				delete(c.getString(0));
			}
		} finally {
			c.close();
		}
	}

	public int numFiles() {
		int retval = -1;
		Cursor c = mDB.rawQuery("SELECT COUNT(*) FROM fileCache", null);
		try {
			if (c.moveToFirst()) {
				retval = c.getInt(0);
			}
		} finally {
			c.close();
		}
		return retval;
	}
}
