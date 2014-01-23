package ua.org.yozh

import org.tmatesoft.svn.core.{SVNNodeKind, SVNDirEntry}
import org.tmatesoft.svn.core.io.SVNRepository
import java.io.{File, ByteArrayOutputStream, OutputStream}
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.util.Version
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.index.{IndexWriterConfig, IndexWriter}
import org.apache.lucene.document.{TextField, Field, Document}

/**
 * @author artem
 */
class IndexingVisitor(repository: SVNRepository) extends SvnEntryVisitor {
  val analyzer = new StandardAnalyzer(Version.LUCENE_46)
  val directory = FSDirectory.open(new File("d:/tmp/index"))
  val indexWriter = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_46, analyzer))

  def visit(path: String, entry: SVNDirEntry) {
    if (entry.getKind == SVNNodeKind.FILE) {
      val contents = new ByteArrayOutputStream()
      repository.getFile(path + "/" + entry.getName, -1, null, contents)

      val document = new Document()
      document.add(new Field("contents", contents.toString("UTF-8"), TextField.TYPE_STORED))
      document.add(new Field("path", path + "/" + entry.getName, TextField.TYPE_STORED))
      document.add(new Field("name", entry.getName, TextField.TYPE_STORED))
      indexWriter.addDocument(document)
    }
  }

  def close() {
    if (indexWriter != null) {
      indexWriter.close()
    }
  }
}
