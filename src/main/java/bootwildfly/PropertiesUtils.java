package bootwildfly;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author huanbinzhang
 * 
 */
public final class PropertiesUtils {

	static String DELIM_START = "${";
	static char DELIM_STOP = '}';
	static int DELIM_START_LEN = 2;
	static int DELIM_STOP_LEN = 1;

	public static Properties getProperties(String file) {
		InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
		if (inputStream == null) {
			return null;
		}
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return p;
	}

	public static String getProperties(String propertyName, Properties props) {
		String value = props.get(propertyName) == null ? null : props.get(propertyName).toString();
		return substVars(value, props);
	}

	/**
	 * Perform variable substitution in string <code>val</code> from the values of keys found in the system propeties.
	 * 
	 * <p>
	 * The variable substitution delimeters are <b>${</b> and <b>}</b>.
	 * 
	 * <p>
	 * For example, if the System properties contains "key=value", then the call
	 * 
	 * <pre>
	 * String s = OptionConverter.substituteVars(&quot;Value of key is ${key}.&quot;);
	 * </pre>
	 * 
	 * will set the variable <code>s</code> to "Value of key is value.".
	 * 
	 * <p>
	 * If no value could be found for the specified key, then the <code>props</code> parameter is searched, if the value could not be found
	 * there, then substitution defaults to the empty string.
	 * 
	 * <p>
	 * For example, if system propeties contains no value for the key "inexistentKey", then the call
	 * 
	 * <pre>
	 * String s = OptionConverter.subsVars(&quot;Value of inexistentKey is [${inexistentKey}]&quot;);
	 * </pre>
	 * 
	 * will set <code>s</code> to "Value of inexistentKey is []"
	 * 
	 * <p>
	 * An {@link java.lang.IllegalArgumentException} is thrown if <code>val</code> contains a start delimeter "${" which is not balanced by
	 * a stop delimeter "}".
	 * </p>
	 * 
	 * <p>
	 * <b>Author</b> Avy Sharell</a>
	 * </p>
	 * 
	 * @param val
	 *            The string on which variable substitution is performed.
	 * @throws IllegalArgumentException
	 *             if <code>val</code> is malformed.
	 */
	private static String substVars(String val, Properties props) throws IllegalArgumentException {

		StringBuffer sbuf = new StringBuffer();

		int i = 0;
		int j, k;

		while (true) {
			j = val.indexOf(DELIM_START, i);
			if (j == -1) {
				// no more variables
				if (i == 0) { // this is a simple string
					return val;
				} else { // add the tail string which contails no variables and return the result.
					sbuf.append(val.substring(i, val.length()));
					return sbuf.toString();
				}
			} else {
				sbuf.append(val.substring(i, j));
				k = val.indexOf(DELIM_STOP, j);
				if (k == -1) {
					throw new IllegalArgumentException('"' + val + "\" has no closing brace. Opening brace at position " + j + '.');
				} else {
					j += DELIM_START_LEN;
					String key = val.substring(j, k);
					// first try in System properties
					String replacement = getSystemProperty(key, null);
					// then try props parameter
					if (replacement == null && props != null) {
						replacement = props.getProperty(key);
					}

					if (replacement != null) {
						// Do variable substitution on the replacement string
						// such that we can solve "Hello ${x2}" as "Hello p1"
						// the where the properties are
						// x1=p1
						// x2=${x1}
						String recursiveReplacement = substVars(replacement, props);
						sbuf.append(recursiveReplacement);
					}
					i = k + DELIM_STOP_LEN;
				}
			}
		}
	}

	/**
	 * Very similar to <code>System.getProperty</code> except that the {@link SecurityException} is hidden.
	 * 
	 * @param key
	 *            The key to search for.
	 * @param def
	 *            The default value to return.
	 * @return the string value of the system property, or the default value if there is no property with that key.
	 * @since 1.1
	 */
	private static String getSystemProperty(String key, String def) {
		try {
			return System.getProperty(key, def);
		} catch (Throwable e) { // MS-Java throws com.ms.security.SecurityExceptionEx
			System.out.println("Was not allowed to read system property \"" + key + "\".");
			return def;
		}
	}

}
