package com.googolmo.foursquare.utils;

public class Configuration {

	public static final String CLIENTID = "5TW3HZ2SI1UHPTK4O3D14NYDBJKSMI0T1DOHPAOKIPGNDZAF";
	public static final String CLIENTSECRET = "QC0MZNRVFQDYNDRUP0P3Z3WP0GIU2HGDXSIVBFHSK4LUNTP4";

	public static final boolean ISDEBUG = true;

	public static final String MAPABC_APIKEY = "c2b0f58a6f09cafd1503c06ef08ac7aeb7ddb91aaeab9458b6b92263b894f634b7b447425958c228";

	public static final String REDIRECTURL = "http%3A%2F%2Flocalhost%2F";

	public static String getClientID() {
		return CLIENTID;
	}

	public static String getClientSecret() {
		return CLIENTSECRET;
	}

	public static boolean getisDebug() {
		return ISDEBUG;
	}
}
