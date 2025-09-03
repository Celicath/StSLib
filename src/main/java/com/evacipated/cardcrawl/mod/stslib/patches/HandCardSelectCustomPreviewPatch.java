package com.evacipated.cardcrawl.mod.stslib.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

import java.util.function.Function;

public class HandCardSelectCustomPreviewPatch {
    private static Function<AbstractCard, AbstractCard> customPreviewFunction = null;
    private static AbstractCard cachedCard = null;
    public static void setCustomPreviewFunction(Function<AbstractCard, AbstractCard> newFunction) {
        customPreviewFunction = newFunction;
        cachedCard = null;
    }

    @SpirePatch2(clz = HandCardSelectScreen.class, method = "update")
    public static class ChangeUpgradeCard {
        @SpirePostfixPatch
        public static void Postfix(HandCardSelectScreen __instance) {
            if (customPreviewFunction != null && __instance.numCardsToSelect == 1 && __instance.selectedCards.size() == 1) {
                AbstractCard card = __instance.selectedCards.getTopCard();
                if (cachedCard != card) {
                    __instance.upgradePreviewCard = customPreviewFunction.apply(card.makeStatEquivalentCopy());
                    cachedCard = card;
                }
            }
        }
    }
}
