package com.itheima.lucene.test;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class searchLucene {

	@Test
	public void test() throws Exception {
		//1、创建一个分词器对象
//		Analyzer analyzer = new StandardAnalyzer();
		IKAnalyzer analyzer = new IKAnalyzer();
		//2、创建解析器对象QueryParse,参数（1、默认搜索的域名；2、分词器：一定要和写入的分词器一致）
		QueryParser queryParser = new QueryParser("name", analyzer);
		//3、根据解析器对象，创建查询条件对象Query
		//创建query对象时候，如果parse()方法中自定语法，那么就根据自己写的语法来搜索；如果没有写域名那么就根据默认的域来搜索；
		Query query = queryParser.parse("desc:作者");
		//4、创建指定索引库流的对象Directory
		Directory directory = FSDirectory.open(new File("F:\\41\\lucene"));
		//5、创建读取索引库的对象IndexReader,需要参数（指定索引库地址的流对象）
		IndexReader indexReader = DirectoryReader.open(directory);//包含了索引库中所有数据：索引和文档
		//6、创建搜索对象IndexSearch，参数（读取索引库数据的读的对象IndexReader）
		IndexSearcher searcher = new IndexSearcher(indexReader);//包含了索引库中文档和索引！
		//7、根据搜索对象search搜索：参数（1、指定搜索条件对象Query；2、返回结果集记录数）；返回的对象：前几个数据
		TopDocs topDocs = searcher.search(query, 2);
		//8、根据返回的TopDocs,获取结果集是数组形式：坐标
		ScoreDoc[] docs = topDocs.scoreDocs;
		//9、遍历坐标
		for (ScoreDoc scoreDoc : docs) {
			//10、根据遍历的坐标，获取坐标中的文档ID
			int docId = scoreDoc.doc;
			//11、根据文档ID查询文档对象
			Document doc = searcher.doc(docId);
			//12、打印文档对象
			System.err.println("id=====" + doc.get("id"));
			System.err.println("name=====" + doc.get("name"));
			System.err.println("price=====" + doc.get("price"));
			System.err.println("pic=====" + doc.get("pic"));
		}
			
		
	}

}
