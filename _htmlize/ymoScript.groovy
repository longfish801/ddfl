/*
 * ymoScript.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */

import io.github.longfish801.bltxt.BLtxt;
import io.github.longfish801.bltxt.node.*;
import org.apache.commons.io.FileUtils;

// 入力ファイル
File targetFile = new File(targetDir, 'draft.txt');

// 出力先フォルダ
File outDir = new File(targetDir, 'docs');

class DdflArticle {
	/** インデックス */
	int idx;
	/** 日付 */
	String yymmdd;
	/** タイトル */
	String title;
	/** 識別子 */
	String sign = '';
	/** 分類 */
	String kind = '';
	/** スタイル */
	String style = '';
	/** 本文 */
	BLtxt body;
	
	/**
	 * ファイル名を返します。
	 * @return ファイル名
	 */
	String getFname(){
		return "${yymmdd}_${sign}";
	}
	
	/**
	 * 長い日付文字列（yyyy.mm.dd）を返します。
	 * @return 日付文字列
	 */
	String getLongDate(){
		int yyInt = Integer.parseInt(yymmdd.substring(0, 2));
		String yy = String.format('%02d', yyInt);
		String yyyy = (yyInt > 90)? "19${yy}" : "20${yy}";
		return "${yyyy}.${yymmdd.substring(2, 4)}.${yymmdd.substring(4, 6)}";
	}
}

yakumo.script {
	// 変換資材を読みこみます
	['_bltxt', '_html', convDir].each { configure(it) };
	
	// 解析対象を設定します
	engine.appendTarget('draft', targetFile, '_bltxt');
	
	afterParse {
		// 各記事ページを出力先として設定します
		List articles = [];
		engine.bltxtMap['draft'].root.nodes.each { BLNode node ->
			switch (node){
				case { it.xmlTag == 'block' && it.tag == '見出し' }:
					articles << new DdflArticle();
					articles.last().idx = articles.size() - 1;
					def matcher = node.toString() =~ /【＝見出し】([\d]{6})\:(.+)/;
					if (matcher.matches()){
						articles.last().yymmdd = matcher.group(1);
						articles.last().title = matcher.group(2);
					} else {
						throw new Exception("見出しの形式が不正です。見出し=${node.toString()}");
					}
					articles.last().body = new BLtxt(new BLRoot());
					break;
				case { it.xmlTag == 'meta' && it.tag == 'ヘッダ情報' }:
					articles.last().sign = node.attrs[0];
					articles.last().kind = node.attrs[1];
					articles.last().style = node.attrs[2];
					break;
				default:
					articles.last().body.root << node;
			}
		}
		Map articleMap = [:];
		articles.each { articleMap[it.fname] = it }
		engine.bindClmap(convDir.name, ['articleMap': articleMap]);
		articleMap.each { String fname, DdflArticle article ->
			engine.appendOut(fname, new File(outDir, "${fname}.html"), convDir.name);
		}
		// 目次ページを出力先として設定します
		engine.appendOut('index', new File(outDir, 'index.html'), convDir.name, '#index');
	}
	
	// 固定ファイルの出力先を設定します
	assetHandler.outDir(outDir);
	
	// 出力先フォルダを空にします
	FileUtils.cleanDirectory(outDir);
}
