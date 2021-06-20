package ru.vtb.bot.acronym.ext

fun String.clearMarkdownEscaping(): String =
    replace("\\*", "*")
        .replace("\\_", "_")
        .replace("\\[", "[")
