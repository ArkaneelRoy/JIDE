package com.itsaky.androidide.flutter.pubdev

import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CompletableFuture
import java.util.Scanner

/**
 * PubDevBrowser provides a lightweight interface to search and manage Flutter/Dart
 * dependencies from pub.dev. This enables one-tap dependency management directly
 * within the AndroidIDE Ultra terminal.
 */
class PubDevBrowser(private val projectRoot: File) {

    companion object {
        private const val PUB_DEV_API = "https://pub.dev/api/packages"
        private const val TAG = "PubDevBrowser"
    }

    /**
     * Search for packages on pub.dev
     * @param query The search query (e.g., "http", "provider")
     * @return A CompletableFuture containing a list of package names
     */
    fun searchPackages(query: String): CompletableFuture<List<String>> {
        return CompletableFuture.supplyAsync {
            try {
                val searchUrl = URL("$PUB_DEV_API?q=$query")
                val connection = searchUrl.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                
                if (connection.responseCode == 200) {
                    val response = Scanner(connection.inputStream).useDelimiter("\\A").next()
                    // In a real implementation, we would use a JSON parser like Gson or Kotlinx.Serialization
                    // For this prototype, we'll simulate the extraction
                    parsePackageNames(response)
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    private fun parsePackageNames(json: String): List<String> {
        // Mock parsing logic for the prototype
        return listOf("http", "provider", "flutter_bloc", "dio", "get_it")
            .filter { it.contains(json.substringAfter("q=").substringBefore("&"), ignoreCase = true) }
    }

    /**
     * Get package information from pub.dev
     */
    fun getPackageInfo(packageName: String): CompletableFuture<PackageInfo> {
        return CompletableFuture.supplyAsync {
            // Simulated API call
            PackageInfo(
                name = packageName,
                version = "1.0.0",
                description = "A powerful package for $packageName",
                author = "Flutter Community",
                homepage = "https://pub.dev/packages/$packageName"
            )
        }
    }

    /**
     * Add a dependency to pubspec.yaml and run 'flutter pub get'
     * @param packageName The name of the package
     * @param version The version to add (optional, defaults to latest)
     * @return A CompletableFuture indicating success or failure
     */
    fun addDependency(packageName: String, version: String = ""): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync {
            try {
                val pubspecFile = File(projectRoot, "pubspec.yaml")
                if (!pubspecFile.exists()) return@supplyAsync false

                val lines = pubspecFile.readLines().toMutableList()
                val dependenciesIndex = lines.indexOfFirst { it.trim() == "dependencies:" }
                
                if (dependenciesIndex != -1) {
                    val dependencyLine = "  $packageName: ${version.ifEmpty { "any" }}"
                    lines.add(dependenciesIndex + 1, dependencyLine)
                    pubspecFile.writeText(lines.joinToString("\n"))
                    
                    // Trigger flutter pub get
                    runFlutterPubGet()
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun runFlutterPubGet() {
        try {
            ProcessBuilder("flutter", "pub", "get")
                .directory(projectRoot)
                .start()
                .waitFor()
        } catch (e: Exception) {
            // Log error
        }
    }
}

/**
 * Data class representing package information from pub.dev
 */
data class PackageInfo(
    val name: String,
    val version: String,
    val description: String,
    val author: String,
    val homepage: String
)
