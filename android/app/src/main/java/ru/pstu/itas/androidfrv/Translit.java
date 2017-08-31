package ru.pstu.itas.androidfrv;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * based on https://gist.github.com/alexzaitsev/5f74322a714e83e464a3
 */
public class Translit
{

	private final static int UPPER = 1;
	private final static int LOWER = 2;

	private final static Map<String, String> map = makeTranslitMap();

	private static Map<String, String> makeTranslitMap()
	{
		Map<String, String> map = new HashMap<>();
		map.put("а", "a");
		map.put("б", "b");
		map.put("в", "v");
		map.put("г", "g");
		map.put("д", "d");
		map.put("е", "e");
		map.put("ё", "yo");
		map.put("ж", "zh");
		map.put("з", "z");
		map.put("и", "i");
		map.put("й", "j");
		map.put("к", "k");
		map.put("л", "l");
		map.put("м", "m");
		map.put("н", "n");
		map.put("о", "o");
		map.put("п", "p");
		map.put("р", "r");
		map.put("с", "s");
		map.put("т", "t");
		map.put("у", "u");
		map.put("ф", "f");
		map.put("х", "h");
		map.put("ц", "ts");
		map.put("ч", "ch");
		map.put("ш", "sh");
		map.put("ъ", "`");
		map.put("у", "y");
		map.put("ь", "'");
		map.put("ю", "yu");
		map.put("я", "ya");
		map.put("кс", "x");
		map.put("в", "w");
		map.put("к", "q");
		map.put("ий", "iy");
		return map;
	}

	private static int charClass(char c)
	{
		return Character.isUpperCase(c) ? UPPER : LOWER;
	}

	private static String get(String s)
	{
		int charClass = charClass(s.charAt(0));
		String result = map.get(s.toLowerCase());
		return result == null ? "" : (charClass == UPPER ? (result.charAt(0) + "").toUpperCase() +
				(result.length() > 1 ? result.substring(1) : "") : result);
	}

	public static String translit(String text)
	{
		int len = text.length();
		if (len == 0)
		{
			return text;
		}
		if (len == 1)
		{
			return get(text);
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; )
		{
			String toTranslate = text.substring(i, i <= len - 2 ? i + 2 : i + 1);
			String translated = get(toTranslate);
			if (TextUtils.isEmpty(translated))
			{
				translated = get(toTranslate.charAt(0) + "");
				sb.append(TextUtils.isEmpty(translated) ? toTranslate.charAt(0) : translated);
				i++;
			} else
			{
				sb.append(TextUtils.isEmpty(translated) ? toTranslate : translated);
				i += 2;
			}
		}
		return sb.toString();
	}
}