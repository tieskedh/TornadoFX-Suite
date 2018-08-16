package com.github.hd.tornadofxsuite.controller

import com.github.hd.tornadofxsuite.model.ClassBreakDown
import com.github.hd.tornadofxsuite.view.MainView
import com.intellij.psi.PsiElement
import javafx.event.EventTarget
import tornadofx.*
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FXTestGenerator: Controller() {
    val kotlinFiles = ArrayList<File>()
    val kotlinClasses = ArrayList<ClassBreakDown>()
    private val view: MainView by inject()
    private val scanner: ClassScanner by inject()

    fun walk(path: String) {
        view.console.items.clear()
        view.console.items.add("SEARCHING FILES...")
        Files.walk(Paths.get(path)).use { allFiles ->
            allFiles.filter { path -> path.toString().endsWith(".kt") }
                    .forEach {
                        fileOutputRead(it)
                    }
        }
    }

    private fun fileOutputRead(path: Path) {
        val file = File(path.toUri())
        readFiles(file)
    }

    private fun readFiles(file: File) {
        val fileText = file.bufferedReader().use(BufferedReader::readText)
        if (filterFiles(fileText)) {
            view.console.items.add(view.consolePath + file.toString())
            view.console.items.add("READING FILES...")
            kotlinFiles.add(file)
            view.console.items.add(fileText)
            view.console.items.add("===================================================================")
            kotlinClasses.add(scanner.parseAST(fileText))
        }
    }

    // filter files for only Views and Controllers
    private fun filterFiles(fileText: String): Boolean {
        return !fileText.contains("ApplicationTest()")
                && !fileText.contains("src/test")
                && !fileText.contains("@Test")
                && !fileText.contains("Stylesheet()")
    }

    fun detectModels(psiElement: PsiElement) {

    }

    fun detectControls() {

    }

    fun detectEvents(eventTarget: EventTarget) {

    }
}