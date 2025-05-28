package service;

import util.RandomUtil;

public class DiceService {

    int noOfDices;
    private final int DICE_MIN_VALUE = 1;
    private final int DICE_MAX_VALUE = 6;

    public DiceService(int noOfDices) {
        this.noOfDices = noOfDices;
    }

    public Integer roll() {
        return RandomUtil.generateRandom(DICE_MIN_VALUE, noOfDices*DICE_MAX_VALUE);
    }
}
