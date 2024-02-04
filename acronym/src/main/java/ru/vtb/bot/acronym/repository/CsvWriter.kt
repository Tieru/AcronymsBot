package ru.vtb.bot.acronym.repository

import java.io.FileOutputStream
import java.io.Writer

internal class CsvWriter {

    fun write(filePath: String, emitter: Emitter.() -> Unit) {
        CsvWriter(filePath = filePath)
            .apply(emitter)
            .close()
    }

    interface Emitter {

        fun writeRow(columns: List<String>)
    }

    class CsvWriter(filePath: String) : Emitter {

        private val writer = FileOutputStream(filePath).bufferedWriter()

        init {
            writer.write("\uFEFF")
        }

        override fun writeRow(columns: List<String>) {
            columns.forEach {
                writer.writeWithDelimiter(it)
            }
            writer.newLine()
        }

        fun close() {
            writer.close()
        }

        private fun Writer.writeWithDelimiter(value: String, delimiter: String = ";") {
            val escapedValue = value.replace("\n", " ")
            write(escapedValue + delimiter)
        }
    }
}
