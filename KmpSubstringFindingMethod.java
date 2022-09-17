package analyzer;

import java.util.HashMap;
import java.util.Map;

public class KmpSubstringFindingMethod implements SubstringFindingMethod {
    @Override
    public String findSubstring(String text, HashMap<String, String[]> patternResultMap) {
        int highestPriority = -1;
        String highestPriorityType = "Unknown file type";
        int priority = -1;
        for (String pattern : patternResultMap.keySet()) {
            int patternLength = pattern.length();
            int textLength = text.length();

            int[] prefixFunction = new int[patternLength];
            int j = 0;

            computeLPSArray(pattern, patternLength, prefixFunction);

            int i = 0;
            while (i < textLength) {
                if (pattern.charAt(j) == text.charAt(i)) {
                    ++j;
                    ++i;
                }
                if (j == patternLength) {
                    priority = Integer.parseInt(patternResultMap.get(pattern)[0]);
                    if (priority > highestPriority) {
                        highestPriority = priority;
                        highestPriorityType = patternResultMap.get(pattern)[1];
                    }
                    break;
                } else if (i < textLength && pattern.charAt(j) != text.charAt(i)) {
                    if (j != 0)
                        j = prefixFunction[j - 1];
                    else
                        ++i;
                }
            }
        }
        return highestPriorityType;
    }

    private void computeLPSArray(String pattern, int patternLength, int[] prefixFunction) {

        int length = 0;
        int i = 1;
        prefixFunction[0] = 0;

        while (i < patternLength) {
            if (pattern.charAt(i) == pattern.charAt(length)) {
                length++;
                prefixFunction[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = prefixFunction[length - 1];
                } else {
                    prefixFunction[i] = length;
                    i++;
                }
            }
        }
    }
}
