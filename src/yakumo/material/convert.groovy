/*
 * script.groovy
 *
 * Copyright (C) io.github.longfish801 All Rights Reserved.
 */

import io.github.longfish801.tpac.TpacServer

load {
	material 'fyakumo', 'thtml', scriptFile.parentFile
}

script {
	// 原稿を読みこみます
	def server = new TpacServer().soak(new File(scriptFile.parentFile, '../draft.tpac'))
	append 'draft', server.draft
	// 一部のキーに設定されたテキストをbltxt形式に変換するよう設定します
	targets {
		server.draft.lowers.values().each { def frag ->
			target "${frag.name}#dflt", frag.dflt.join(System.lineSeparator)
			target "${frag.name}#title", frag.title
		}
	}
	results {
		File docsDir = new File('build/docs')
		result 'index', new File(docsDir, 'index.html')
		templateKey 'index', 'index'
		server.draft.lowers.values().each { def frag ->
			result frag.name, new File(docsDir, "${frag.name}.html")
			templateKey frag.name, 'frag'
		}
	}
	doLast {
		fprint.logs.each { println it }
		assert fprint.warns.size() == 0
	}
}
