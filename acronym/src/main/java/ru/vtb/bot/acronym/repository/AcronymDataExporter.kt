package ru.vtb.bot.acronym.repository

import org.slf4j.LoggerFactory
import ru.vtb.bot.acronym.entity.AcronymData
import java.io.File
import java.time.LocalDate

class AcronymDataExporter internal constructor(
    private val csvWriter: CsvWriter,
    private val storePath: String?,
) {

    fun export(data: List<AcronymData>) {
        val storePath = this.storePath ?: return
        try {
            exportSafe(data, storePath)
        } catch (t: Throwable) {
            LOGGER.error("Error exporting data", t)
        }
    }

    private fun exportSafe(data: List<AcronymData>, storePath: String) {
        val filepath = createFile(storePath)
        writeAcronyms(filepath, data)
        LOGGER.info("Data export success: ${data.size} entries saved to $filepath")
    }

    private fun createFile(storePath: String): String {
        val pathname = "${storePath.dropLastWhile { it == '/' }}/acronyms-${LocalDate.now()}.csv"

        File(pathname)
            .also {
                if (it.exists()) {
                    it.delete()
                }
                it.createNewFile()
            }

        return pathname
    }

    private fun writeAcronyms(filePath: String, tasks: List<AcronymData>) {
        csvWriter.write(filePath) {
            writeRow(
                listOf(
                    "Id",
                    "Value",
                    "Description",
                    "AddedById",
                    "AddedByUsername",
                )
            )

            tasks.forEach { task ->
                writeRow(
                    listOf(
                        task.id,
                        task.value,
                        task.description,
                        task.addedById?.toString().orEmpty(),
                        task.addedByUsername.orEmpty(),
                    )
                )
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AcronymDataExporter::class.java)
    }
}
