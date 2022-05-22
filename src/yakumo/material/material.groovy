/*
 * material.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */

related {
	outDir 'build/docs'
	// relatedフォルダ配下のファイルすべてを関連ファイルとしてコピー対象に設定します
	File related = new File(scriptFile.parentFile, 'related')
	def scanner = new AntBuilder().fileScanner {
		fileset(dir: related.path) { include(name: '**/*') }
	}
	for (File file in scanner){
		source 'draft', file.absolutePath.substring(related.absolutePath.length() + 1), file
	}
}

material {
	clmap new File(convDir, 'thtml.tpac')
	def templateDir = new File(convDir, 'template')
	['index', 'frag'].each {
		template it, new File(templateDir, "${it}.html")
	}
}
