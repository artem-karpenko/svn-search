package ua.org.yozh

/**
 * @author artem
 */
object RunIndex {
  def main(args: Array[String]) {
    val repoUrl = args(0)

    println("Indexing repositories...")
    val indexer = new Indexer(repoUrl, "artem", "oxseed")
    indexer.index()
  }
}
