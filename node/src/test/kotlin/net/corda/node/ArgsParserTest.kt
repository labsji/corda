package net.corda.node

import joptsimple.OptionException
import net.corda.core.isSameFileAs
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.Test
import java.nio.file.Paths

class ArgsParserTest {
    private val parser = ArgsParser()

    @Test
    fun `missing base-directory`() {
        val cmdLineOptions = parser.parse()
        assertThat(cmdLineOptions.baseDirectory.isSameFileAs(Paths.get("."))).isTrue()
    }

    @Test
    fun `base-directory with missing argument`() {
        assertThatExceptionOfType(OptionException::class.java).isThrownBy {
            parser.parse("--base-directory")
        }.withMessageContaining("base-directory")
    }

    @Test
    fun `base-directory with argument`() {
        val path = Paths.get("tmp").toAbsolutePath()
        val cmdLineOptions = parser.parse("--base-directory", path.toString())
        assertThat(cmdLineOptions.baseDirectory).isEqualTo(path)
    }
}