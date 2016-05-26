package Tmdb;

public class LDistance {

	/*
	 * Returns the Levenshtein distance b/w 2 strings
	 */
	private static int LD(String sA, String tA) {
		
		String s = sA.toLowerCase();
		String t = tA.toLowerCase();
		
		int d[][];
		int s_l, t_l;
		int i, j;
		int cost;

		if (s.isEmpty()) {
			return t.length();
		}
		if (t.isEmpty()) {
			return s.length();
		}
		s_l = s.length();
		t_l = t.length();
		d = new int[s_l + 1][t_l + 1];

		for (i = 0; i <= s_l; i++)
			d[i][0] = i;

		for (j = 0; j <= t_l; j++)
			d[0][j] = j;

		for (i = 1; i <= s_l; i++) {

			for (j = 1; j <= t_l; j++) {
				if (s.charAt(i - 1) == t.charAt(j - 1)) {
					cost = 0;
				} else {
					cost = 1;
				}
				d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
			}

		}

		return d[s_l][t_l];

	}

	private static int minimum(int i, int j, int k) {

		int min = i;
		if (j < i)
			min = j;
		if (k < min)
			min = k;
		return min;

	}

	public static void printM(int m[][]) {
		for (int row = 0; row < m.length; row++) {

			for (int col = 0; col < m[row].length; col++) {
				System.out.print(m[row][col] + " ");
			}
			System.out.println();
		}
	}

	public static double similarity(String aS, String aT) {
		String s = aS.trim();
		String t = aT.trim();
		int longerLength = s.length() > t.length() ? s.length() : t.length();
		if (longerLength == 0) {
			return 1.0;
		}
		return ((longerLength - LD(s, t)) / (double) longerLength);

	}
	public static void printSimilarity(String s, String t) {
        System.out.println(String.format(
            "%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
    }
 
    public static void main(String[] args) {
        printSimilarity("", "");
        printSimilarity("1234567890", "1");
        printSimilarity("1234567890", "123");
        printSimilarity("1234567890", "1234567");
        printSimilarity("1234567890", "1234567890");
        printSimilarity("1234567890", "1234567980");
        printSimilarity("47/2010", "472010");
        printSimilarity("47/2010", "472011");
        printSimilarity("47/2010", "AB.CDEF");
        printSimilarity("47/2010", "4B.CDEFG");
        printSimilarity("47/2010", "AB.CDEFG");
        printSimilarity("The quick fox jumped", "The fox jumped");
        printSimilarity("The quick fox jumped", "The fox");
        printSimilarity("The quick fox jumped", "The quick fox jumped off the balcany");
        printSimilarity("kitten", "sitting");
        printSimilarity("the greatest", "the greatest game ever played");
        printSimilarity("The Greatest Miracle", "the greatest game ever played dvdrip");
        printSimilarity("2 Guns WEB DL PublicHD", "One Girl, 2 Guns");
        printSimilarity("Black Swan", "Black Swan ");
        printSimilarity("Black Swan", "Black Swan");
    }
}
