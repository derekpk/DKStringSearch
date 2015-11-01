package datastructuresassignment;

/**
 *
 * @author Derek Keogh
 */

/***************************************************************
 *
 *  Takes in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  Las Vegas version of the Rabin-Karp algorithm.
 *
 *  pattern: abracadabra
 *  text:    abacadabrabracabracadabrabrabracad 
 *  match:                 abracadabra          
 *
 *  pattern: rab
 *  text:    abacadabrabracabracadabrabrabracad 
 *  match:           rab                         
 *
 *  pattern: bcara
 *  text:         abacadabrabracabracadabrabrabracad 
 *
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ***************************************************************/

import java.math.BigInteger;
import java.util.Random;

public class RabinKarp {
    private String pat;      // the pattern  // needed only for Las Vegas
    private long patHash;    // pattern hash value
    private int M;           // pattern length
    private long Q;          // a large prime, small enough to avoid long overflow
    private int R;           // radix
    private long RM;         // R^(M-1) % Q
    private int i_MatchCount;
    
    public RabinKarp(int R, char[] pattern) {
        throw new RuntimeException("Operation not supported yet");
    }

    public RabinKarp(String pat) {
        i_MatchCount = 0;
        this.pat = pat;      // save pattern (needed only for Las Vegas)
        R = 256;
        M = pat.length();
        Q = longRandomPrime();

        // precompute R^(M-1) % Q for use in removing leading digit
        RM = 1;
        for (int i = 1; i <= M-1; i++)
           RM = (R * RM) % Q;
        patHash = hash(pat, M);
    } 

    // Compute hash for key[0..M-1]. 
    private long hash(String key, int M) { 
        long h = 0; 
        for (int j = 0; j < M; j++) 
            h = (R * h + key.charAt(j)) % Q; 
        return h; 
    } 

    // Las Vegas version: does pat[] match txt[i..i-M+1] ?
    private boolean check(String txt, int i) {
        for (int j = 0; j < M; j++) 
            if (pat.charAt(j) != txt.charAt(i + j)) 
                return false; 
        return true;
    }

    // Monte Carlo version: always return true
    private boolean check(int i) {
        return true;
    }

    // check for exact match
    public String search(String txt) {
        String str_offset = "";
        int N = txt.length(); 
        if (N < M)
            return str_offset = Integer.toString(N);
        long txtHash = hash(txt, M); 

        // check for match at offset 0
        if ((patHash == txtHash) && check(txt, 0))
            return str_offset = "0";

        // check for hash match; if hash match, check for exact match
        for (int i = M; i < N; i++) {
            // Remove leading digit, add trailing digit, check for match. 
            txtHash = (txtHash + Q - RM*txt.charAt(i-M) % Q) % Q; 
            txtHash = (txtHash*R + txt.charAt(i)) % Q; 

            // match
            int offset = i - M + 1;
            if ((patHash == txtHash) && check(txt, offset)){
                //return offset;
                str_offset += Integer.toString(offset) + ", ";
                i_MatchCount++;
            }
        }

        // no match
        return str_offset;
    }


    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = new BigInteger(31, new Random());
        return prime.longValue();
    }
    
    public int get_iMatchCount(){
        return i_MatchCount;
    }
}