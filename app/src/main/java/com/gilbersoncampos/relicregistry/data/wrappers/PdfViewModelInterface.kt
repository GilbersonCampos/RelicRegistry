package com.gilbersoncampos.relicregistry.data.wrappers

import java.io.File

interface PdfViewModelInterface {
    fun generatePdf()
    fun getPdf():File
}