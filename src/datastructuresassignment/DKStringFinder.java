/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datastructuresassignment;

import java.util.ArrayList; 

/**
 *
 * @author Derek Keogh
 * This class is still being developed, only to make it more efficient
 * It works as it is now, all tested and debugged
 * 
 * It is a string finder, pass in the search string and the text to search.
 * if found the number of times the string was found is stored
 * and the location of each found string
 * 
 * I was using two long numbers to do the comparisons with
 * but that only worked for search strings with ten characters or less as each
 * character had a three digit code, ten characters = 30 digits. CRASH!
 * So now I'm using two array lists<Long> to do the comparisons on.
 * This is a bit of a hack but it allows the code to handle
 * search strings of any length. I have allowed for each item in the array
 * to hold only the values of seven characters just incase the character has an extended
 * code ie. four digits, this is something that will change as I develop
 * this class. I intend to measure the character codes to fit in the amount
 * closer to 30 digits.
 * 
 * The initial comparison is performed by adding code point values.
 * EXAMPLE:
 * Search String:  "ello"
 * Text to Search: "Hello World! Hello Universe!"
 * 
 * Search String:  "e" : 101    "l" : 108     "l" : 108     "o" : 111
 * Text to Search: "H" : 72     "e" : 101     "l" : 108     "l" : 108
 * 
 * Add all the values
 * 
 * Search String:  "ello" : 428
 * Text to Search: "Hell" : 389
 * 
 * Are the totals equal? No
 * 
 * Increment the Text to search
 *   Subtract the value of the the "H", ("Hell" : 428) - ("H" : 70) = 317
 *   Then Add the value of the next character in the Text to Search Block
 *   In this case its "o", ("ell" : 388) + ("o" : 111) = 428
 *   Search String:  "ello" : 428
 *   Text to Search: "ello" : 428
 *   Are the totals equal? Yes
 *     Check the order of the values
 *     Search String:  "e" : 101    "l" : 108     "l" : 108     "o" : 111
 *     Text to Search: "e" : 101    "l" : 108     "l" : 108     "o" : 111
 *     Are they in order? Yes
 *     Increment the match count
 *     Set the start search point to the character after the last character in the Text to Search block
 *     The next character after the "o" is a " "
 *  Start again until the end of the Text to Search block
 * 
 */
public class DKStringFinder {

    ArrayList<Long> al_StrSearchNumericComposite =  new ArrayList<Long>();
    ArrayList<Long> al_StrCurSegmentNumericComposite =  new ArrayList<Long>();
    
    private int i_CurSegTotal;
    private int i_StartPoint;
    private int i_EndPoint;
    private int i_StrSearchTotal;
    private int i_MatchCount;
    private String str_CurSegment;
    private String str_MatchLocations;
    
    public DKStringFinder()
    {
        i_MatchCount = 0;
        i_CurSegTotal = -1;
        i_StartPoint = -1;
        i_EndPoint = -1;
        i_StrSearchTotal = -1;
        str_CurSegment = "";
        str_MatchLocations = "";
    }
    
    public int i_GetMatchCount()
    {
        return i_MatchCount;//al_MatchLocations.size();
    }
            
    public String get_strGetMatchLocations()
    {
        return str_MatchLocations;
    }
    
    public String str_BruteForceSearchForFirstMatch(String strText, String strPattern)
    {
        int n = strText.length();
        int m = strPattern.length();
        
        String t = strText;
        String p = strPattern;
        for (int i = 0; i < n-m; i++)
        {
            int j = 0;
            while (j < m && t.charAt(i+j) == p.charAt(j))
            {
                j++;
            }
            if (j == m)
                return "Found at " + i + "\n";
      
        }
        return "Not Found\n";
    }

    public String str_BruteForceSearchForAll(String strText, String strPattern)
    {
        int i_strTextLen = strText.length();
        int i_strPatternLen = strPattern.length();
        int i_ComparisonCount = 0;
        
        String strValue = "";
        
        for(int i = 0; i < i_strTextLen - i_strPatternLen+1; i++)
        {
            i_ComparisonCount++;
            if(strText.charAt(i) == strPattern.charAt(0))
            {
                int i_BruteForceMatchCount = 0;
                for(int x = 1; x < i_strPatternLen; x++)
                {
                    i_ComparisonCount++;
                    if(strText.charAt(i+x) == strPattern.charAt(x))
                    {
                        i_BruteForceMatchCount++;
                    }
                }
                if(i_BruteForceMatchCount == i_strPatternLen-1)
                {
                    strValue += "Found at position : " + i + "\n";
                }
            }
        }
        if(strValue.length() < i_strPatternLen)
            strValue = "Not Found";
        else
            strValue += "\n" + i_ComparisonCount + " Comparisons Made.";
        return strValue;
    }
    
    public int i_StringSearch(String str_TextToSearch, String str_SearchforThisString)
    {
        if(str_TextToSearch.length() <= 0 || str_SearchforThisString.length() <= 0)
        {
            return -1;//one of the input strings is too short
        }
        
        if(str_SearchforThisString.length() > str_TextToSearch.length())
        {
            return -1;//string to search for is bigger than the search text 
        }
 
        i_StartPoint = 0;
        //point to start searching from
        i_EndPoint = i_StartPoint + str_SearchforThisString.length();
        //last character position of search string
         
        int i_Counter = 0; // this counter will be used in several loops
        i_StrSearchTotal = 0;
        for(i_Counter = 0; i_Counter < i_EndPoint; i_Counter++)
        {
             i_StrSearchTotal += str_SearchforThisString.codePointAt(i_Counter);
             //add all the numeric characters values together
        }
        //total value of all characters in search string
        
        //create an array with the numeric chaarcter values
        al_StrSearchNumericComposite.clear();
        i_Counter = 0;
        while(i_Counter < str_SearchforThisString.length())
        {
            String  str_ValueOfSevenCharacters = "";
            for(int j = 0; j < 7; j++)
            {
                if(i_Counter < str_SearchforThisString.length())
                {
                    str_ValueOfSevenCharacters += str_SearchforThisString.codePointAt(i_Counter);
                }
                else
                {
                    j = 7;
                }
                i_Counter++;
            }
            al_StrSearchNumericComposite.add(Long.valueOf(str_ValueOfSevenCharacters));
        }
        
        i_CurSegTotal = 0;
        str_CurSegment = null;
        str_CurSegment = str_TextToSearch.substring(i_StartPoint, i_EndPoint);
        
        //get the first segment to search
        i_CurSegTotal = 0;
        for(i_Counter = 0; i_Counter < i_EndPoint; i_Counter++)
        {
             i_CurSegTotal += str_CurSegment.codePointAt(i_Counter);
             //add all the numeric characters values together
        }
        //calculate the the total of all the chaarcters in the search string
        //eg 0=48, 1=49, total is 97

        while(i_EndPoint <= str_TextToSearch.length())
        {
            boolean b_IncrementStartEndPointers = true;
            if(i_CurSegTotal == i_StrSearchTotal)
            {//if we get in here then a numeric total matches the search string, now check the order
                str_CurSegment = str_TextToSearch.substring(i_StartPoint, i_EndPoint);
                al_StrCurSegmentNumericComposite.clear();
                i_Counter = 0;
                while(i_Counter < str_CurSegment.length())
                {
                    String  str_ValueOfSevenCharacters = "";
                    for(int j = 0; j < 7; j++)
                    {
                        if(i_Counter < str_CurSegment.length())
                        {
                            str_ValueOfSevenCharacters += str_CurSegment.codePointAt(i_Counter);
                        }
                        else
                        {
                            j = 7;
                        }
                        i_Counter++;
                    }
                    al_StrCurSegmentNumericComposite.add(Long.valueOf(str_ValueOfSevenCharacters));
                }

                if(al_StrSearchNumericComposite.size() == al_StrCurSegmentNumericComposite.size())
                {
                    boolean b_Match = true;
                    int i_Size = al_StrSearchNumericComposite.size();
                    for(int i_count = 0; i_count < i_Size; i_count++)
                    {
                        if(b_Match != false&& al_StrSearchNumericComposite.get(i_count).longValue() != al_StrCurSegmentNumericComposite.get(i_count).longValue())
                        {
                            b_Match = false;//not equal
                            i_count = i_Size;
                        }
                    }
                    if(b_Match == true)
                    {//match found, numeric total and order is indentical to search string
                        str_MatchLocations += Integer.toString(i_StartPoint) + ", ";

                        i_MatchCount++;
                        //add to the match count
                        i_StartPoint = i_EndPoint;
                        i_EndPoint = i_StartPoint + str_SearchforThisString.length();
                        if(i_StartPoint > str_TextToSearch.length() || i_EndPoint > str_TextToSearch.length())
                        {//the end of the search text has been reached so return
                            return i_GetMatchCount();
                        }
                        //we found a match so we need to get the next segment of txt to search
                        str_CurSegment = str_TextToSearch.substring(i_StartPoint, i_EndPoint);

                        i_CurSegTotal = 0;
                        for(i_Counter = 0; i_Counter < str_CurSegment.length(); i_Counter++)
                        {
                             i_CurSegTotal += str_CurSegment.codePointAt(i_Counter);
                             //add all the numeric characters values together
                        }
                        b_IncrementStartEndPointers = false;
                    }
                }
            }

            if(b_IncrementStartEndPointers == true)
            {
                if(i_EndPoint >= str_TextToSearch.length())
                {//the end of the search text has been reached so return
                    return i_GetMatchCount();
                }

                i_CurSegTotal -= str_TextToSearch.codePointAt(i_StartPoint);
                //subtract the previous first character numeric value
                i_CurSegTotal += str_TextToSearch.codePointAt(i_EndPoint);

                i_StartPoint = i_StartPoint + 1;
                //point to start searching from
                i_EndPoint = i_EndPoint + 1;
                //last character position of search string
            }
        }
        return i_GetMatchCount();
    }
}        