@file:Suppress("unused")

package com.scythetec.bdobosstimer.function

import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

fun Resources.getHtmlSpannedString(@StringRes id: Int): Spanned = getString(id).toHtmlSpan()

fun Resources.getHtmlSpannedString(@StringRes id: Int, vararg formatArgs: Any): Spanned = getString(id, *formatArgs).toHtmlSpan()

fun Resources.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int): Spanned = getQuantityString(id, quantity).toHtmlSpan()

fun Resources.getQuantityHtmlSpannedString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): Spanned = getQuantityString(id, quantity, *formatArgs).toHtmlSpan()

fun String.toHtmlSpan(): Spanned =
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)

fun nvl(value: Int?, valueIfNullOr0: Int): Int{
    if (value == null || value == 0){
        return valueIfNullOr0
    }
    return value
}

fun <T> nvl(value: T?, valueIfNullOr0: T): T{
    if (value == null){
        return valueIfNullOr0
    }
    return value
}

class KotlinExtension {
}
