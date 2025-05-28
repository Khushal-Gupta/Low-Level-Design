package util;

import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    public static int generateRandom(int minVal, int maxVal) {
        return random.nextInt(maxVal+1-minVal) + minVal;
    }

}
