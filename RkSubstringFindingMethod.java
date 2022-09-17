package analyzer;

import java.util.HashMap;

public class RkSubstringFindingMethod implements SubstringFindingMethod {
    private final int MODULO = 11;
    private final int PRIME_NUMBER = 3;
    @Override
    public String findSubstring(String text, HashMap<String, String[]> patternResultMap) {
        int highestPriority = -1;
        String highestPriorityType = "Unknown file type";
        int priority = -1;
        int substringHash = 0;
        String oldSubstring = "";
        int oldHash = 0;

        for (String pattern : patternResultMap.keySet()) {
            int patternLength = pattern.length();
            int textLength = text.length();
            int patternHash = hashFunction(pattern);
            for (int i = textLength; i > patternLength - 1; --i) {
                String substring = text.substring(i - patternLength, i);
                if (i == textLength) {
                    substringHash = hashFunction(substring);
                } else {
                    substringHash = rollingHashing(oldSubstring, substring, oldHash);
                }
                if (substringHash == patternHash) {
                    if (pattern.equals(substring)) {
                        priority = Integer.parseInt(patternResultMap.get(pattern)[0]);
                        if (priority > highestPriority) {
                            highestPriority = priority;
                            highestPriorityType = patternResultMap.get(pattern)[1];
                        }
                    }
                }
                oldSubstring = substring;
                oldHash = substringHash;
            }
        }

        return highestPriorityType;
    }

    private int hashFunction(String string) {
        int stringLength = string.length();
        int hash = 0;
        for (int i = 0; i < stringLength; ++i) {
            hash += string.charAt(i) * (int)Math.pow(PRIME_NUMBER, i);
        }
        hash = hash % MODULO;
        return hash;
    }

    private int rollingHashing(String oldString, String newString, int oldStringHash) {
        System.out.println("old string: " + oldString + ", new string: " + newString + ", old hash: " + oldStringHash);
        int firstCharValue = newString.charAt(0);
        int lastCharPosition = oldString.length() - 1;
        int lastCharValue = oldString.charAt(lastCharPosition);
        int result = (oldStringHash - (lastCharValue * (int)Math.pow(PRIME_NUMBER, lastCharPosition)));
        result = result  * (PRIME_NUMBER + firstCharValue);
        result = result % MODULO;
        int rightResult = hashFunction(newString);
        System.out.printf("result: " + result + ", right result: " + rightResult + "\n");
        return rightResult;
    }
}
