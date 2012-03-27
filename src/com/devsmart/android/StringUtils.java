package com.devsmart.android;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class StringUtils {

	public static String loadRawResourceString(Resources res, int resourceId)
			throws IOException {
		InputStream is = res.openRawResource(resourceId);
		return loadString(is);
	}

	public static String loadAssetString(Resources res, String filename)
			throws IOException {
		InputStream is = res.getAssets().open(filename);
		return loadString(is);
	}

	public static String loadString(InputStream is) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(is);
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			builder.append(buf, 0, numRead);
		}
		return builder.toString();
	}

	public static String[] union(String[] a, String[] b) {
		LinkedList<String> retval = new LinkedList<String>();
		for (int i = 0; i < a.length; i++) {
			retval.add(a[i]);
		}

		for (int i = 0; i < b.length; i++) {
			if (!retval.contains(b[i])) {
				retval.add(b[i]);
			}
		}

		String[] retarray = new String[retval.size()];
		retval.toArray(retarray);
		return retarray;

	}

	public static String[] intersection(String[] a, String[] b) {
		List<String> blist = Arrays.asList(b);
		LinkedList<String> retval = new LinkedList<String>();

		for (int i = 0; i < a.length; i++) {
			if (blist.contains(a[i])) {
				retval.add(a[i]);
			}
		}

		String[] retarray = new String[retval.size()];
		retval.toArray(retarray);
		return retarray;
	}

	public abstract static class ClickSpan extends ClickableSpan {

	}

	public static void clickify(TextView view, final String clickableText,
			final ClickSpan span) {

		CharSequence text = view.getText();
		String string = text.toString();

		int start = string.indexOf(clickableText);
		int end = start + clickableText.length();
		if (start == -1)
			return;

		if (text instanceof Spannable) {
			((Spannable) text).setSpan(span, start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			SpannableString s = SpannableString.valueOf(text);
			s.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			view.setText(s);
		}

		MovementMethod m = view.getMovementMethod();
		if ((m == null) || !(m instanceof LinkMovementMethod)) {
			view.setMovementMethod(LinkMovementMethod.getInstance());
		}
	}

	private static HashMap<String, String> htmlEntities;
	static {
		htmlEntities = new HashMap<String, String>();
		htmlEntities.put("&lt;", "<");
		htmlEntities.put("&gt;", ">");
		htmlEntities.put("&amp;", "&");
		htmlEntities.put("&quot;", "\"");
		htmlEntities.put("&agrave;", "�");
		htmlEntities.put("&Agrave;", "�");
		htmlEntities.put("&acirc;", "�");
		htmlEntities.put("&auml;", "�");
		htmlEntities.put("&Auml;", "�");
		htmlEntities.put("&Acirc;", "�");
		htmlEntities.put("&aring;", "�");
		htmlEntities.put("&Aring;", "�");
		htmlEntities.put("&aelig;", "�");
		htmlEntities.put("&AElig;", "�");
		htmlEntities.put("&ccedil;", "�");
		htmlEntities.put("&Ccedil;", "�");
		htmlEntities.put("&eacute;", "�");
		htmlEntities.put("&Eacute;", "�");
		htmlEntities.put("&egrave;", "�");
		htmlEntities.put("&Egrave;", "�");
		htmlEntities.put("&ecirc;", "�");
		htmlEntities.put("&Ecirc;", "�");
		htmlEntities.put("&euml;", "�");
		htmlEntities.put("&Euml;", "�");
		htmlEntities.put("&iuml;", "�");
		htmlEntities.put("&Iuml;", "�");
		htmlEntities.put("&ocirc;", "�");
		htmlEntities.put("&Ocirc;", "�");
		htmlEntities.put("&ouml;", "�");
		htmlEntities.put("&Ouml;", "�");
		htmlEntities.put("&oslash;", "�");
		htmlEntities.put("&Oslash;", "�");
		htmlEntities.put("&szlig;", "�");
		htmlEntities.put("&ugrave;", "�");
		htmlEntities.put("&Ugrave;", "�");
		htmlEntities.put("&ucirc;", "�");
		htmlEntities.put("&Ucirc;", "�");
		htmlEntities.put("&uuml;", "�");
		htmlEntities.put("&Uuml;", "�");
		htmlEntities.put("&nbsp;", " ");
		htmlEntities.put("&copy;", "\u00a9");
		htmlEntities.put("&reg;", "\u00ae");
		htmlEntities.put("&euro;", "\u20a0");
		htmlEntities.put("&apos;", "'");
		htmlEntities.put("&mdash;", "-");
	}

	public static String unescapeHTML(String input) {
		StringBuilder builder = new StringBuilder();
		int pos = 0;
		int c = input.indexOf('&', pos);

		while (c != -1) {
			builder.append(input.substring(pos, c));
			int posEndToken = input.indexOf(';', c);
			if (posEndToken != -1) {
				posEndToken++;
				String token = input.substring(c, posEndToken);
				String unescapeToken = htmlEntities.get(token);
				if (unescapeToken != null) {
					builder.append(unescapeToken);
				} else {
					builder.append(input.substring(pos, posEndToken));
				}
				pos = posEndToken;
			} else {
				pos = c;
				builder.append(input.charAt(pos));
				pos++;
			}
			c = input.indexOf('&', pos);
		}

		builder.append(input.substring(pos));

		return builder.toString();
	}

	public static String getSha1Hash(String input) throws Exception {
		String retval = null;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				input.getBytes("UTF-8"));
		MessageDigest hash = MessageDigest.getInstance("SHA1");
		byte[] buffer = new byte[1024];

		int numRead = 0;
		while ((numRead = inputStream.read(buffer)) != -1) {
			hash.update(buffer, 0, numRead);
		}

		retval = toHexString(hash.digest());
		return retval;

	}

	protected static final byte[] Hexhars = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] input) {
		StringBuilder buf = new StringBuilder(2 * input.length);
		for (int i = 0; i < input.length; i++) {
			int v = input[i] & 0xff;
			buf.append((char) Hexhars[v >> 4]);
			buf.append((char) Hexhars[v & 0xf]);
		}
		return buf.toString();
	}

	// public static String loadRawResourceString(Resources res, int resourceId)
	// throws IOException {
	// StringBuilder builder = new StringBuilder();
	// InputStream is = res.openRawResource(resourceId);
	// InputStreamReader reader = new InputStreamReader(is);
	// char[] buf = new char[1024];
	// int numRead = 0;
	// while ((numRead = reader.read(buf)) != -1) {
	// builder.append(buf, 0, numRead);
	// }
	// return builder.toString();
	//
	// }

	public static String elipse(String input, int numMaxChar) {
		if (input.length() >= numMaxChar - 3) {
			String retval = input.substring(0, numMaxChar);
			retval += "...";
			return retval;
		} else {
			return input;
		}
	}

	public static String replaceAll(String input, String regex, String value) {
		return input.replaceAll(regex,
				value == null ? "" : Matcher.quoteReplacement(value));
	}

}
