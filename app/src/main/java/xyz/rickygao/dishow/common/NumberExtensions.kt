package xyz.rickygao.dishow.common

import java.text.NumberFormat

fun Double.roundFraction(digits: Int = 2): String =
        NumberFormat.getInstance().apply { maximumFractionDigits = digits }.format(this)