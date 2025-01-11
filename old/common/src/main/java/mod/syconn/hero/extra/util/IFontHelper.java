/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package mod.syconn.hero.extra.util;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;

/**
 * Extension interface for {@link Font}.
 */
public interface IFontHelper
{
    FormattedText ELLIPSIS = FormattedText.of("...");

    Font self();
    
    default FormattedText ellipsis(FormattedText text, int maxWidth)
    {
        final Font self = self();
        final int strWidth = self.width(text);
        final int ellipsisWidth = self.width(ELLIPSIS);
        if (strWidth > maxWidth)
        {
            if (ellipsisWidth >= maxWidth) return self.substrByWidth(text, maxWidth);
            return FormattedText.composite(
                    self.substrByWidth(text, maxWidth - ellipsisWidth),
                    ELLIPSIS
            );
        }
        return text;
    }
}
